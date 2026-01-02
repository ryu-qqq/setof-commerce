package com.connectly.partnerAdmin.module.image.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PreSignedUrlResponse {
    /**
     * Fileflow 세션 ID (업로드 완료 처리에 필요).
     */
    private String sessionId;

    /**
     * S3 Presigned URL.
     */
    private String preSignedUrl;

    /**
     * S3 객체 키.
     */
    private String objectKey;

    /**
     * 하위 호환성을 위한 생성자 (sessionId 없이).
     */
    public PreSignedUrlResponse(String preSignedUrl, String objectKey) {
        this(null, preSignedUrl, objectKey);
    }
}
