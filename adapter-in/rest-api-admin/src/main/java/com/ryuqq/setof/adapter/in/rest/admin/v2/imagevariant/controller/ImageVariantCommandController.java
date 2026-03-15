package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.ImageVariantAdminEndpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command.SyncImageVariantsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.mapper.ImageVariantCommandApiMapper;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.port.in.command.SyncImageVariantsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ImageVariantCommandController - 이미지 Variant 동기화 API.
 *
 * <p>OMS에서 이미지 변환 완료 후 variant 데이터를 동기화하는 엔드포인트입니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-009: @Valid 어노테이션 필수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "이미지 Variant 관리", description = "이미지 Variant 동기화 API")
@RestController
@RequestMapping(ImageVariantAdminEndpoints.BASE)
public class ImageVariantCommandController {

    private final SyncImageVariantsUseCase syncUseCase;
    private final ImageVariantCommandApiMapper mapper;

    public ImageVariantCommandController(
            SyncImageVariantsUseCase syncUseCase, ImageVariantCommandApiMapper mapper) {
        this.syncUseCase = syncUseCase;
        this.mapper = mapper;
    }

    /**
     * 이미지 Variant 동기화 API.
     *
     * <p>OMS에서 이미지 변환 완료 후 variant 데이터를 동기화합니다. 기존 variant와 diff 비교하여 추가/삭제/유지를 판단합니다.
     *
     * @param request 동기화 요청 DTO
     * @return 빈 응답
     */
    @Operation(
            summary = "이미지 Variant 동기화",
            description = "원본 이미지의 Variant를 동기화합니다. 기존 variant와 비교하여 변경분만 처리합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "동기화 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PutMapping(ImageVariantAdminEndpoints.SYNC)
    public ResponseEntity<ApiResponse<Void>> sync(
            @Valid @RequestBody SyncImageVariantsApiRequest request) {

        SyncImageVariantsCommand command = mapper.toSyncCommand(request);
        syncUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.of());
    }
}
