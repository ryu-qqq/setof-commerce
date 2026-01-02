package com.ryuqq.setof.application.image.port.out.client;

import com.ryuqq.setof.application.image.dto.ImageUploadResult;
import com.ryuqq.setof.application.image.dto.PreSignedUrlResult;
import java.util.List;

/**
 * Image Upload Port (External Storage API)
 *
 * <p>외부 이미지 스토리지(S3, GCS, Azure Blob 등)와 연동하는 Port
 *
 * <p>구현체는 Adapter Out 모듈에서 실제 외부 스토리지 API 연동
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ImageUploadPort {

    /**
     * Presigned URL 발급
     *
     * <p>클라이언트가 직접 스토리지에 업로드할 수 있는 임시 URL 발급
     *
     * @param fileName 파일명
     * @param contentType 파일 MIME 타입 (image/jpeg, image/png 등)
     * @param directory 저장 경로 (banner, product, content 등)
     * @return Presigned URL 정보
     */
    PreSignedUrlResult generatePresignedUrl(String fileName, String contentType, String directory);

    /**
     * 다중 Presigned URL 발급
     *
     * @param fileNames 파일명 목록
     * @param contentType 파일 MIME 타입
     * @param directory 저장 경로
     * @return Presigned URL 목록
     */
    List<PreSignedUrlResult> generatePresignedUrls(
            List<String> fileNames, String contentType, String directory);

    /**
     * 이미지 삭제
     *
     * @param imageUrl 삭제할 이미지 URL
     * @return 삭제 성공 여부
     */
    boolean deleteImage(String imageUrl);

    /**
     * 다중 이미지 삭제
     *
     * @param imageUrls 삭제할 이미지 URL 목록
     * @return 삭제된 이미지 수
     */
    int deleteImages(List<String> imageUrls);

    /**
     * 이미지 존재 여부 확인
     *
     * @param imageUrl 확인할 이미지 URL
     * @return 존재하면 true
     */
    boolean exists(String imageUrl);

    /**
     * 이미지 직접 업로드 (서버 사이드)
     *
     * <p>서버에서 직접 이미지를 업로드해야 하는 경우 사용
     *
     * @param imageBytes 이미지 바이트 배열
     * @param fileName 파일명
     * @param contentType 파일 MIME 타입
     * @param directory 저장 경로
     * @return 업로드 결과
     */
    ImageUploadResult uploadImage(
            byte[] imageBytes, String fileName, String contentType, String directory);
}
