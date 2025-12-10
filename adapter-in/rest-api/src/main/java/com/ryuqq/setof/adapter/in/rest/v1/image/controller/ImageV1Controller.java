package com.ryuqq.setof.adapter.in.rest.v1.image.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.command.CreatePreSignedUrlV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.response.CreatePreSignedUrlV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image (Legacy V1)", description = "레거시 Image API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class ImageV1Controller {

    @Deprecated
    @Operation(
            summary = "[Legacy] Image Presigned Url 발급",
            description = "Image upload를 위한 presigned url 발급")
    @PostMapping(ApiPaths.Image.PRESIGNED)
    public ResponseEntity<ApiResponse<PageApiResponse<CreatePreSignedUrlV1ApiResponse>>>
            getPresignedUrl(@RequestBody @Validated CreatePreSignedUrlV1ApiRequest request) {
        throw new UnsupportedOperationException("Image Presigned Url 발급 기능은 아직 지원되지 않습니다.");
    }
}
