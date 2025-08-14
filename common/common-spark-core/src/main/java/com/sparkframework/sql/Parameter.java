//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparkframework.sql;

public class Parameter {
    public int type;
    public ParameterDirection direction;
    public Object value;

    public Parameter(int type, ParameterDirection direction, Object value) {
        this.type = type;
        this.direction = direction;
        this.value = value;
    }
}
