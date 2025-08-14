//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.service;

public interface ChannelEventDealService {
    void dealChannelActive(String serverIp, String clientIp, int clientPort);

    void dealChannelDestory(String serverIp, String clientIp, int clientPort);
}
