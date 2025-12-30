package com.ryuqq.setof.application.image.dto;

/**
 * 이미지 업로드 결과 DTO
 *
 * @param imageUrl 업로드된 이미지 접근 URL
 * @param fileName 저장된 파일명
 * @param fileSize 파일 크기 (bytes)
 * @param contentType 파일 MIME 타입
 * @author development-team
 * @since 2.0.0
 */
public record ImageUploadResult(
        String imageUrl, String fileName, long fileSize, String contentType) {

    /**
     * 정적 팩토리 메서드
     *
     * @param imageUrl 이미지 URL
     * @param fileName 파일명
     * @param fileSize 파일 크기
     * @param contentType 컨텐츠 타입
     * @return ImageUploadResult 인스턴스
     */
    public static ImageUploadResult of(
            String imageUrl, String fileName, long fileSize, String contentType) {
        return new ImageUploadResult(imageUrl, fileName, fileSize, contentType);
    }
}
