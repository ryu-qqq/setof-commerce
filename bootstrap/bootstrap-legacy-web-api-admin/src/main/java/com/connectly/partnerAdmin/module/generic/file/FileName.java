package com.connectly.partnerAdmin.module.generic.file;

import lombok.Getter;

import java.text.Normalizer;


public class FileName {

    @Getter
    private final String originalFileName;
    private final String fileExtension;

    public FileName(String originalFileName) {
        this.originalFileName = originalFileName;
        this.fileExtension = extractExtension(originalFileName);
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex < 0) {
            return "jpg";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    public String getFileExtension() {
        return FileType.getFileType(fileExtension);
    }

    public String getContentType() {
        return FileType.getContentType(fileExtension);
    }

    public boolean containsKoreanCharacters() {
        String normalizedFileName = Normalizer.normalize(originalFileName, Normalizer.Form.NFC);
        return normalizedFileName.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }


    @Override
    public String toString() {
        return "FileName{" +
                "originalFileName='" + originalFileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }


}
