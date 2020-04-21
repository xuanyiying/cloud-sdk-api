package com.aliyun.invoice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.invoice.util.HttpUtil;
import com.aliyun.invoice.util.ImageBase64Util;
import com.aliyun.invoice.util.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuanyiying
 * Created on 2020/4/15  14:16
 */
public class OcrInvoiceDistinguish {
    private static final String METHOD_POST = "POST";
    private static String appcode;
    private static final int CODE_200 = 200;

    public static OcrInvoiceDistinguish newInstance(String appcode) {
        OcrInvoiceDistinguish.appcode = appcode;
        return new OcrInvoiceDistinguish();
    }

    public String distinguishOcrInvoice(InputStream in)
            throws DistinguishException {
        String host = "https://ocrapi-invoice.taobao.com";
        String path = "/ocrservice/invoice";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        String imgBase64 = ImageBase64Util.encode(in);
        String body = "{\"img\": \"" + imgBase64 + "\"}";
        return this.doPost(host, path, headers, querys, body);
    }

    public String distinguishOcrInvoice(File file) throws DistinguishException {
        if (null == file || !file.exists()) {
            throw new DistinguishException("The file  does not  exist!");
        }
        InputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new DistinguishException(e);
        }
        return this.distinguishOcrInvoice(in);
    }

    public String distinguishOcrInvoice(String path) throws DistinguishException {
        if (StringUtil.isEmpty(path)) {
            throw new DistinguishException("The path  is invalid!");
        }
        return this.distinguishOcrInvoice(new File(path));
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
