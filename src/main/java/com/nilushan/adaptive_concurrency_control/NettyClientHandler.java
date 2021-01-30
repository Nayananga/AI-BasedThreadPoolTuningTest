package com.nilushan.adaptive_concurrency_control;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    CustomThreadPool customThreadPool;

    public NettyClientHandler(CustomThreadPool customThreadPool) {
        this.customThreadPool = customThreadPool;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

            System.out.println("STATUS: " + response.status());
            System.out.println("VERSION: " + response.protocolVersion());
            System.out.println();

            if (!response.headers().isEmpty()) {
                for (String name : response.headers().names()) {
                    for (String value : response.headers().getAll(name)) {
                        System.out.println("HEADER: " + name + " = " + value);
                    }
                }
                System.out.println();
            }

            if (HttpUtil.isTransferEncodingChunked(response)) {
                System.out.println("CHUNKED CONTENT {");
            } else {
                System.out.println("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            System.out.print(content.content().toString(CharsetUtil.UTF_8));
            System.out.flush();
            int currentThreadPoolSize = customThreadPool.getThreadPoolSize();
            int newThreadPoolSize = Integer.parseInt(content.content().toString(CharsetUtil.UTF_8));

            if (newThreadPoolSize - currentThreadPoolSize > 0) {
                customThreadPool.incrementPoolTo(newThreadPoolSize);
            } else if (newThreadPoolSize - currentThreadPoolSize < 0) {
                customThreadPool.decrementPoolSizeTo(newThreadPoolSize);
            }

            if (content instanceof LastHttpContent) {
                System.out.println("} END OF CONTENT");
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
