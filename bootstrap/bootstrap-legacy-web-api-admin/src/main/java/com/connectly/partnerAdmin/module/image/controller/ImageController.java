package com.connectly.partnerAdmin.module.image.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlRequest;
import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlResponse;
import com.connectly.partnerAdmin.module.image.dto.UploadCompleteRequest;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

import lombok.RequiredArgsConstructor;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    /**
     * Presigned URL 발급.
     *
     * <p>클라이언트가 직접 S3에 업로드할 수 있는 Presigned URL을 발급합니다.
     */
    @PostMapping("/image/presigned")
    public ResponseEntity<ApiResponse<PreSignedUrlResponse>> getContent(
            @RequestBody @Validated PreSignedUrlRequest preSignedUrlRequest) {
        return ResponseEntity.ok(
                ApiResponse.success(imageUploadService.getPreSignedUrl(preSignedUrlRequest)));
    }

    /**
     * 업로드 완료 처리.
     *
     * <p>클라이언트가 Presigned URL로 S3에 업로드를 완료한 후 호출합니다.
     */
    @PostMapping("/image/complete")
    public ResponseEntity<ApiResponse<Void>> completeUpload(
            @RequestBody @Validated UploadCompleteRequest request) {
        imageUploadService.completeUpload(request.getSessionId(), request.getEtag());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
