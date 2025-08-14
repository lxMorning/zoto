//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.service;

import com.cdeer.apns.http2.core.model.ApnsConfig;
import com.cdeer.apns.http2.core.model.PushNotification;
import com.cdeer.apns.http2.core.netty.NettyApnsConnection;
import com.cdeer.apns.http2.core.netty.NettyApnsConnectionPool;

public class NettyApnsService extends AbstractApnsService {
    private NettyApnsConnectionPool connectionPool;

    private NettyApnsService(ApnsConfig config) {
        super(config);
        this.connectionPool = new NettyApnsConnectionPool(config);
    }

    public static NettyApnsService create(ApnsConfig apnsConfig) {
        return new NettyApnsService(apnsConfig);
    }

    public void sendNotification(PushNotification notification) {
        this.executorService.execute(() -> {
            NettyApnsConnection connection = null;

            try {
                connection = this.connectionPool.acquire();
                if (connection != null) {
                    boolean result = connection.sendNotification(notification);
                    if (!result) {
                        log.debug("发送通知失败");
                    }
                }
            } catch (Exception e) {
                log.error("sendNotification", e);
            } finally {
                this.connectionPool.release(connection);
            }

        });
    }

    public boolean sendNotificationSynch(PushNotification notification) {
        NettyApnsConnection connection = null;

        boolean var4;
        try {
            connection = this.connectionPool.acquire();
            if (connection == null) {
                return false;
            }

            boolean result = connection.sendNotification(notification);
            var4 = result;
        } catch (Exception e) {
            log.error("sendNotification", e);
            return false;
        } finally {
            this.connectionPool.release(connection);
        }

        return var4;
    }

    public void shutdown() {
        this.connectionPool.shutdown();
        super.shutdown();
    }
}
