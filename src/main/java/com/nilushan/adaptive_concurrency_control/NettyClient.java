package com.nilushan.adaptive_concurrency_control;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.github.rollingmetrics.histogram.HdrBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.json.simple.JSONObject;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class NettyClient implements Runnable {
    public static int IN_PROGRESS_COUNT;
    public static MetricRegistry METRICS;
    public static HdrBuilder BUILDER;
    public static Timer LATENCY_TIMER;
    public static MetricRegistry METRICS2;
    public static HdrBuilder BUILDER2;
    public static Timer THROUGHPUT_TIMER;
    public static int oldInProgressCount;
    private static double oldTenSecondRate;
    int port;
    String host, optimization;
    CustomThreadPool customThreadPool;

    public NettyClient(int port, String host, String optimization, CustomThreadPool customThreadPool) {
        this.port = port;
        this.host = host;
        this.optimization = optimization;
        this.customThreadPool = customThreadPool;
        METRICS = new MetricRegistry();
        BUILDER = new HdrBuilder();
        BUILDER.resetReservoirOnSnapshot();
        BUILDER.withPredefinedPercentiles(new double[]{0.99}); // Predefine required percentiles
        LATENCY_TIMER = BUILDER.buildAndRegisterTimer(METRICS, "ThroughputAndLatency");
        METRICS2 = new MetricRegistry();
        BUILDER2 = new HdrBuilder();
        THROUGHPUT_TIMER = BUILDER2.buildAndRegisterTimer(METRICS2, "ThroughputAndLatency2");
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup(50); //TODO: This should be reduced

        try {
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    channelPipeline.addLast(new HttpClientCodec());
                    channelPipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
                    channelPipeline.addLast(new NettyClientHandler(customThreadPool));
                }
            });

            ChannelFuture channelFuture = clientBootstrap.connect().sync();

            // Send the HTTP request.
            int currentThreadPoolSize = customThreadPool.getThreadPoolSize();
            double currentTenSecondRate = THROUGHPUT_TIMER.getTenSecondRate();
            double rateDifference = (currentTenSecondRate - oldTenSecondRate) * 100 / oldTenSecondRate;
            int currentInProgressCount = IN_PROGRESS_COUNT;
            Snapshot latencySnapshot = LATENCY_TIMER.getSnapshot();
            double currentMeanLatency = latencySnapshot.getMean() / 1000000; // Divided by 1000000 to convert the time to ms
            double current99PLatency = latencySnapshot.get99thPercentile() / 1000000; // Divided by 1000000 to convert the time to ms

            AdaptiveConcurrencyControl.LOGGER
                    .info(currentThreadPoolSize + ", " + currentTenSecondRate + ", " + rateDifference + ", "
                            + currentInProgressCount + ", " + currentMeanLatency + ", " + current99PLatency);
            JSONObject jsonObject = new JSONObject();

            oldTenSecondRate = currentTenSecondRate;
            oldInProgressCount = currentInProgressCount;

            jsonObject.put("currentTenSecondRate", currentTenSecondRate);
            jsonObject.put("currentMeanLatency", currentMeanLatency);
            jsonObject.put("current99PLatency", current99PLatency);

            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            ByteBuf byteBuf = Unpooled.copiedBuffer(jsonObject.toString(), StandardCharsets.UTF_8);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            request.content().clear().writeBytes(byteBuf);

            channelFuture.channel().writeAndFlush(request);

            // Wait for the server to close the connection.
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
