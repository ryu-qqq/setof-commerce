package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContextHolder;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerApprovalStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query.SellerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Seller Admin V1 API Mapper
 *
 * <p>V1 Legacy API DTO ↔ Application Command/Response 변환
 *
 * <p>Legacy API 호환성을 위해 V2 UseCase를 재사용하면서 V1 응답 형식으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Component
@Deprecated
public class SellerAdminV1ApiMapper {

    /**
     * V1 등록 요청 → RegisterSellerCommand 변환
     *
     * @param request V1 셀러 등록 요청
     * @return RegisterSellerCommand
     */
    public RegisterSellerCommand toRegisterCommand(SellerInfoContextV1ApiRequest request) {
        String tenantId = SecurityContextHolder.getCurrentTenantId();
        String organizationId = SecurityContextHolder.getCurrentOrganizationId();

        SellerInfoContextV1ApiRequest.SellerInfoInsertV1ApiRequest sellerInfo =
                request.sellerInfo();
        SellerInfoContextV1ApiRequest.SellerBusinessInfoV1ApiRequest businessInfo =
                request.sellerBusinessInfo();

        return new RegisterSellerCommand(
                tenantId,
                organizationId,
                sellerInfo.sellerName(),
                sellerInfo.sellerLogoUrl(),
                sellerInfo.sellerDescription(),
                businessInfo.registrationNumber(),
                businessInfo.saleReportNumber(),
                businessInfo.representative(),
                businessInfo.businessAddressLine1(),
                businessInfo.businessAddressLine2(),
                businessInfo.businessAddressZipCode(),
                businessInfo.csEmail(),
                businessInfo.csPhoneNumber(),
                businessInfo.csNumber());
    }

    /**
     * V1 수정 요청 → UpdateSellerCommand 변환
     *
     * @param sellerId 셀러 ID
     * @param request V1 셀러 수정 요청
     * @return UpdateSellerCommand
     */
    public UpdateSellerCommand toUpdateCommand(
            Long sellerId, SellerUpdateDetailV1ApiRequest request) {
        return new UpdateSellerCommand(
                sellerId,
                request.sellerName(),
                null, // logoUrl - V1 요청에 없음
                null, // description - V1 요청에 없음
                null, // registrationNumber - V1 요청에 없음
                null, // saleReportNumber - V1 요청에 없음
                null, // representative - V1 요청에 없음
                null, // businessAddressLine1 - V1 요청에 없음
                null, // businessAddressLine2 - V1 요청에 없음
                null, // businessZipCode - V1 요청에 없음
                request.csEmail(),
                request.csNumber(),
                null); // csLandlinePhone - V1 요청에 없음
    }

    /**
     * V1 필터 요청 → SellerSearchQuery 변환
     *
     * @param filter V1 셀러 필터 요청
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return SellerSearchQuery
     */
    public SellerSearchQuery toSearchQuery(SellerFilterV1ApiRequest filter, int page, int size) {
        String sellerName = null;
        if ("SELLER_NAME".equals(filter.searchKeyword())) {
            sellerName = filter.searchWord();
        }
        return new SellerSearchQuery(sellerName, filter.status(), page, size);
    }

    /**
     * V1 승인 상태 변경 요청 → UpdateApprovalStatusCommand 리스트 변환
     *
     * @param request V1 승인 상태 변경 요청
     * @return UpdateApprovalStatusCommand 리스트
     */
    public List<UpdateApprovalStatusCommand> toApprovalStatusCommands(
            SellerApprovalStatusV1ApiRequest request) {
        return request.sellerIds().stream()
                .map(
                        sellerId ->
                                new UpdateApprovalStatusCommand(sellerId, request.approvalStatus()))
                .toList();
    }

    /**
     * SellerResponse → SellerDetailV1ApiResponse 변환
     *
     * @param response Application 셀러 응답
     * @return V1 셀러 상세 응답
     */
    public SellerDetailV1ApiResponse toDetailResponse(SellerResponse response) {
        return new SellerDetailV1ApiResponse(
                response.id(),
                response.sellerName(),
                response.logoUrl(),
                null, // commissionRate - V2에 없음, 추후 확장 필요
                response.approvalStatus(),
                response.description(),
                response.businessInfo() != null ? response.businessInfo().addressLine1() : null,
                response.businessInfo() != null ? response.businessInfo().addressLine2() : null,
                response.businessInfo() != null ? response.businessInfo().zipCode() : null,
                null, // returnAddressLine1 - V2에 없음
                null, // returnAddressLine2 - V2에 없음
                null, // returnAddressZipCode - V2에 없음
                response.csInfo() != null ? response.csInfo().landlinePhone() : null,
                response.csInfo() != null ? response.csInfo().mobilePhone() : null,
                response.csInfo() != null ? response.csInfo().email() : null,
                response.businessInfo() != null
                        ? response.businessInfo().registrationNumber()
                        : null,
                response.businessInfo() != null ? response.businessInfo().saleReportNumber() : null,
                response.businessInfo() != null ? response.businessInfo().representative() : null,
                null, // bankName - V2에 없음
                null, // accountNumber - V2에 없음
                null, // accountHolderName - V2에 없음
                Collections.emptyList()); // sites - V2에 없음
    }

    /**
     * SellerResponse → SellerContextV1ApiResponse 변환
     *
     * <p>Legacy SellerContext는 인증된 셀러 정보를 반환합니다.
     *
     * @param response Application 셀러 응답
     * @param email 인증된 사용자 이메일
     * @param roleType 역할 타입
     * @return V1 셀러 컨텍스트 응답
     */
    public SellerContextV1ApiResponse toContextResponse(
            SellerResponse response, String email, String roleType) {
        return new SellerContextV1ApiResponse(
                response.id(),
                email,
                null, // passwordHash - 보안상 반환하지 않음
                roleType,
                response.approvalStatus());
    }

    /**
     * SellerSummaryResponse → SellerV1ApiResponse 변환
     *
     * @param response Application 셀러 요약 응답
     * @return V1 셀러 응답
     */
    public SellerV1ApiResponse toSummaryResponse(SellerSummaryResponse response) {
        return new SellerV1ApiResponse(
                response.id(),
                response.sellerName(),
                null, // commissionRate - V2에 없음
                response.approvalStatus(),
                null, // csPhoneNumber - V2 Summary에 없음
                null, // csEmail - V2 Summary에 없음
                null); // insertDate - V2 Summary에 없음
    }

    /**
     * PageResponse → V1PageResponse 변환
     *
     * @param pageResponse Application 페이지 응답
     * @return V1 페이지 응답
     */
    public V1PageResponse<SellerV1ApiResponse> toPageResponse(
            PageResponse<SellerSummaryResponse> pageResponse) {

        return V1PageResponse.from(pageResponse, this::toSummaryResponse);
    }
}
