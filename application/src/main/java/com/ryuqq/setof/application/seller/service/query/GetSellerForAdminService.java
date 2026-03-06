package com.ryuqq.setof.application.seller.service.query;

import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.manager.SellerCompositionReadManager;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetSellerForAdminService implements GetSellerForAdminUseCase {

    private final SellerCompositionReadManager sellerCompositionReadManager;

    public GetSellerForAdminService(SellerCompositionReadManager sellerCompositionReadManager) {
        this.sellerCompositionReadManager = sellerCompositionReadManager;
    }

    @Override
    public SellerCompositeResult execute(Long sellerId) {
        return sellerCompositionReadManager.getSellerComposite(sellerId);
    }
}
