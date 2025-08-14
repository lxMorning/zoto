

package com.cdeer.apns.http2.core.error;

import com.alibaba.fastjson.JSON;
import com.cdeer.apns.http2.core.model.PushNotification;

public class ErrorModel {
    private int code;
    private String appName;
    private PushNotification notification;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PushNotification getNotification() {
        return this.notification;
    }

    public void setNotification(PushNotification notification) {
        this.notification = notification;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
