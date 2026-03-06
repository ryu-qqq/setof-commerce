package com.ryuqq.setof.application.seller.internal;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.validator.SellerBusinessInfoValidator;
import com.ryuqq.setof.application.seller.validator.SellerValidator;
import org.springframework.stereotype.Component;

@Component
public class SellerRegistrationCoordinator {

    private final SellerValidator sellerValidator;
    private final SellerBusinessInfoValidator sellerBusinessInfoValidator;
    private final SellerCommandFacade sellerCommandFacade;

    public SellerRegistrationCoordinator(
            SellerValidator sellerValidator,
            SellerBusinessInfoValidator sellerBusinessInfoValidator,
            SellerCommandFacade sellerCommandFacade) {
        this.sellerValidator = sellerValidator;
        this.sellerBusinessInfoValidator = sellerBusinessInfoValidator;
        this.sellerCommandFacade = sellerCommandFacade;
    }

    public Long register(SellerRegistrationBundle bundle) {
        sellerValidator.validateSellerNameNotDuplicate(bundle.sellerNameValue());
        sellerBusinessInfoValidator.validateRegistrationNumberNotDuplicate(
                bundle.registrationNumberValue());
        return sellerCommandFacade.registerSeller(bundle);
    }
}
