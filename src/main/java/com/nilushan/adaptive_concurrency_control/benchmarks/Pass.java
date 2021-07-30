package com.nilushan.adaptive_concurrency_control.benchmarks;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.AdaptiveConcurrencyControl;
import com.nilushan.adaptive_concurrency_control.NettyClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Test to measure performance of Square root calculation
 */
public class Pass implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;
    private final Timer.Context throughputContext;

    public Pass(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerCtx, Timer.Context throughputContext) {
        this.msg = msg;
        this.ctx = ctx;
        this.timerContext = timerCtx;
        this.throughputContext = throughputContext;
    }

    @Override
    public void run() {
        NettyClient.IN_PROGRESS_COUNT++;

        try {
            Bootstrap passThroughClientBootstrap = new Bootstrap();
            passThroughClientBootstrap.group(ctx.channel().eventLoop());
            passThroughClientBootstrap.channel(NioServerSocketChannel.class);
//            passThroughClientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            passThroughClientBootstrap.remoteAddress(new InetSocketAddress("localhost", 30000));
            passThroughClientBootstrap.option(ChannelOption.AUTO_READ, false);
            passThroughClientBootstrap.handler(new PassHandler(timerContext, throughputContext));
            Channel f = passThroughClientBootstrap.connect().sync().channel();

            DefaultFullHttpRequest request;
            if (msg.content() == null) {
                request = new DefaultFullHttpRequest(
                        HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            } else {
                request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
                request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.content().readableBytes());
                request.content().clear().writeBytes(msg.content());

            }
            request.headers().set(HttpHeaderNames.HOST, "localhost");

            f.writeAndFlush(request);

            // Wait for the server to close the connection.
            f.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PassHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final Timer.Context timerContext;
    private final Timer.Context throughputContext;

    public PassHandler(Timer.Context timerContext, Timer.Context throughputContext) {
        this.timerContext = timerContext;
        this.throughputContext = throughputContext;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        HttpContent content = (HttpContent) msg;
        HttpResponse res = (HttpResponse) msg;

        try {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content.content());
            String contentType = res.headers().get(HttpHeaderNames.CONTENT_TYPE);
            boolean keepAlive = HttpUtil.isKeepAlive(res);

            if (contentType != null) {
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
            }
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
            }

        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in Netty Handler", e);
        }

        ctx.flush();
        NettyClient.IN_PROGRESS_COUNT--;
        throughputContext.stop();
        timerContext.stop(); // Stop Dropwizard metrics timer
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}