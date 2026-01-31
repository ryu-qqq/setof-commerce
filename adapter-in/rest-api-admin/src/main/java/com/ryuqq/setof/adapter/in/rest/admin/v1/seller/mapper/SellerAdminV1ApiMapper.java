package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query.SellerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApplySellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApproveSellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.dto.command.RejectSellerApplicationCommand;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * SellerAdminV1ApiMapper - V1 Admin 셀러 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 Admin API 응답으로 변환합니다.
 *
 * <p>레거시 호환을 위해 V1 요청의 enum String 값들을 새 도메인 값으로 변환합니다:
 *
 * <ul>
 *   <li>searchKeyword (레거시 SearchKeyword enum) → searchField (새 도메인)
 *   <li>status (레거시 ApprovalStatus enum) → active (Boolean)
 * </ul>
 *
 * <p>API-MAP-001: Mapper는 @Component로 정의.
 *
 * <p>API-MAP-002: 순수 변환 로직만 포함 (비즈니스 로직 금지).
 *
 * <p>API-MAP-003: null-safe 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerAdminV1ApiMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));

    /**
     * 레거시 SearchKeyword enum → 새 도메인 searchField 매핑.
     *
     * <p>레거시 SearchKeyword enum 값들:
     *
     * <ul>
     *   <li>SELLER_ID → "sellerId"
     *   <li>SELLER_NAME → "sellerName"
     * </ul>
     */
    private static final Map<String, String> SEARCH_KEYWORD_TO_FIELD =
            Map.of(
                    "SELLER_ID", "sellerId",
                    "SELLER_NAME", "sellerName");

    /**
     * 레거시 ApprovalStatus enum → active Boolean 매핑.
     *
     * <p>레거시 ApprovalStatus enum 값들:
     *
     * <ul>
     *   <li>APPROVED → true (활성 셀러)
     *   <li>PENDING → null (전체 조회)
     *   <li>REJECTED → false (비활성 셀러)
     * </ul>
     */
    private static final String APPROVAL_STATUS_APPROVED = "APPROVED";

    private static final String APPROVAL_STATUS_REJECTED = "REJECTED";

    /**
     * SellerFilterV1ApiRequest를 SellerSearchParams로 변환.
     *
     * <p>레거시 필드들을 새 도메인 값으로 변환합니다:
     *
     * <ul>
     *   <li>searchKeyword → searchField 변환
     *   <li>status → active 변환
     *   <li>siteIds는 현재 미사용 (향후 확장용)
     * </ul>
     *
     * @param filter V1 필터 요청
     * @return UseCase 검색 파라미터
     */
    public SellerSearchParams toSearchParams(SellerFilterV1ApiRequest filter) {
        CommonSearchParams commonParams =
                CommonSearchParams.of(
                        false,
                        null,
                        null,
                        filter.sortKey(),
                        filter.sortDirection(),
                        filter.page(),
                        filter.size());

        String searchField = convertSearchKeywordToField(filter.searchKeyword());
        Boolean active = convertStatusToActive(filter.status());

        // siteIds는 현재 미사용 (향후 확장 시 SellerSearchParams에 추가)

        return SellerSearchParams.of(active, searchField, filter.searchWord(), commonParams);
    }

    /**
     * 레거시 SearchKeyword enum String을 새 도메인 searchField로 변환.
     *
     * @param searchKeyword 레거시 SearchKeyword enum 값 (String)
     * @return 새 도메인 searchField 값, 매핑되지 않으면 null
     */
    private String convertSearchKeywordToField(String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return null;
        }
        return SEARCH_KEYWORD_TO_FIELD.getOrDefault(searchKeyword.toUpperCase(), null);
    }

    /**
     * 레거시 ApprovalStatus enum String을 active Boolean으로 변환.
     *
     * @param status 레거시 ApprovalStatus enum 값 (String)
     * @return active 상태 (APPROVED → true, REJECTED → false, 그 외 → null)
     */
    private Boolean convertStatusToActive(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        String upperStatus = status.toUpperCase();
        if (APPROVAL_STATUS_APPROVED.equals(upperStatus)) {
            return true;
        }
        if (APPROVAL_STATUS_REJECTED.equals(upperStatus)) {
            return false;
        }
        // PENDING 또는 기타 → null (전체 조회)
        return null;
    }

    /**
     * SellerFullCompositeResult를 V1 상세 응답으로 변환.
     *
     * <p>레거시 호환을 위해 없는 필드는 null로 설정합니다.
     *
     * @param result UseCase 실행 결과
     * @return V1 호환 셀러 상세 응답
     */
    public SellerDetailV1ApiResponse toDetailResponse(SellerFullCompositeResult result) {
        SellerCompositeResult composite = result.sellerComposite();
        var seller = composite.seller();
        var address = composite.address();
        var business = composite.businessInfo();

        return new SellerDetailV1ApiResponse(
                seller.id(),
                seller.sellerName(),
                seller.logoUrl(),
                null, // commissionRate - 정산 모듈 분리 예정
                null, // approvalStatus - 미구현
                seller.description(),
                nullSafe(business.businessAddress()),
                nullSafe(business.businessAddressDetail()),
                nullSafe(business.businessZipcode()),
                nullSafe(address.address()),
                nullSafe(address.addressDetail()),
                nullSafe(address.zipcode()),
                null, // csPhone - SellerCs 모듈로 분리
                null, // csMobile - SellerCs 모듈로 분리
                null, // csEmail - SellerCs 모듈로 분리
                nullSafe(business.registrationNumber()),
                nullSafe(business.saleReportNumber()),
                nullSafe(business.representative()),
                null, // bankName - 정산 모듈 분리 예정
                null, // accountNumber - 정산 모듈 분리 예정
                null // accountHolderName - 정산 모듈 분리 예정
                );
    }

    /**
     * SellerPageResult를 V1 셀러 목록 응답으로 변환.
     *
     * @param pageResult UseCase 실행 결과
     * @return V1 호환 셀러 목록
     */
    public List<SellerV1ApiResponse> toListResponse(SellerPageResult pageResult) {
        return pageResult.content().stream().map(this::toResponse).toList();
    }

    /**
     * SellerResult를 V1 셀러 응답으로 변환.
     *
     * @param seller 셀러 조회 결과
     * @return V1 호환 셀러 응답
     */
    private SellerV1ApiResponse toResponse(SellerResult seller) {
        return new SellerV1ApiResponse(
                seller.id(),
                seller.sellerName(),
                null, // commissionRate - 정산 모듈 분리 예정
                null, // approvalStatus - 미구현
                "", // csPhoneNumber - SellerResult에 없음
                "", // csEmail - SellerResult에 없음
                formatDateTime(seller.createdAt()));
    }

    /**
     * Instant를 레거시 형식 문자열로 변환.
     *
     * @param instant 시간
     * @return "yyyy-MM-dd HH:mm:ss" 형식 문자열
     */
    private String formatDateTime(Instant instant) {
        if (instant == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(instant);
    }

    /**
     * null을 빈 문자열로 변환.
     *
     * @param value 원본 값
     * @return null이면 빈 문자열, 아니면 원본 값
     */
    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    /**
     * V1 셀러 등록 요청을 입점 신청 Command로 변환.
     *
     * <p>레거시 V1 필드를 새 Application Command 구조로 매핑합니다.
     *
     * @param request V1 셀러 등록 요청
     * @return ApplySellerApplicationCommand
     */
    public ApplySellerApplicationCommand toApplyCommand(SellerInfoContextV1ApiRequest request) {
        var sellerInfo = request.sellerInfo();
        var businessInfo = request.sellerBusinessInfo();
        var shippingInfo = request.sellerShippingInfo();

        ApplySellerApplicationCommand.SellerInfoCommand sellerInfoCmd =
                new ApplySellerApplicationCommand.SellerInfoCommand(
                        sellerInfo.sellerName(),
                        sellerInfo.sellerName(),
                        sellerInfo.sellerLogoUrl(),
                        sellerInfo.sellerDescription());

        ApplySellerApplicationCommand.AddressCommand businessAddress =
                new ApplySellerApplicationCommand.AddressCommand(
                        nullSafe(businessInfo.businessAddressZipCode()),
                        nullSafe(businessInfo.businessAddressLine1()),
                        nullSafe(businessInfo.businessAddressLine2()));

        ApplySellerApplicationCommand.BusinessInfoCommand businessInfoCmd =
                new ApplySellerApplicationCommand.BusinessInfoCommand(
                        businessInfo.registrationNumber(),
                        businessInfo.companyName(),
                        businessInfo.representative(),
                        businessInfo.saleReportNumber(),
                        businessAddress);

        ApplySellerApplicationCommand.CsContactCommand csContactCmd =
                new ApplySellerApplicationCommand.CsContactCommand(
                        nullSafe(businessInfo.csNumber()),
                        nullSafe(businessInfo.csEmail()),
                        nullSafe(businessInfo.csPhoneNumber()));

        ApplySellerApplicationCommand.AddressCommand returnAddress =
                new ApplySellerApplicationCommand.AddressCommand(
                        nullSafe(shippingInfo.returnAddressZipCode()),
                        nullSafe(shippingInfo.returnAddressLine1()),
                        nullSafe(shippingInfo.returnAddressLine2()));

        ApplySellerApplicationCommand.ContactInfoCommand contactInfoCmd =
                new ApplySellerApplicationCommand.ContactInfoCommand(
                        nullSafe("cs 관리자"), nullSafe(businessInfo.csNumber()));

        ApplySellerApplicationCommand.AddressInfoCommand addressInfoCmd =
                new ApplySellerApplicationCommand.AddressInfoCommand(
                        "RETURN", "반품지", returnAddress, contactInfoCmd);

        return new ApplySellerApplicationCommand(
                sellerInfoCmd, businessInfoCmd, csContactCmd, addressInfoCmd);
    }

    /**
     * 승인 Command 생성.
     *
     * @param applicationId 신청 ID
     * @param processedBy 처리자
     * @return ApproveSellerApplicationCommand
     */
    public ApproveSellerApplicationCommand toApproveCommand(
            Long applicationId, String processedBy) {
        return new ApproveSellerApplicationCommand(applicationId, processedBy);
    }

    /**
     * 거절 Command 생성.
     *
     * @param applicationId 신청 ID
     * @param processedBy 처리자
     * @return RejectSellerApplicationCommand
     */
    public RejectSellerApplicationCommand toRejectCommand(Long applicationId, String processedBy) {
        return new RejectSellerApplicationCommand(applicationId, "V1 API 반려 처리", processedBy);
    }

    /**
     * V1 셀러 수정 요청을 셀러 정보 수정 Command로 변환.
     *
     * <p>Seller + Address + CS + BusinessInfo를 한번에 수정하는 Command로 변환합니다.
     *
     * @param sellerId 셀러 ID
     * @param request V1 수정 요청
     * @return UpdateSellerCommand
     */
    public UpdateSellerCommand toUpdateSellerCommand(
            Long sellerId, SellerUpdateDetailV1ApiRequest request) {
        UpdateSellerCommand.AddressCommand addressCommand =
                new UpdateSellerCommand.AddressCommand(
                        request.returnAddressZipCode(),
                        request.returnAddressLine1(),
                        request.returnAddressLine2());

        UpdateSellerCommand.CsInfoCommand csInfoCommand =
                new UpdateSellerCommand.CsInfoCommand(
                        nullSafe(request.csNumber()), nullSafe(request.csEmail()), null);

        UpdateSellerCommand.AddressCommand businessAddressCommand =
                new UpdateSellerCommand.AddressCommand(
                        request.businessAddressZipCode(),
                        request.businessAddressLine1(),
                        request.businessAddressLine2());

        UpdateSellerCommand.BusinessInfoCommand businessInfoCommand =
                new UpdateSellerCommand.BusinessInfoCommand(
                        request.registrationNumber(),
                        request.companyName(),
                        request.representative(),
                        request.saleReportNumber(),
                        businessAddressCommand);

        return new UpdateSellerCommand(
                sellerId,
                request.sellerName(),
                request.sellerName(),
                null,
                null,
                addressCommand,
                csInfoCommand,
                businessInfoCommand);
    }
}
