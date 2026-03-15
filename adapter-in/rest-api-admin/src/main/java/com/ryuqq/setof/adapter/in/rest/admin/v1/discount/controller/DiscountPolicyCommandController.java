package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountFromExcelV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyCommandApiMapper;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPoliciesFromExcelUseCase;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.command.ModifyDiscountTargetsUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyStatusUseCase;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyDetailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DiscountPolicyCommandController - 할인 정책 생성/수정/상태변경/대상 관리 API (v1 레거시 호환).
 *
 * <p>레거시 URL 엔드포인트를 그대로 유지하면서 새 Application UseCase를 호출합니다.
 *
 * <p>API-CTR-001: Controller는 @RestController로 정의.
 *
 * <p>API-CTR-004: ResponseEntity&lt;ApiResponse&lt;T&gt;&gt; 래핑 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-005: Controller에서 @Transactional 금지.
 *
 * <p>API-CTR-007: Controller에 비즈니스 로직 포함 금지.
 *
 * <p>API-CTR-009: @Valid 어노테이션 필수.
 *
 * <p>API-CTR-010: CQRS Controller 분리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "할인 정책 관리 (v1)", description = "레거시 호환 할인 정책 생성/수정/삭제 API")
@RestController
@RequestMapping("/api/v1")
public class DiscountPolicyCommandController {

    private final CreateDiscountPolicyUseCase createUseCase;
    private final UpdateDiscountPolicyUseCase updateUseCase;
    private final UpdateDiscountPolicyStatusUseCase updateStatusUseCase;
    private final ModifyDiscountTargetsUseCase modifyTargetsUseCase;
    private final CreateDiscountPoliciesFromExcelUseCase createFromExcelUseCase;
    private final GetDiscountPolicyDetailUseCase getDetailUseCase;
    private final DiscountPolicyCommandApiMapper mapper;

