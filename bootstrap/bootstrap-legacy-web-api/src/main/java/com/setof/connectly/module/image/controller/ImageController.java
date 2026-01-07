package com.setof.connectly.module.image.controller;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.dto.UploadCompleteRequest;
import com.setof.connectly.module.image.service.ImageUploadService;
import com.setof.connectly.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    /**
     * Presigned URL 발급.
     *
     * <p>클라이언트가 직접 S3에 업로드할 수 있는 Presigned URL을 발급합니다.
     * 응답의 sessionId를 저장하여 업로드 완료 후 /image/complete 호출 시 사용합니다.
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
     * S3 업로드 응답에서 받은 ETag 헤더 값을 함께 전달해야 합니다.
     */
    @PostMapping("/image/complete")
    public ResponseEntity<ApiResponse<Void>> completeUpload(
            @RequestBody @Validated UploadCompleteRequest request) {

        imageUploadService.completeUpload(request.getSessionId(), request.getEtag());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
