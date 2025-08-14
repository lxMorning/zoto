//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aqmd.netty.codec;

import io.netty.channel.Channel;

public interface Codec {
    byte[] decrypt(Channel channel, byte[] body);

    byte[] encrypt(Channel channel, byte[] body);
}
