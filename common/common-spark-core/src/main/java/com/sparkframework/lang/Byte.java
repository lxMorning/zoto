//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparkframework.lang;

public class Byte {
    public static boolean Compare(byte[] input1, byte[] input2) {
        if (input1 != null && input2 != null) {
            if (input1.length != input2.length) {
                return false;
            } else {
                for(int i = 0; i < input1.length; ++i) {
                    if (input1[i] != input2[i]) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static String byte2Hex(byte[] buffer) {
        String str1 = "";

        for(int i = 0; i < buffer.length; ++i) {
            String str2;
            if ((str2 = Integer.toHexString(buffer[i] & 255)).length() == 1) {
                str1 = str1 + "0" + str2;
            } else {
                str1 = str1 + str2;
            }
        }

        return str1.toUpperCase();
    }

    public static byte[] hex2Byte(byte[] buffer) {
        if (buffer.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] arrayOfByte = new byte[buffer.length / 2];

            for(int i = 0; i < buffer.length; i += 2) {
                String str = new String(buffer, i, 2);
                arrayOfByte[i / 2] = (byte)Integer.parseInt(str, 16);
            }

            return arrayOfByte;
        }
    }
}
