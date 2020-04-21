package com.aliyun.invoice;

/**
 * @author Created on 2020/4/15  11:45
 */
public class DistinguishException extends Throwable {
    public DistinguishException(Exception e) {
        super(e);
    }

    public DistinguishException(String message) {
        super(message);
    }
}
