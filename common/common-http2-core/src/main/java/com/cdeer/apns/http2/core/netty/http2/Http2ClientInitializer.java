//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.netty.http2;

import com.cdeer.apns.http2.core.model.PingMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.DelegatingDecompressorFrameListener;
import io.netty.handler.codec.http2.Http2ClientUpgradeCodec;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.HttpToHttp2ConnectionHandler;
import io.netty.handler.codec.http2.HttpToHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapterBuilder;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class Http2ClientInitializer extends ChannelInitializer<SocketChannel> {
    private static final int IDLE_TIME_SECONDS = 30;
    private final Http2FrameLogger logger;
    private final SslContext sslCtx;
    private final int maxContentLength;
    private HttpToHttp2ConnectionHandler connectionHandler;
    private HttpResponseHandler responseHandler;
    private Http2SettingsHandler settingsHandler;
    private Http2PingHandler pingHandler;
    private String name;

    public Http2ClientInitializer(String name, SslContext sslCtx, int maxContentLength) {
        this.sslCtx = sslCtx;
        this.maxContentLength = maxContentLength;
        this.name = name;
        this.logger = new SimpleHttp2FrameLogger(name);
    }

    public void initChannel(SocketChannel ch) throws Exception {
        Http2Connection connection = new DefaultHttp2Connection(false);
        this.connectionHandler = (new HttpToHttp2ConnectionHandlerBuilder()).frameListener(new DelegatingDecompressorFrameListener(connection, (new InboundHttp2ToHttpAdapterBuilder(connection)).maxContentLength(this.maxContentLength).propagateSettings(true).build())).frameLogger(this.logger).connection(connection).build();
        this.pingHandler = new Http2PingHandler(this.connectionHandler.encoder());
        this.responseHandler = new HttpResponseHandler(this.name);
        this.settingsHandler = new Http2SettingsHandler(ch.newPromise());
        if (this.sslCtx != null) {
            this.configureSsl(ch);
        } else {
            this.configureClearText(ch);
        }

    }

    public HttpResponseHandler responseHandler() {
        return this.responseHandler;
    }

    public Http2SettingsHandler settingsHandler() {
        return this.settingsHandler;
    }

    private void configureEndOfPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new ChannelHandler[]{this.settingsHandler, this.responseHandler, this.pingHandler});
    }

    private void configureSsl(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ChannelHandler[]{this.sslCtx.newHandler(ch.alloc())});
        pipeline.addLast(new ChannelHandler[]{new ApplicationProtocolNegotiationHandler("") {
            protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
                ChannelPipeline p = ctx.pipeline();
                p.addLast(new ChannelHandler[]{Http2ClientInitializer.this.connectionHandler});
                Http2ClientInitializer.this.configureEndOfPipeline(p);
            }
        }});
        pipeline.addLast(new ChannelHandler[]{new IdleStateHandler(Integer.MAX_VALUE, 30, 30)}).addLast(new ChannelHandler[]{new ChannelInboundHandlerAdapter() {
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                super.userEventTriggered(ctx, evt);
                if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
                    IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
                    if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                        ctx.channel().write(PingMessage.INSTANCE);
                    }
                }

            }
        }});
    }

    private void configureClearText(SocketChannel ch) {
        HttpClientCodec sourceCodec = new HttpClientCodec();
        Http2ClientUpgradeCodec upgradeCodec = new Http2ClientUpgradeCodec(this.connectionHandler);
        HttpClientUpgradeHandler upgradeHandler = new HttpClientUpgradeHandler(sourceCodec, upgradeCodec, 65536);
        ch.pipeline().addLast(new ChannelHandler[]{sourceCodec, upgradeHandler, new UpgradeRequestHandler(), new UserEventLogger()});
    }

    private static class UserEventLogger extends ChannelInboundHandlerAdapter {
        private UserEventLogger() {
        }

        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            ctx.fireUserEventTriggered(evt);
        }
    }

    private final class UpgradeRequestHandler extends ChannelInboundHandlerAdapter {
        private UpgradeRequestHandler() {
        }

        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            DefaultFullHttpRequest upgradeRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            ctx.writeAndFlush(upgradeRequest);
            ctx.fireChannelActive();
            ctx.pipeline().remove(this);
            Http2ClientInitializer.this.configureEndOfPipeline(ctx.pipeline());
        }
    }
}
