package com.setof.connectly.module.utils;

import com.setof.connectly.module.image.enums.ImagePath;

public class FileUtils {

    public static String getContentType(String extension) {
        switch (extension.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream"; // 기본값
        }
    }

    public static String getContentType(ImagePath imagePath) {
        switch (imagePath) {
            case PRODUCT:
            case DESCRIPTION:
            case QNA:
            case CONTENT:
            case IMAGE_COMPONENT:
            case BANNER:
            case REVIEW:
                return "image/jpg";
            default:
                return "application/octet-stream"; // 기본값
        }
    }
}
