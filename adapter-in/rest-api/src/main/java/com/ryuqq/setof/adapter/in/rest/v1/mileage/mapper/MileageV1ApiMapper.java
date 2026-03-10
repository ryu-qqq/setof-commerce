package com.ryuqq.setof.adapter.in.rest.v1.mileage.mapper;

import com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.request.SearchMileageHistoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryPageV1ApiResponse.PageableResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryPageV1ApiResponse.SortResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryPageV1ApiResponse.UserMileageSummaryResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response.MileageHistoryV1ApiResponse;
import com.ryuqq.setof.application.mileage.dto.query.MileageHistorySearchParams;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryPageResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageSummaryResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * MileageV1ApiMapper - 마일리지 V1 Public API 변환 매퍼.
 *
 * <p>Application Result → Legacy 호환 V1 Response 변환.
 *
 * <p>레거시 MileagePage 구조에 맞춰 페이징 필드를 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MileageV1ApiMapper {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    // ─────────────────────────────────────────────
    // Request → Params 변환
    // ─────────────────────────────────────────────

    /**
     * SearchMileageHistoriesV1ApiRequest → MileageHistorySearchParams 변환.
     *
     * <p>기본값 처리: page가 null이면 0, size가 null이면 20.
     */
    public MileageHistorySearchParams toSearchParams(
            long userId, SearchMileageHistoriesV1ApiRequest request) {
        int page = request.page() != null ? request.page() : DEFAULT_PAGE;
        int size = request.size() != null ? request.size() : DEFAULT_SIZE;
        List<String> reasons = request.reasons() != null ? request.reasons() : List.of();
        return MileageHistorySearchParams.of(userId, reasons, page, size);
    }

    // ─────────────────────────────────────────────
    // Result → Response 변환
    // ─────────────────────────────────────────────

    /**
     * MileageHistoryPageResult → MileageHistoryPageV1ApiResponse 변환.
     *
     * <p>레거시 MileagePage 구조에 맞춰 페이징 메타를 개별 필드로 풀어 반환합니다. numberOfElements는 content 리스트 크기로 산정합니다.
     */
    public MileageHistoryPageV1ApiResponse toPageResponse(MileageHistoryPageResult result) {
        MileageSummaryResult summary = result.mileageSummary();
        PageMeta pageMeta = result.pageMeta();

        UserMileageSummaryResponse userMileage =
                new UserMileageSummaryResponse(
                        summary.userId(),
                        summary.currentMileage(),
                        summary.expectedSaveMileage(),
                        summary.expectedExpireMileage());

        List<MileageHistoryV1ApiResponse> content =
                result.histories().stream().map(this::toHistoryResponse).toList();

        SortResponse sort = SortResponse.defaultUnsorted();
        PageableResponse pageable =
                new PageableResponse(
                        pageMeta.page(), pageMeta.size(), sort, pageMeta.offset(), false, true);

        return new MileageHistoryPageV1ApiResponse(
                userMileage,
                content,
                pageable,
                pageMeta.isLast(),
                pageMeta.totalPages(),
                pageMeta.totalElements(),
                pageMeta.isFirst(),
                pageMeta.page(),
                sort,
                pageMeta.size(),
                content.size(),
                pageMeta.isEmpty(),
                null);
    }

    private MileageHistoryV1ApiResponse toHistoryResponse(MileageHistoryItemResult item) {
        long paymentId = item.paymentId() != null ? item.paymentId() : 0L;
        long orderId = item.orderId() != null ? item.orderId() : 0L;
        return new MileageHistoryV1ApiResponse(
                item.mileageId(),
                item.title(),
                paymentId,
                orderId,
                item.changeAmount(),
                item.reason(),
                DateTimeFormatUtils.format(item.usedDate()),
                DateTimeFormatUtils.format(item.expirationDate()));
    }
}
