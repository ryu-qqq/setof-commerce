package com.connectly.partnerAdmin.module.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 업로드 완료 요청 DTO.
 *
 * <p>클라이언트가 Presigned URL로 S3에 업로드를 완료한 후 호출합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UploadCompleteRequest {

    /**
     * Fileflow 세션 ID.
     * Presigned URL 발급 시 응답에서 받은 sessionId.
     */
    @NotBlank(message = "sessionId는 필수입니다.")
    private String sessionId;

    /**
     * S3 업로드 후 반환된 ETag.
     * S3 PUT 응답의 ETag 헤더 값.
     */
    @NotBlank(message = "etag는 필수입니다.")
    private String etag;
}
