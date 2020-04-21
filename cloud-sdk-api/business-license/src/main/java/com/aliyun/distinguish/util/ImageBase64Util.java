package com.aliyun.distinguish.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author
 */
public class ImageBase64Util {
    public static String encode(File image) {
        if (null == image || !image.exists()) {
            return null;
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(image);
        } catch (FileNotFoundException e) {
            System.err.println("Encode image string to image met error:" + e.getMessage());
        }
        return encode(in);
    }

    public static String encode(InputStream in) {
        if (null == in) {
            return null;
        }
        String base64Image = "";
        try {
            // Reading a Image file from file system
            byte[] imageData = new byte[in.available()];
            in.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (Exception e) {
            System.err.println("Encode image string to image met error:" + e.getMessage());
        }
        return base64Image;
    }

}


