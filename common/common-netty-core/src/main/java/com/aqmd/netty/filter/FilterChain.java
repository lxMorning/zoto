//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.filter;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;

public interface FilterChain {
    void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx);
}
