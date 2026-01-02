package com.connectly.partnerAdmin.module.product.validator;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryValidatorImpl implements CategoryValidator{
    private final CategoryFetchService categoryFetchService;

    @Override
    public boolean isValid(long categoryId) {
        return categoryFetchService.hasCategoryIdExist(categoryId);
    }

}
