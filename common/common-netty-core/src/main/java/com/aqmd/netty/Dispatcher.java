//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty;

import com.aqmd.netty.entity.Packet;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;

public interface Dispatcher<R extends Packet, P extends Packet> {
    P dispatch(R request, ChannelHandlerContext ctx) throws NettyException;
}
