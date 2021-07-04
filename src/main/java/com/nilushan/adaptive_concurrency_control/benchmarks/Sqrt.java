package com.nilushan.adaptive_concurrency_control.benchmarks;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.AdaptiveConcurrencyControl;
import com.nilushan.adaptive_concurrency_control.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.ThreadLocalRandom;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Test to measure performance of Square root calculation
 */
public class Sqrt implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;
    private final Timer.Context throughputContext;

    public Sqrt(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerCtx, Timer.Context throughputContext) {
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
            String resultString;
            double randomNumber = ThreadLocalRandom.current().nextDouble(6000000000.0000000, 6900000000.0000000 + 1); // Generate
            // random number between 6000000000.0000000 to 6900000000.0000000
            double sqrt = Math.sqrt(randomNumber);
            resultString = sqrt + "\n";
            buf = Unpooled.copiedBuffer(resultString.getBytes());
        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in Sqrt run method", e);
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