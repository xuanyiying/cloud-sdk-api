package com.aliyun.idcard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.idcard.util.HttpUtil;
import com.aliyun.idcard.util.ImageBase64Util;
import com.aliyun.idcard.util.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created on 2020/4/15  11:43
 */
public class IdCardDistinguish {
    private static final String METHOD_POST = "POST";
    private static String appcode;
    private static final int CODE_200 = 200;

    public static IdCardDistinguish newInstance(String appcode) {
        IdCardDistinguish.appcode = appcode;
        return new IdCardDistinguish();
    }

    public String distinguishIDCard(String path, boolean front)
            throws DistinguishException {
        if (StringUtil.isEmpty(path)) {
            throw new DistinguishException("The path is invalid!");
        }
        try {
            return this.distinguishIDCard(new File(path), front);
        } catch (Exception e) {
            throw new DistinguishException(e);
        }
    }

    public String distinguishIDCard(File file, boolean front)
            throws DistinguishException {
        if (null == file || !file.exists()) {
            throw new DistinguishException("The file  does not  exist!");
        }
        try {
            return this.distinguishIDCard(new FileInputStream(file), front);
        } catch (FileNotFoundException e) {
            throw new DistinguishException(e);
        }
    }

    public String distinguishIDCard(InputStream in, boolean front)
            throws DistinguishException {
        final String host = "http://dm-51.data.aliyun.com";
        final String path = "/rest/160601/ocr/ocr_idcard.json";
        JSONObject configObj = new JSONObject();
        if (front) {
            configObj.put("side", "face");
        } else {
            configObj.put("side", "back");
        }
        String config_str = configObj.toString();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        String imgBase64 = ImageBase64Util.encode(in);
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("image", imgBase64);
            if (config_str.length() > 0) {
                requestObj.put("configure", config_str);
            }
        } catch (JSONException e) {
            throw new DistinguishException(e);
        }
        String body = requestObj.toString();
        return this.doPost(host, path, headers, query, body);
    }

    private String doPost(String host, String path, Map<String, String> headers, Map<String, String> query, String body) throws DistinguishException {
        HttpResponse response;
        try {
            response = HttpUtil.doPost(host, path, METHOD_POST, headers, query, body);
            int status = response.getStatusLine().getStatusCode();
            if (status != CODE_200) {
                throw new DistinguishException("Distinguish file met error,error info :Http code: " + status + "; http header error msg: " + response.getFirstHeader("X-Ca-Error-Message") + "; http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
            }
            String res = EntityUtils.toString(response.getEntity());
            JSONObject responseJson = JSON.parseObject(res);
            return responseJson.toJSONString();
        } catch (Exception e) {
            throw new DistinguishException(e);
        }
    }

}
