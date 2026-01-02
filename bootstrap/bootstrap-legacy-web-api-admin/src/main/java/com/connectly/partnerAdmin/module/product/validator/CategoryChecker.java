package com.connectly.partnerAdmin.module.product.validator;

import com.connectly.partnerAdmin.module.product.annotation.CategoryValidate;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateCategory;
import com.connectly.partnerAdmin.module.product.exception.InvalidProductException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import static com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant.INVALID_CATEGORY_MSG;


@RequiredArgsConstructor
public class CategoryChecker implements ConstraintValidator<CategoryValidate, UpdateCategory> {

    private final CategoryValidator categoryValidator;


    @Override
    public boolean isValid(UpdateCategory updateCategory, ConstraintValidatorContext context) {
        try {
            checkCategory(updateCategory.getCategoryId());
        } catch (InvalidProductException ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ex.getMessage())
                    .addPropertyNode("categoryId").addConstraintViolation();
            return false;
        }
        return true;
    }

    private void checkCategory(long categoryId) {
        if (!categoryValidator.isValid(categoryId)) {
            throw new InvalidProductException(INVALID_CATEGORY_MSG);
        }
    }
}
