package com.nilushan.adaptive_concurrency_control;

import com.codahale.metrics.Timer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String testName;
    private final CustomThreadPool executingPool;
    private final Timer.Context timerContext;

    public NettyServerHandler(String name, CustomThreadPool pool, Timer.Context tContext) {
        this.testName = name;
        this.executingPool = pool;
        this.timerContext = tContext;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {

        switch (testName) {
            case "Prime1m":
                executingPool.submitTask(new Prime1m(ctx, msg, timerContext));
                break;
            case "Prime10m":
                executingPool.submitTask(new Prime10m(ctx, msg, timerContext));
                break;
            case "DbWrite":
                executingPool.submitTask(new DbWrite(ctx, msg, timerContext));
                break;
            case "DbRead":
                executingPool.submitTask(new DbRead(ctx, msg, timerContext));
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
