package com.ryuqq.setof.application.seller.dto.response;

import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.util.List;

public record SellerPageResult(List<SellerResult> results, PageMeta pageMeta) {
    public static SellerPageResult of(
            List<SellerResult> results, int page, int size, long totalElements) {
        return new SellerPageResult(results, PageMeta.of(page, size, totalElements));
    }
}
