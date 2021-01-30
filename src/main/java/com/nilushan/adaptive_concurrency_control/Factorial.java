package com.nilushan.adaptive_concurrency_control;

import com.codahale.metrics.Timer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.ThreadLocalRandom;

import java.math.BigInteger;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Test to measure performance of Factorial calculation
 */
public class Factorial implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;

    public Factorial(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerCtx) {
        this.msg = msg;
        this.ctx = ctx;
        this.timerContext = timerCtx;
    }

    @Override
    public void run() {
        Timer.Context throughputTimerContext = NettyClient.THROUGHPUT_TIMER.time();
        ByteBuf buf = null;
        try {
            NettyClient.IN_PROGRESS_COUNT++;
            BigInteger result = BigInteger.ONE;
            String resultString;
            int randomNumber = ThreadLocalRandom.current().nextInt(2000, 2999 + 1); // Generate random number between
            // 2000
            // and 2999
            for (int i = randomNumber; i > 0; i--) {
                result = result.multiply(BigInteger.valueOf(i));
            }
            resultString = result.toString().substring(0, 3) + "\n"; // get a substring to prevent the effect of network
            // I/O
            // affecting factorial calculation performance
            buf = Unpooled.copiedBuffer(resultString.getBytes());
            NettyClient.IN_PROGRESS_COUNT--;
        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in Factorial Run method", e);
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
        throughputTimerContext.stop();
        timerContext.stop(); // Stop Dropwizard metrics timer

    }

}