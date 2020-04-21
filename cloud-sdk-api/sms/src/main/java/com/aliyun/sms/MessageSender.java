package com.aliyun.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author
 */
public class MessageSender {
    private static final String OK = "OK";
    private static Config config;
    private  MessageSender sender;
    private String templateCode;
    private String signName;

    public  MessageSender(String appcode, String accessKeyId, String accessKeySecret) {
        config = new Config(appcode, accessKeyId, accessKeySecret);
        sender = new MessageSender();
    }

    public MessageSender() {
    }

    public static MessageSender newMessageSender(String appcode, String accessKeyId, String accessKeySecret){
        return new MessageSender(appcode,accessKeyId,accessKeySecret);
    }

    public MessageSender signName(String signName, String templateCode) {
        this.signName = signName;
        this.templateCode = templateCode;
        return sender;
    }

    public boolean sendMessage(String phoneNumber, String verificationCode) throws MessageSendException {
        IAcsClient acsClient = getIAcsClient(config);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        JSONObject templateParam = new JSONObject();
        templateParam.put("code", verificationCode);
        request.setTemplateParam(templateParam.toJSONString());
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ServerException e) {
            throw new MessageSendException(e);
        } catch (ClientException e) {
            throw new MessageSendException(e);
        }
        if (sendSmsResponse.getCode() != null && OK.equals(sendSmsResponse.getCode())) {
            return true;
        } else {
            throw new MessageSendException(sendSmsResponse.getMessage());
        }
    }

    private IAcsClient getIAcsClient(Config config) throws MessageSendException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "1000");
        System.setProperty("sun.net.client.defaultReadTimeout", "1000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            throw new MessageSendException(e);
        }
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(),
                config.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }


}
