package com.aliyun.distinguish;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.distinguish.util.HttpUtil;
import com.aliyun.distinguish.util.ImageBase64Util;
import com.aliyun.distinguish.util.StringUtil;
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
public class BusinessLicenseDistinguish {
    private static final String METHOD_POST = "POST";
    private static String appcode;
    private static final int CODE_200 = 200;

    public BusinessLicenseDistinguish() {
    }


    public static BusinessLicenseDistinguish newInstance(String appcode) {
        BusinessLicenseDistinguish.appcode = appcode;
        return new BusinessLicenseDistinguish();
    }

    public String distinguishBusinessLicense(InputStream in) throws DistinguishException {
        if (StringUtil.isEmpty(appcode)) {
            throw new DistinguishException("The appcode is  required!");
        }
        if (null == in) {
            throw new DistinguishException("The inputStream is  null!");
        }
        String host = "http://dm-58.data.aliyun.com";
        final String path = "/rest/160601/ocr/ocr_business_license.json";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> query = new HashMap<>();
        String body = "{\"image\":\"" + ImageBase64Util.encode(in) + "\"}";
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

    public String distinguishBusinessLicense(File file) throws DistinguishException {
        if (null == file || !file.exists()) {
            throw new DistinguishException("The file  does not  exist!");
        }
        try {
            return this.distinguishBusinessLicense(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new DistinguishException(e);
        }
    }

    public BusinessLicenseDistinguish(String appcode) {
        BusinessLicenseDistinguish.appcode = appcode;
    }

    public String distinguishBusinessLicense(String path) throws DistinguishException {
        if (StringUtil.isEmpty(path)) {
            throw new DistinguishException("The path  is invalid!");
        }
        return this.distinguishBusinessLicense(new File(path));
    }


}
