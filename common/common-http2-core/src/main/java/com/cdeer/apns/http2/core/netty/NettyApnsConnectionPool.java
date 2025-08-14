//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.netty;

import com.cdeer.apns.http2.core.model.ApnsConfig;
import java.security.KeyStore;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.net.ssl.KeyManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyApnsConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(NettyApnsConnectionPool.class);
    private static final String HOST_DEVELOPMENT = "api.development.push.apple.com";
    private static final String HOST_PRODUCTION = "api.push.apple.com";
    private static final String ALGORITHM = "sunx509";
    private static final String KEY_STORE_TYPE = "PKCS12";
    private static final int PORT = 2197;
    private volatile boolean isShutdown;
    public BlockingQueue<NettyApnsConnection> connectionQueue;

    public NettyApnsConnectionPool(ApnsConfig config) {
        int poolSize = config.getPoolSize();
        this.connectionQueue = new LinkedBlockingQueue(poolSize);
        String host = config.isDevEnv() ? "api.development.push.apple.com" : "api.push.apple.com";
        KeyManagerFactory keyManagerFactory = this.createKeyManagerFactory(config);

        for(int i = 0; i < poolSize; ++i) {
            NettyApnsConnection connection = new NettyApnsConnection(config.getName(), host, 2197, config.getRetries(), config.getTimeout(), config.getTopic(), keyManagerFactory);
            connection.setConnectionPool(this);
            this.connectionQueue.add(connection);
        }

    }

    private KeyManagerFactory createKeyManagerFactory(ApnsConfig config) {
        try {
            char[] password = config.getPassword().toCharArray();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(config.getKeyStore(), password);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("sunx509");
            keyManagerFactory.init(keyStore, password);
            return keyManagerFactory;
        } catch (Exception e) {
            log.error("createKeyManagerFactory", e);
            throw new IllegalStateException("create key manager factory failed");
        }
    }

    public NettyApnsConnection acquire() {
        try {
            return (NettyApnsConnection)this.connectionQueue.take();
        } catch (InterruptedException e) {
            log.error("acquire", e);
            return null;
        }
    }

    public void release(NettyApnsConnection connection) {
        if (connection != null) {
            this.connectionQueue.add(connection);
        }

    }

    public void shutdown() {
        this.isShutdown = true;
    }

    public boolean isShutdown() {
        return this.isShutdown;
    }
}
