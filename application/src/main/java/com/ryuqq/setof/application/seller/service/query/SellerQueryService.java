package com.ryuqq.setof.application.seller.service.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.assembler.SellerAssembler;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.manager.query.SellerReadManager;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 셀러 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>SellerReadManager로 셀러 조회
 *   <li>SellerAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SellerQueryService implements GetSellerUseCase, GetSellersUseCase {

    private final SellerReadManager sellerReadManager;
    private final SellerAssembler sellerAssembler;

    public SellerQueryService(
            SellerReadManager sellerReadManager, SellerAssembler sellerAssembler) {
        this.sellerReadManager = sellerReadManager;
        this.sellerAssembler = sellerAssembler;
    }

    @Override
    public SellerResponse execute(Long sellerId) {
        Seller seller = sellerReadManager.findById(sellerId);
        return sellerAssembler.toSellerResponse(seller);
    }

    @Override
    public PageResponse<SellerSummaryResponse> execute(SellerSearchQuery query) {
        List<Seller> sellers =
                sellerReadManager.findByConditions(
                        query.sellerName(), query.approvalStatus(), query.page(), query.size());
        long totalCount =
                sellerReadManager.countByConditions(query.sellerName(), query.approvalStatus());

        List<SellerSummaryResponse> content = sellerAssembler.toSellerSummaryResponses(sellers);

        int totalPages = calculateTotalPages(totalCount, query.size());
        boolean isFirst = query.page() == 0;
        boolean isLast = query.page() >= totalPages - 1;

        return PageResponse.of(
                content, query.page(), query.size(), totalCount, totalPages, isFirst, isLast);
    }

    private int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
