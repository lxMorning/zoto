//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparkframework.util;

import com.sparkframework.lang.Convert;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertyFile {
    private Properties properties;
    private InputStream file;
    private String configFilePath;

    public PropertyFile() {
        this("config.properties");
    }

    public PropertyFile(String propertyFileName) {
        try {
            try {
                this.configFilePath = URLDecoder.decode(this.getClass().getResource("/").getPath() + propertyFileName, "UTF-8");
                this.properties = new Properties();
                this.file = new FileInputStream(this.configFilePath);
                this.properties.load(this.file);
                return;
            } catch (IOException var14) {
            }

            try {
                this.file.close();
            } catch (IOException localIOException3) {
                localIOException3.printStackTrace();
                return;
            }
        } finally {
            try {
                this.file.close();
            } catch (IOException localIOException4) {
                localIOException4.printStackTrace();
            }

        }

    }

    public PropertyFile(String propertyFilePath, String propertyFileName) {
        try {
            try {
                this.configFilePath = URLDecoder.decode(propertyFilePath + propertyFileName, "UTF-8");
                this.properties = new Properties();
                this.file = new FileInputStream(this.configFilePath);
                this.properties.load(this.file);
                return;
            } catch (IOException var15) {
            }

            try {
                this.file.close();
            } catch (IOException localIOException2) {
                localIOException2.printStackTrace();
                return;
            }
        } finally {
            try {
                this.file.close();
            } catch (IOException localIOException3) {
                localIOException3.printStackTrace();
            }

        }

    }

    public String read(String key) {
        return this.properties.getProperty(key);
    }

    public int readInt(String key, int value) {
        return Convert.strToInt(this.properties.getProperty(key), value);
    }

    public String readString(String key, String value) {
        return Convert.strToStr(this.properties.getProperty(key), value);
    }

    public void write(String key, String value) {
        FileOutputStream localFileOutputStream = null;

        try {
            try {
                this.properties.setProperty(key, value);
                localFileOutputStream = new FileOutputStream(this.configFilePath);
                this.properties.store(localFileOutputStream, "");
                localFileOutputStream.flush();
                return;
            } catch (Exception var16) {
            }

            try {
                localFileOutputStream.close();
            } catch (IOException localIOException1) {
                localIOException1.printStackTrace();
                return;
            }
        } finally {
            try {
                localFileOutputStream.close();
            } catch (IOException localIOException2) {
                localIOException2.printStackTrace();
            }

        }

    }
}
