package com.nilushan.adaptive_concurrency_control.benchmarks;

import com.codahale.metrics.Timer;
import com.nilushan.adaptive_concurrency_control.AdaptiveConcurrencyControl;
import com.nilushan.adaptive_concurrency_control.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Test to measure performance of Database Write
 */
public class DbWrite implements Runnable {

    private final FullHttpRequest msg;
    private final ChannelHandlerContext ctx;
    private final Timer.Context timerContext;

    public DbWrite(ChannelHandlerContext ctx, FullHttpRequest msg, Timer.Context timerCtx) {
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
            Connection connection = null;
            PreparedStatement stmt = null;
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/echoserver?useSSL=false&autoReconnect=true&failOverReadOnly=false&maxReconnects=10",
                        "root", "root");
                Timestamp current = Timestamp.from(Instant.now()); // get current timestamp
                String sql = "INSERT INTO Timestamp (timestamp) VALUES (?)";
                stmt = connection.prepareStatement(sql);
                stmt.setTimestamp(1, current);
                stmt.executeUpdate();
                buf = Unpooled.copiedBuffer(current.toString().getBytes());
            } catch (Exception e) {
                AdaptiveConcurrencyControl.LOGGER.error("Exception", e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e) {
                        AdaptiveConcurrencyControl.LOGGER.error("Exception", e);
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        AdaptiveConcurrencyControl.LOGGER.error("Exception", e);
                    }
                }
            }
            NettyClient.IN_PROGRESS_COUNT--;
        } catch (Exception e) {
            AdaptiveConcurrencyControl.LOGGER.error("Exception in DbWrite Run method", e);
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