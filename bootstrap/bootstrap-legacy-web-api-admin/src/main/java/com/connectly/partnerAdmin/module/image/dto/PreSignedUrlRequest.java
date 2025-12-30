package com.connectly.partnerAdmin.module.image.dto;

import com.connectly.partnerAdmin.module.image.enums.ImagePath;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PreSignedUrlRequest {

    private String fileName;
    private ImagePath imagePath;

    /**
     * 파일 크기 (bytes).
     * 선택적 필드로, 미입력 시 기본값 10MB 적용.
     */
    private Long fileSize;

    /**
     * 파일 크기 반환. 미설정 시 기본값 10MB.
     */
    public long getFileSizeOrDefault() {
        return fileSize != null ? fileSize : 10 * 1024 * 1024L; // 10MB default
    }
}
