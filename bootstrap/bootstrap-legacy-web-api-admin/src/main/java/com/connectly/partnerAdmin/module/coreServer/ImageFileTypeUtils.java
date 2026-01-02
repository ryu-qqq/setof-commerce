package com.connectly.partnerAdmin.module.coreServer;

import java.util.HashMap;
import java.util.Map;

public class ImageFileTypeUtils {

    private static final Map<String, String> IMAGE_CONTENT_TYPE_MAP = new HashMap<>();

    static {
        IMAGE_CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        IMAGE_CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        IMAGE_CONTENT_TYPE_MAP.put("png", "image/png");
        IMAGE_CONTENT_TYPE_MAP.put("gif", "image/gif");
        IMAGE_CONTENT_TYPE_MAP.put("webp", "image/webp");
        IMAGE_CONTENT_TYPE_MAP.put("avif", "image/avif");
        IMAGE_CONTENT_TYPE_MAP.put("svg", "image/svg+xml");
        IMAGE_CONTENT_TYPE_MAP.put("bmp", "image/bmp");
    }

    public static String getImageContentType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "application/octet-stream";  // 기본 Content-Type
        }

        String extension = getFileExtension(fileName);
        return IMAGE_CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream");
    }


    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }

        String lowerCase = fileName.substring(fileName.lastIndexOf(".")
            + 1).toLowerCase();
        if(lowerCase.equals("webp")) return "jpg";
        return lowerCase;
    }

}
