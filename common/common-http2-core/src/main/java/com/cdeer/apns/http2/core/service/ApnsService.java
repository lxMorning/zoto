//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.service;

import com.cdeer.apns.http2.core.model.PushNotification;

public interface ApnsService {
    void sendNotification(PushNotification var1);

    boolean sendNotificationSynch(PushNotification var1);

    void shutdown();
}
