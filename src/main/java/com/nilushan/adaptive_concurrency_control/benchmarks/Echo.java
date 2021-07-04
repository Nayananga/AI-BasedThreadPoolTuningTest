package com.nilushan.adaptive_concurrency_control.benchmarks;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.AdaptiveConcurrencyControl;
import com.nilushan.adaptive_concurrency_control.NettyClient;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class Echo implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;
    private final Timer.Context throughputContext;

    public Echo(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerContext, Timer.Context throughputContext) {
        this.msg = msg;
        this.ctx = ctx;
        this.timerContext = timerContext;
        this.throughputContext = throughputContext;
    }

    @Override
    public void run() {
        NettyClient.IN_PROGRESS_COUNT++;
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, msg.content());
            String contentType = msg.headers().get(HttpHeaderNames.CONTENT_TYPE);
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
}
