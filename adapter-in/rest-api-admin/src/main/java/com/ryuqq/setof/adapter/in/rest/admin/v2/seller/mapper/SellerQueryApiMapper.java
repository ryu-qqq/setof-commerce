package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.util.DateTimeFormatUtils;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerQueryApiMapper - 셀러 Query API 변환 매퍼.
 *
 * <p>API Request/Response와 Application Query/Result 간 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result -> API Response 변환.
 *
 * <p>API-MAP-004: Slice/Page 변환 지원.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>API-DTO-005: 날짜 String 변환 필수.
 *
 * <p>CQRS 분리: Query 전용 Mapper (CommandApiMapper와 분리).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerQueryApiMapper {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    /**
     * SearchSellersApiRequest -> SellerSearchParams 변환.
     *
     * @param request 조회 요청 DTO
     * @return SellerSearchParams 객체
     */
    public SellerSearchParams toSearchParams(SearchSellersApiRequest request) {
        int page = request.page() != null ? request.page() : DEFAULT_PAGE;
        int size = request.size() != null ? request.size() : DEFAULT_SIZE;

        CommonSearchParams searchParams =
                CommonSearchParams.of(
                        null, null, null, request.sortKey(), request.sortDirection(), page, size);

        return SellerSearchParams.of(
                request.active(), request.searchField(), request.searchWord(), searchParams);
    }

    /**
     * 단일 SellerResult -> SellerApiResponse 변환.
     *
     * @param result SellerResult
     * @return SellerApiResponse
     */
    public SellerApiResponse toResponse(SellerResult result) {
        return new SellerApiResponse(
                result.id(),
                result.sellerName(),
                result.displayName(),
                result.logoUrl(),
                result.description(),
                result.active(),
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.updatedAt()));
    }

    /**
     * SellerPageResult -> PageApiResponse 변환.
     *
     * @param pageResult Application 페이지 결과 DTO
     * @return API 페이지 응답 DTO
     */
    public PageApiResponse<SellerApiResponse> toPageResponse(SellerPageResult pageResult) {
        List<SellerApiResponse> responses =
                pageResult.results().stream().map(this::toResponse).toList();
        return PageApiResponse.of(
                responses,
                pageResult.pageMeta().page(),
                pageResult.pageMeta().size(),
                pageResult.pageMeta().totalElements());
    }

    /**
     * SellerCompositeResult -> SellerDetailApiResponse 변환.
     *
     * @param result SellerCompositeResult
     * @return SellerDetailApiResponse
     */
    public SellerDetailApiResponse toDetailResponse(SellerCompositeResult result) {
        SellerDetailApiResponse.SellerInfo sellerInfo = null;
        if (result.seller() != null) {
            SellerCompositeResult.SellerInfo s = result.seller();
            sellerInfo =
                    new SellerDetailApiResponse.SellerInfo(
                            s.id(),
                            s.sellerName(),
                            s.displayName(),
                            s.logoUrl(),
                            s.description(),
                            s.active(),
                            DateTimeFormatUtils.formatIso8601(s.createdAt()),
                            DateTimeFormatUtils.formatIso8601(s.updatedAt()));
        }

        SellerDetailApiResponse.BusinessInfo businessInfo = null;
        if (result.businessInfo() != null) {
            SellerCompositeResult.BusinessInfo b = result.businessInfo();
            businessInfo =
                    new SellerDetailApiResponse.BusinessInfo(
                            b.id(),
                            b.registrationNumber(),
                            b.companyName(),
                            b.representative(),
                            b.saleReportNumber(),
                            b.businessZipcode(),
                            b.businessAddress(),
                            b.businessAddressDetail());
        }

        SellerDetailApiResponse.CsInfo csInfo = null;
        if (result.csInfo() != null) {
            SellerCompositeResult.CsInfo c = result.csInfo();
            csInfo =
                    new SellerDetailApiResponse.CsInfo(
                            c.id(),
                            c.csPhone(),
                            c.csMobile(),
                            c.csEmail(),
                            c.operatingStartTime(),
                            c.operatingEndTime(),
                            c.operatingDays(),
                            c.kakaoChannelUrl());
        }

        return new SellerDetailApiResponse(sellerInfo, businessInfo, csInfo);
    }
}
