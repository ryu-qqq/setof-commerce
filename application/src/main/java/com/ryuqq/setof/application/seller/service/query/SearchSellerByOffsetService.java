package com.ryuqq.setof.application.seller.service.query;

import com.ryuqq.setof.application.seller.assembler.SellerAssembler;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.factory.SellerQueryFactory;
import com.ryuqq.setof.application.seller.manager.SellerReadManager;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SearchSellerByOffsetService implements SearchSellerByOffsetUseCase {

    private final SellerReadManager sellerReadManager;
    private final SellerQueryFactory sellerQueryFactory;
    private final SellerAssembler sellerAssembler;

    public SearchSellerByOffsetService(
            SellerReadManager sellerReadManager,
            SellerQueryFactory sellerQueryFactory,
            SellerAssembler sellerAssembler) {
        this.sellerReadManager = sellerReadManager;
        this.sellerQueryFactory = sellerQueryFactory;
        this.sellerAssembler = sellerAssembler;
    }

    @Override
    public SellerPageResult execute(SellerSearchParams params) {
        SellerSearchCriteria criteria = sellerQueryFactory.createCriteria(params);

        List<Seller> sellers = sellerReadManager.findByCriteria(criteria);
        long totalElements = sellerReadManager.countByCriteria(criteria);

        return sellerAssembler.toPageResult(sellers, params.page(), params.size(), totalElements);
    }
}
