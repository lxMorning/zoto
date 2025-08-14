//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.netty.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Flags;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttp2FrameLogger extends Http2FrameLogger {
    private static final Logger log = LoggerFactory.getLogger(SimpleHttp2FrameLogger.class);
    private String name;

    SimpleHttp2FrameLogger(String name) {
        super(LogLevel.DEBUG, name);
        this.name = name;
    }

    public void logData(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream) {
        this.log(direction, "DATA: streamId=%d, endStream=%b", streamId, endStream);
    }

    public void logHeaders(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) {
        this.log(direction, "HEADERS: streamId=%d, endStream=%b", streamId, endStream);
    }

    public void logHeaders(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) {
        this.log(direction, "HEADERS: streamId=%d endStream=%b", streamId, endStream);
    }

    public void logPriority(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) {
        this.log(direction, "PRIORITY: streamId=%d, streamDependency=%d, weight=%d, exclusive=%b", streamId, streamDependency, weight, exclusive);
    }

    public void logRstStream(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, long errorCode) {
        this.log(direction, "RST_STREAM: streamId=%d, errorCode=%d", streamId, errorCode);
    }

    public void logSettingsAck(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx) {
        this.log(direction, "SETTINGS: ack=true");
    }

    public void logSettings(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, Http2Settings settings) {
        this.log(direction, "SETTINGS: ack=false, settings=%s", settings);
    }

    public void logPing(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, ByteBuf data) {
        this.log(direction, "PING: ack=false");
    }

    public void logPingAck(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, ByteBuf data) {
        this.log(direction, "PING: ack=true");
    }

    public void logPushPromise(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) {
        this.log(direction, "PUSH_PROMISE: streamId=%d, promisedStreamId=%d, headers=%s, padding=%d", streamId, promisedStreamId, headers, padding);
    }

    public void logGoAway(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) {
        this.log(direction, "GO_AWAY: lastStreamId=%d, errorCode=%d", lastStreamId, errorCode);
    }

    public void logWindowsUpdate(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) {
        this.log(direction, "WINDOW_UPDATE: streamId=%d, windowSizeIncrement=%d", streamId, windowSizeIncrement);
    }

    public void logUnknownFrame(Http2FrameLogger.Direction direction, ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf data) {
        this.log(direction, "UNKNOWN: frameType=%d, streamId=%d, flags=%d", frameType & 255, streamId, flags.value());
    }

    private void log(Http2FrameLogger.Direction direction, String format, Object... args) {
        log.debug(this.name + " " + direction.name() + " " + String.format(format, args));
    }
}
