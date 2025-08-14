//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.service;

import com.cdeer.apns.http2.core.model.ApnsConfig;
import com.cdeer.apns.http2.core.model.NamedThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractApnsService implements ApnsService {
    protected static final Logger log = LoggerFactory.getLogger(AbstractApnsService.class);
    private static final int EXPIRE = 900000;
    private static final AtomicInteger IDS = new AtomicInteger(0);
    protected ExecutorService executorService;

    public AbstractApnsService(ApnsConfig config) {
        this.executorService = Executors.newFixedThreadPool(config.getPoolSize(), new NamedThreadFactory(config.getName()));
    }

    public void shutdown() {
        this.executorService.shutdownNow();

        try {
            this.executorService.awaitTermination(6L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("shutdown", e);
        }

    }
}
