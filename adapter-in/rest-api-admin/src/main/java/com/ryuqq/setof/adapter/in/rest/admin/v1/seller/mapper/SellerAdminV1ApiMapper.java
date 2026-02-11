package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SearchSellersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.AddressInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.BusinessInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.ContractInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.CsInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.SellerInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse.SettlementInfoResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerAdminV1ApiMapper - 셀러 Admin V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 SellerController.fetchSellers / fetchSellerById 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerAdminV1ApiMapper {

    private static final String DEFAULT_SEARCH_FIELD = "sellerName";
    private static final String DEFAULT_SORT_KEY = "createdAt";

    /**
     * SearchSellersV1ApiRequest → SellerSearchParams 변환.
     *
     * <p>searchKeyword를 searchField로 매핑합니다.
     *
     * <ul>
     *   <li>SELLER_ID → id
     *   <li>SELLER_NAME → sellerName (기본값)
     * </ul>
     *
     * @param request 검색 요청 DTO
     * @return SellerSearchParams
     */
    public SellerSearchParams toSearchParams(SearchSellersV1ApiRequest request) {
        String searchField = resolveSearchField(request.searchKeyword());
        String searchWord =
                request.searchWord() != null && !request.searchWord().isBlank()
                        ? request.searchWord().trim()
                        : null;

        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 20;

        CommonSearchParams searchParams =
                CommonSearchParams.of(false, null, null, DEFAULT_SORT_KEY, "DESC", page, size);

        return SellerSearchParams.of(null, searchField, searchWord, searchParams);
    }

    /**
     * SellerPageResult → CustomPageableV1ApiResponse 변환.
     *
     * @param pageResult Application 페이지 결과
     * @return CustomPageableV1ApiResponse
     */
    public CustomPageableV1ApiResponse<SellerV1ApiResponse> toPageResponse(
            SellerPageResult pageResult) {
        List<SellerV1ApiResponse> content =
                pageResult.content().stream().map(this::toResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content,
                pageResult.pageMeta().page(),
                pageResult.pageMeta().size(),
                pageResult.pageMeta().totalElements());
    }

    /**
     * SellerResult → SellerV1ApiResponse 변환.
     *
     * @param result SellerResult
     * @return SellerV1ApiResponse
     */
    public SellerV1ApiResponse toResponse(SellerResult result) {
        long sellerId = result.id() != null ? result.id() : 0L;
        return new SellerV1ApiResponse(
                sellerId,
                result.sellerName(),
                result.displayName(),
                result.logoUrl(),
                result.description(),
                result.active(),
                result.createdAt(),
                result.updatedAt());
    }

    /**
     * SellerFullCompositeResult → SellerDetailV1ApiResponse 변환.
     *
     * @param result SellerFullCompositeResult
     * @return SellerDetailV1ApiResponse
     */
    public SellerDetailV1ApiResponse toDetailResponse(SellerFullCompositeResult result) {
        SellerCompositeResult composite = result.sellerComposite();

        SellerInfoResponse seller = toSellerInfoResponse(composite.seller());
        AddressInfoResponse address = toAddressInfoResponse(composite.address());
        BusinessInfoResponse businessInfo = toBusinessInfoResponse(composite.businessInfo());
        CsInfoResponse csInfo = toCsInfoResponse(composite.csInfo());
        ContractInfoResponse contractInfo = toContractInfoResponse(result.contractInfo());
        SettlementInfoResponse settlementInfo = toSettlementInfoResponse(result.settlementInfo());

        return new SellerDetailV1ApiResponse(
                seller, address, businessInfo, csInfo, contractInfo, settlementInfo);
    }

    private String resolveSearchField(String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return DEFAULT_SEARCH_FIELD;
        }
        return switch (searchKeyword.toUpperCase()) {
            case "SELLER_ID" -> "id";
            case "SELLER_NAME" -> "sellerName";
            default -> DEFAULT_SEARCH_FIELD;
        };
    }

    private SellerInfoResponse toSellerInfoResponse(SellerCompositeResult.SellerInfo info) {
        if (info == null) {
            return null;
        }
        return new SellerInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.sellerName(),
                info.displayName(),
                info.logoUrl(),
                info.description(),
                info.active(),
                info.createdAt(),
                info.updatedAt());
    }

    private AddressInfoResponse toAddressInfoResponse(SellerCompositeResult.AddressInfo info) {
        if (info == null) {
            return null;
        }
        return new AddressInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.addressType(),
                info.addressName(),
                info.zipcode(),
                info.address(),
                info.addressDetail(),
                info.contactName(),
                info.contactPhone(),
                info.defaultAddress());
    }

    private BusinessInfoResponse toBusinessInfoResponse(SellerCompositeResult.BusinessInfo info) {
        if (info == null) {
            return null;
        }
        return new BusinessInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.registrationNumber(),
                info.companyName(),
                info.representative(),
                info.saleReportNumber(),
                info.businessZipcode(),
                info.businessAddress(),
                info.businessAddressDetail());
    }

    private CsInfoResponse toCsInfoResponse(SellerCompositeResult.CsInfo info) {
        if (info == null) {
            return null;
        }
        return new CsInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.csPhone(),
                info.csMobile(),
                info.csEmail(),
                info.operatingStartTime(),
                info.operatingEndTime(),
                info.operatingDays(),
                info.kakaoChannelUrl());
    }

    private ContractInfoResponse toContractInfoResponse(
            SellerFullCompositeResult.ContractInfo info) {
        if (info == null) {
            return null;
        }
        return new ContractInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.commissionRate(),
                info.contractStartDate(),
                info.contractEndDate(),
                info.status(),
                info.specialTerms(),
                info.createdAt(),
                info.updatedAt());
    }

    private SettlementInfoResponse toSettlementInfoResponse(
            SellerFullCompositeResult.SettlementInfo info) {
        if (info == null) {
            return null;
        }
        return new SettlementInfoResponse(
                info.id() != null ? info.id() : 0L,
                info.bankCode(),
                info.bankName(),
                info.accountNumber(),
                info.accountHolderName(),
                info.settlementCycle(),
                info.settlementDay(),
                info.verified(),
                info.verifiedAt(),
                info.createdAt(),
                info.updatedAt());
    }
}
