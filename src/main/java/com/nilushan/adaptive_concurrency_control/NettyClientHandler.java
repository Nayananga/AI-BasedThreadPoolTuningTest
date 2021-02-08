package com.nilushan.adaptive_concurrency_control;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    CustomThreadPool customThreadPool;

    public NettyClientHandler(CustomThreadPool customThreadPool) {
        this.customThreadPool = customThreadPool;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        int status = 0;
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            status = response.status().code();
            NettyClient.COOKIE_STRING = response.headers().getAll(HttpHeaderNames.SET_COOKIE);
            System.out.println("STATUS: " + response.status());
            System.out.println("VERSION: " + response.protocolVersion());
            System.out.println();
        }
        if (status == 200) {
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                System.out.flush();
                int currentThreadPoolSize = customThreadPool.getThreadPoolSize();
                float temp = Float.parseFloat(content.content().toString(CharsetUtil.UTF_8));
                int newThreadPoolSize = (int) temp;
                System.out.println("New ThreadPool Size: " + newThreadPoolSize);

                if (newThreadPoolSize > currentThreadPoolSize) {
                    customThreadPool.incrementPoolTo(newThreadPoolSize);
                } else if (newThreadPoolSize < currentThreadPoolSize) {
                    customThreadPool.decrementPoolSizeTo(newThreadPoolSize);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
