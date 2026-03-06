package com.ryuqq.setof.application.seller.port.in.query;

import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;

public interface SearchSellerByOffsetUseCase {
    SellerPageResult execute(SellerSearchParams params);
}
