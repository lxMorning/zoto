//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.shiro.util;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;

public interface RequestPairSource {
    RequestPacket getHawkRequest();

    ResponsePacket getHawkResponse();
}
