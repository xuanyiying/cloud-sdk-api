package com.aliyun.sms;

/**
 * @author
 */
public class MessageSendException extends Exception {
    private static final long serialVersionUID = 1L;

    public MessageSendException(Exception e) {
        super(e);
    }

    public MessageSendException(String message) {
        super(message);
    }

}
