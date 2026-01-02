package com.connectly.partnerAdmin.module.generic.file;

public class FileType {
    public static String getFileType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpeg", "jpg", "webp", "bmp" -> "jpg";
            case "png" -> "png";
            case "gif" -> "gif";
            default -> extension;
        };
    }

    public static String getContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpeg", "jpg" -> "image/jpeg";
            case "webp" -> "image/webp";
            case "bmp" -> "image/bmp";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }
}
