package com.connectly.partnerAdmin.module.product.validator;

import com.connectly.partnerAdmin.module.brand.service.BrandFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandValidatorImpl implements BrandValidator{

    private final BrandFetchService brandFetchService;

    @Override
    public boolean isValid(long brandId) {
        return brandFetchService.hasBrandIdExist(brandId);
    }
}
