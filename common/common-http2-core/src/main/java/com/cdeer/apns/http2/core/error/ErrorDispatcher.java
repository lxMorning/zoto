//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cdeer.apns.http2.core.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorDispatcher {
    private List<ErrorListener> list;

    private ErrorDispatcher() {
        this.list = new ArrayList();
    }

    public static ErrorDispatcher getInstance() {
        return ErrorDispatcher.Nested.instance;
    }

    public void addListener(ErrorListener errorListener) {
        this.list.add(errorListener);
    }

    public void removeListener(ErrorListener errorListener) {
        this.list.remove(errorListener);
    }

    public void dispatch(ErrorModel errorModel) {
        for(ErrorListener listener : this.list) {
            listener.handle(errorModel);
        }

    }

    private static class Nested {
        private static ErrorDispatcher instance = new ErrorDispatcher();
    }
}
