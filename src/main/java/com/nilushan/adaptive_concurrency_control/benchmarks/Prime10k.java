package com.nilushan.adaptive_concurrency_control.benchmarks;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.AdaptiveConcurrencyControl;
import com.nilushan.adaptive_concurrency_control.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.Random;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Test to measure performance of Primality check
 */
public class Prime10k implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;
    private final Timer.Context throughputContext;

    public Prime10k(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerCtx, Timer.Context throughputContext) {
        this.msg = msg;
        this.ctx = ctx;
        this.timerContext = timerCtx;
        this.throughputContext = throughputContext;
    }

    @Override
    public void run() {
        NettyClient.IN_PROGRESS_COUNT++;
        ByteBuf buf = null;
        try {
            Random rand = new Random();
            int number = rand.nextInt((10021) - 10000) + 10000;  //Generate random integer between 10000 and 10020
            String resultString = "true";
            for (int i = 2; i < number; i++) {
                if (number % i == 0) {
                    resultString = "false";
                    break;
                }
            }
            buf = Unpooled.copiedBuffer(resultString.getBytes());
        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in Prime100k Run method", e);
        }

        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, buf);
        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in Netty Handler", e);
        }
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
        ctx.flush();
        NettyClient.IN_PROGRESS_COUNT--;
        throughputContext.stop();
        timerContext.stop(); // Stop Dropwizard metrics timer
    }
}