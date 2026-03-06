package com.ryuqq.setof.application.seller.assembler;

import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerAssembler {

    public SellerResult toResult(Seller seller) {
        return SellerResult.from(seller);
    }

    public List<SellerResult> toResults(List<Seller> sellers) {
        return sellers.stream().map(SellerResult::from).toList();
    }

    public SellerPageResult toPageResult(
            List<Seller> sellers, int page, int size, long totalElements) {
        List<SellerResult> results = toResults(sellers);
        return SellerPageResult.of(results, page, size, totalElements);
    }
}
