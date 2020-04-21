package com.aliyun.sms;


/**
 * @author xuanyiying
 * Created on 2020/4/15  10:08
 */
public class Config {
    private String appcode;
    private String accessKeyId;
    private String accessKeySecret;

    public Config(String appcode, String accessKeyId, String accessKeySecret) {
        this.appcode = appcode;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
