//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.filter;

import com.aqmd.netty.annotation.HawkFilter;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;

public abstract class HFilter {
    public abstract void init() throws NettyException;

    public abstract void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws IOException, NettyException;

    public abstract void destroy();

    protected String buildExceptionMsg(int code, String message) {
        return code + "~" + message;
    }

    public boolean isMatch(RequestPacket req) {
        HawkFilter hawkFilter = (HawkFilter)this.getClass().getAnnotation(HawkFilter.class);

        for(int cmd : hawkFilter.ignoreCmds()) {
            if (cmd == req.getCmd()) {
                return false;
            }
        }

        for(int cmd : hawkFilter.cmds()) {
            if (cmd == req.getCmd()) {
                return true;
            }
        }

        return true;
    }
}
