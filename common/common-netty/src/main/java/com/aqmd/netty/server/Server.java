//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.server;

public interface Server {
    void open();

    void close();

    boolean isClosed();

    boolean isAvailable();
}
