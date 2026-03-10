package com.ryuqq.setof.adapter.in.rest.v1.image.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.image.ImageV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.request.PreSignedUrlV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.request.UploadCompleteV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.response.PreSignedUrlV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.image.mapper.ImageV1ApiMapper;
import com.ryuqq.setof.application.uploadsession.dto.command.CompleteUploadSessionCommand;
import com.ryuqq.setof.application.uploadsession.dto.command.GenerateUploadUrlCommand;
import com.ryuqq.setof.application.uploadsession.dto.query.PresignedUrlResult;
import com.ryuqq.setof.application.uploadsession.port.in.command.CompleteUploadSessionUseCase;
import com.ryuqq.setof.application.uploadsession.port.in.command.GenerateUploadUrlUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ImageCommandV1Controller - 이미지 업로드 V1 Public API.
 *
 * <p>레거시 ImageController와 동일한 엔드포인트를 제공합니다.
 *
 * <ul>
 *   <li>POST /api/v1/image/presigned - Presigned URL 발급
 *   <li>POST /api/v1/image/complete - 업로드 완료 처리
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "이미지 업로드 V1", description = "이미지 업로드 V1 Public API (인증 필요)")
@RestController
public class ImageCommandV1Controller {

    private final GenerateUploadUrlUseCase generateUploadUrlUseCase;
    private final CompleteUploadSessionUseCase completeUploadSessionUseCase;
    private final ImageV1ApiMapper mapper;

    public ImageCommandV1Controller(
            GenerateUploadUrlUseCase generateUploadUrlUseCase,
            CompleteUploadSessionUseCase completeUploadSessionUseCase,
            ImageV1ApiMapper mapper) {
        this.generateUploadUrlUseCase = generateUploadUrlUseCase;
        this.completeUploadSessionUseCase = completeUploadSessionUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Presigned URL 발급",
            description = "클라이언트가 직접 S3에 업로드할 수 있는 Presigned URL을 발급합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "발급 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(ImageV1Endpoints.IMAGE_PRESIGNED)
    public ResponseEntity<V1ApiResponse<PreSignedUrlV1ApiResponse>> getPresignedUrl(
            @Valid @RequestBody PreSignedUrlV1ApiRequest request) {

        GenerateUploadUrlCommand command = mapper.toGenerateCommand(request);
        PresignedUrlResult result = generateUploadUrlUseCase.execute(command);
        PreSignedUrlV1ApiResponse response = mapper.toApiResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(summary = "업로드 완료 처리", description = "클라이언트가 Presigned URL로 S3에 업로드를 완료한 후 호출합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "완료 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(ImageV1Endpoints.IMAGE_COMPLETE)
    public ResponseEntity<V1ApiResponse<Void>> completeUpload(
            @Valid @RequestBody UploadCompleteV1ApiRequest request) {

        CompleteUploadSessionCommand command = mapper.toCompleteCommand(request);
        completeUploadSessionUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }
}
