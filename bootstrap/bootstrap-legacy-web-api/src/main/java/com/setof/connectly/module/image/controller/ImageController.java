package com.setof.connectly.module.image.controller;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.service.ImageUploadService;
import com.setof.connectly.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/image/presigned")
    public ResponseEntity<ApiResponse<PreSignedUrlResponse>> getContent(
            @RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        return ResponseEntity.ok(
                ApiResponse.success(imageUploadService.getPreSignedUrl(preSignedUrlRequest)));
    }
}
