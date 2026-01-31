package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.CreateSellerSettlementAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SellerApprovalStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper.SellerAdminV1ApiMapper;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.sellerapplication.port.in.command.ApplySellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.port.in.command.ApproveSellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.port.in.command.RejectSellerApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * SellerCommandV1Controller - 셀러 생성/수정 V1 API.
 *
 * <p>레거시 호환을 위한 V1 셀러 생성 및 수정 엔드포인트를 제공합니다.
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
 * @since 1.0.0
 */
@Tag(name = "셀러 관리 V1", description = "셀러 생성/수정 V1 API (레거시 호환)")
@RestController
public class SellerCommandV1Controller {

    private static final String APPROVAL_STATUS_APPROVED = "APPROVED";
    private static final String APPROVAL_STATUS_REJECTED = "REJECTED";
    private static final String DEFAULT_PROCESSOR = "V1_ADMIN_API";

    private final ApplySellerApplicationUseCase applyUseCase;
    private final ApproveSellerApplicationUseCase approveUseCase;
    private final RejectSellerApplicationUseCase rejectUseCase;
    private final UpdateSellerUseCase updateSellerUseCase;
    private final SellerAdminV1ApiMapper mapper;

    public SellerCommandV1Controller(
            ApplySellerApplicationUseCase applyUseCase,
            ApproveSellerApplicationUseCase approveUseCase,
            RejectSellerApplicationUseCase rejectUseCase,
            UpdateSellerUseCase updateSellerUseCase,
            SellerAdminV1ApiMapper mapper) {
        this.applyUseCase = applyUseCase;
        this.approveUseCase = approveUseCase;
        this.rejectUseCase = rejectUseCase;
        this.updateSellerUseCase = updateSellerUseCase;
        this.mapper = mapper;
    }

    /**
     * 셀러 등록 API.
     *
     * <p>새로운 셀러를 등록합니다.
     *
     * @param request 셀러 등록 요청 DTO
     * @return 생성된 셀러 ID
     * @deprecated V2 API를 사용하세요. POST /api/v2/admin/sellers
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "셀러 등록 (Deprecated)",
            description = "새로운 셀러를 등록합니다. V2 API를 사용하세요: POST /api/v2/admin/sellers")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @PostMapping(SellerAdminV1Endpoints.SELLER)
    public ResponseEntity<ApiResponse<Long>> registerSeller(
            @Valid @RequestBody SellerInfoContextV1ApiRequest request) {
        throw new UnsupportedOperationException(
                "registerSeller API is deprecated. Use V2 API: POST /api/v2/admin/sellers");
    }

    /**
     * 셀러 정산 계좌 확인 API.
     *
     * <p>셀러 정산 계좌 정보를 확인합니다.
     *
     * @param request 정산 계좌 확인 요청 DTO
     * @return 확인 결과 (null 반환)
     * @deprecated 이 API는 더 이상 지원되지 않습니다.
     */
    @Deprecated
    @Operation(
            summary = "셀러 정산 계좌 확인 (Deprecated)",
            description = "셀러 정산 계좌 정보의 유효성을 확인합니다. 이 API는 더 이상 지원되지 않습니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "확인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "계좌 정보 오류"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @PostMapping(SellerAdminV1Endpoints.SELLER_ACCOUNT)
    public ResponseEntity<ApiResponse<Long>> validateSellerAccount(
            @RequestBody CreateSellerSettlementAccountV1ApiRequest request) {
        throw new UnsupportedOperationException(
                "validateSellerAccount API is deprecated and no longer supported.");
    }

    /**
     * 셀러 정보 수정 API.
     *
     * <p>마스터 권한 필요. 기존 셀러의 정보를 수정합니다.
     *
     * @param sellerId 셀러 ID
     * @param request 셀러 수정 요청 DTO
     * @return 수정된 셀러 ID
     * @deprecated V2 API를 사용하세요. PUT /api/v2/admin/sellers/{sellerId}
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "셀러 정보 수정 (Deprecated)",
            description =
                    "셀러 정보를 수정합니다. (마스터 권한 필요) V2 API를 사용하세요: "
                            + "PUT /api/v2/admin/sellers/{sellerId}")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @PutMapping(SellerAdminV1Endpoints.SELLER_BY_ID)
    public ResponseEntity<ApiResponse<Long>> updateSeller(
            @Parameter(description = "셀러 ID", required = true)
                    @PathVariable(SellerAdminV1Endpoints.PATH_SELLER_ID)
                    Long sellerId,
            @Valid @RequestBody SellerUpdateDetailV1ApiRequest request) {
        throw new UnsupportedOperationException(
                "updateSeller API is deprecated. Use V2 API: PUT /api/v2/admin/sellers/{sellerId}");
    }

    /**
     * 셀러 승인 상태 변경 API.
     *
     * <p>마스터 권한 필요. 선택한 셀러들의 승인 상태를 변경합니다.
     *
     * @param request 승인 상태 변경 요청 DTO
     * @return 변경된 셀러 ID 목록
     * @deprecated V2 API를 사용하세요.
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    @Operation(
            summary = "셀러 승인 상태 변경 (Deprecated)",
            description = "셀러들의 승인 상태를 변경합니다. (마스터 권한 필요) V2 API를 사용하세요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "501",
                description = "지원되지 않는 API")
    })
    @PutMapping(SellerAdminV1Endpoints.SELLER_APPROVAL_STATUS)
    public ResponseEntity<ApiResponse<List<Long>>> changeApprovalStatus(
            @RequestBody SellerApprovalStatusV1ApiRequest request) {
        throw new UnsupportedOperationException(
                "changeApprovalStatus API is deprecated. Use V2 API for approval status changes.");
    }
}