    /**
     * DiscountPolicyCommandController 생성자.
     *
     * @param createUseCase 할인 정책 생성 UseCase
     * @param updateUseCase 할인 정책 수정 UseCase
     * @param updateStatusUseCase 할인 정책 상태 일괄 변경 UseCase
     * @param modifyTargetsUseCase 할인 적용 대상 수정 UseCase
     * @param createFromExcelUseCase 엑셀 기반 할인 정책 일괄 생성 UseCase
     * @param getDetailUseCase 할인 정책 단건 조회 UseCase
     * @param mapper Command API 매퍼
     */
    public DiscountPolicyCommandController(
            CreateDiscountPolicyUseCase createUseCase,
            UpdateDiscountPolicyUseCase updateUseCase,
            UpdateDiscountPolicyStatusUseCase updateStatusUseCase,
            ModifyDiscountTargetsUseCase modifyTargetsUseCase,
            CreateDiscountPoliciesFromExcelUseCase createFromExcelUseCase,
            GetDiscountPolicyDetailUseCase getDetailUseCase,
            DiscountPolicyCommandApiMapper mapper) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.updateStatusUseCase = updateStatusUseCase;
        this.modifyTargetsUseCase = modifyTargetsUseCase;
        this.createFromExcelUseCase = createFromExcelUseCase;
        this.getDetailUseCase = getDetailUseCase;
        this.mapper = mapper;
    }

    /**
     * 할인 정책 생성 API.
     *
     * <p>신규 할인 정책을 생성하고 생성된 정책의 상세 정보를 반환합니다. 레거시와 동일하게 생성 후 단건 조회 결과를 응답으로 반환합니다.
     *
     * @param request 할인 정책 생성 요청 DTO
     * @return 생성된 할인 정책 상세 정보 (201 Created)
     */
    @Operation(summary = "할인 정책 생성", description = "신규 할인 정책을 생성합니다. 생성 후 상세 정보를 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping("/discount")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> create(
            @Valid @RequestBody CreateDiscountV1ApiRequest request) {

        CreateDiscountPolicyCommand command = mapper.toCommand(request);
        long createdId = createUseCase.execute(command);

        DiscountPolicyResult result = getDetailUseCase.execute(createdId);
        DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    /**
     * 할인 정책 수정 API.
     *
     * <p>기존 할인 정책을 수정하고 수정된 정책의 상세 정보를 반환합니다. 레거시와 동일하게 수정 후 단건 조회 결과를 응답으로 반환합니다.
     *
     * @param discountPolicyId 수정할 할인 정책 ID
     * @param request 할인 정책 수정 요청 DTO
     * @return 수정된 할인 정책 상세 정보 (200 OK)
     */
    @Operation(summary = "할인 정책 수정", description = "기존 할인 정책을 수정합니다. 수정 후 상세 정보를 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "할인 정책을 찾을 수 없음")
    })
    @PutMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> update(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId,
            @Valid @RequestBody UpdateDiscountV1ApiRequest request) {

        UpdateDiscountPolicyCommand command = mapper.toCommand(discountPolicyId, request);
        updateUseCase.execute(command);

        DiscountPolicyResult result = getDetailUseCase.execute(discountPolicyId);
        DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

        return ResponseEntity.ok(ApiResponse.of(response));
    }

    /**
     * 할인 정책 활성화 상태 일괄 변경 API.
     *
     * <p>복수의 할인 정책을 일괄로 활성화 또는 비활성화합니다. 레거시는 변경 후 목록을 반환했으나 간소화하여 빈 리스트를 반환합니다.
     *
     * @param request 상태 변경 대상 정책 ID 목록 및 변경 방향 요청 DTO
     * @return 빈 리스트 (200 OK)
     */
    @Operation(summary = "할인 정책 상태 일괄 변경", description = "복수의 할인 정책을 일괄로 활성화 또는 비활성화합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "상태 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PatchMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountPolicyV1ApiResponse>>> updateStatus(
            @Valid @RequestBody UpdateDiscountStatusV1ApiRequest request) {

        UpdateDiscountPolicyStatusCommand command = mapper.toCommand(request);
        updateStatusUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.of(List.of()));
    }

    /**
     * 엑셀 기반 할인 정책 일괄 생성 API.
     *
     * <p>엑셀에서 파싱된 할인 정책 목록을 일괄 생성하고 생성된 정책 상세 목록을 반환합니다. 각 정책 생성 후 단건 조회하여 응답 목록을 구성합니다.
     *
     * @param requests 엑셀 기반 할인 정책 생성 요청 DTO 목록
     * @return 생성된 할인 정책 상세 목록 (201 Created)
     */
    @Operation(summary = "엑셀 기반 할인 정책 일괄 생성", description = "엑셀에서 파싱된 할인 정책 목록을 일괄 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "일괄 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @PostMapping("/discounts/excel")
    public ResponseEntity<ApiResponse<List<DiscountPolicyV1ApiResponse>>> createFromExcel(
            @Valid @RequestBody List<CreateDiscountFromExcelV1ApiRequest> requests) {

        List<CreateDiscountPolicyCommand> commands = mapper.toCommands(requests);
        List<Long> createdIds = createFromExcelUseCase.execute(commands);

        List<DiscountPolicyV1ApiResponse> responses =
                createdIds.stream().map(getDetailUseCase::execute).map(mapper::toResponse).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(responses));
    }

    /**
     * 할인 대상 추가 API.
     *
     * <p>특정 할인 정책에 적용 대상을 추가합니다. 내부적으로 대상 교체(replace) 방식으로 처리됩니다. 레거시는 추가된 대상 엔티티 목록을 반환했으나 간소화하여 빈
     * 리스트를 반환합니다.
     *
     * @param discountPolicyId 할인 정책 ID
     * @param request 할인 대상 추가 요청 DTO
     * @return 빈 리스트 (200 OK)
     */
    @Operation(summary = "할인 대상 추가", description = "특정 할인 정책에 적용 대상을 추가합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "대상 추가 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "할인 정책을 찾을 수 없음")
    })
    @PostMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<Object>>> addTargets(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId,
            @Valid @RequestBody CreateDiscountTargetV1ApiRequest request) {

        ModifyDiscountTargetsCommand command = mapper.toCommand(discountPolicyId, request);
        modifyTargetsUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.of(List.of()));
    }

    /**
     * 할인 대상 교체 API.
     *
     * <p>특정 할인 정책의 적용 대상 목록을 전체 교체합니다. 기존 대상은 비활성화되고 새 대상이 등록됩니다. 레거시는 교체된 대상 엔티티 목록을 반환했으나 간소화하여 빈
     * 리스트를 반환합니다.
     *
     * @param discountPolicyId 할인 정책 ID
     * @param request 할인 대상 교체 요청 DTO
     * @return 빈 리스트 (200 OK)
     */
    @Operation(summary = "할인 대상 교체", description = "특정 할인 정책의 적용 대상 목록을 전체 교체합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "대상 교체 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "할인 정책을 찾을 수 없음")
    })
    @PutMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<Object>>> replaceTargets(
            @Parameter(description = "할인 정책 ID", required = true) @PathVariable
                    long discountPolicyId,
            @Valid @RequestBody CreateDiscountTargetV1ApiRequest request) {

        ModifyDiscountTargetsCommand command = mapper.toCommand(discountPolicyId, request);
        modifyTargetsUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.of(List.of()));
    }
}
