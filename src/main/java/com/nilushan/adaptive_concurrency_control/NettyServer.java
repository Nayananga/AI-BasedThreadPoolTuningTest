package com.nilushan.adaptive_concurrency_control;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyServer {

    String test;
    CustomThreadPool executingPool;

    public NettyServer(String testName, CustomThreadPool pool) {
        this.test = testName;
        this.executingPool = pool;
    }

    public void start() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast("aggregator", new HttpObjectAggregator(1048576))
                                    .addLast(new NettyServerHandler(test, executingPool));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1000000)
                    .childOption(ChannelOption.SO_RCVBUF, 2147483647) // Increase receive buffer size
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(AdaptiveConcurrencyControl.SERVER_PORT).sync().channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
