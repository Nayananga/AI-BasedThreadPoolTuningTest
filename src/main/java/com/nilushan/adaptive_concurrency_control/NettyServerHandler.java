package com.nilushan.adaptive_concurrency_control;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.benchmarks.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String testName;
    private final CustomThreadPool executingPool;

    public NettyServerHandler(String name, CustomThreadPool pool) {
        this.testName = name;
        this.executingPool = pool;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        StatusData.decreaseConnectionCounter();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        StatusData.increaseConnectionCounter();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        Timer.Context timerContext = NettyClient.LATENCY_TIMER.time();
        Timer.Context throughputContext = NettyClient.THROUGHPUT_TIMER.time();
        switch (testName) {
            case "Prime10k":
                executingPool.submitTask(new Prime10k(ctx, msg, timerContext, throughputContext));
                break;
            case "Prime100k":
                executingPool.submitTask(new Prime100k(ctx, msg, timerContext, throughputContext));
                break;
            case "Prime1m":
                executingPool.submitTask(new Prime1m(ctx, msg, timerContext, throughputContext));
                break;
            case "Prime10m":
                executingPool.submitTask(new Prime10m(ctx, msg, timerContext, throughputContext));
                break;
            case "DbWrite":
                executingPool.submitTask(new DbWrite(ctx, msg, timerContext, throughputContext));
                break;
            case "DbRead":
                executingPool.submitTask(new DbRead(ctx, msg, timerContext, throughputContext));
                break;
            case "Sqrt":
                executingPool.submitTask(new Sqrt(ctx, msg, timerContext, throughputContext));
                break;
            case "Factorial":
                executingPool.submitTask(new Factorial(ctx, msg, timerContext, throughputContext));
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
