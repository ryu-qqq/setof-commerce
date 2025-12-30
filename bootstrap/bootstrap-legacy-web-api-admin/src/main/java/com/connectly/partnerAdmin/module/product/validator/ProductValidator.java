package com.connectly.partnerAdmin.module.product.validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.connectly.partnerAdmin.module.product.annotation.ProductValidate;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.connectly.partnerAdmin.module.product.exception.InvalidProductException;

import static com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductValidator implements ConstraintValidator<ProductValidate, CreateProductGroup> {

    private final BrandValidator brandValidator;
    private final CategoryValidator categoryValidator;
    private final ProductImageValidator productImageValidator;
    private final SellerValidator sellerValidator;
    private final PriceValidator priceValidator;



    @Override
    public boolean isValid(CreateProductGroup createProductGroup, ConstraintValidatorContext context) {
        try {

            checkPrice(createProductGroup.getPrice());
            checkBrand(createProductGroup.getBrandId());
            checkCategory(createProductGroup.getCategoryId());
            checkProductImage(createProductGroup.getProductImageList());
            checkSeller(createProductGroup.getSellerId());
            checkOption(createProductGroup);

        } catch (InvalidProductException ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ex.getMessage())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private void checkPrice(CreatePrice createPrice){
        if(!priceValidator.isValid(createPrice)){
            throw new InvalidProductException(INVALID_PRICE_MSG);
        }
    }


    private void checkBrand(long brandId) {
        if (!brandValidator.isValid(brandId)) {
            throw new InvalidProductException(INVALID_BRAND_MSG);
        }
    }

    private void checkCategory(long categoryId) {
        if (!categoryValidator.isValid(categoryId)) {
            throw new InvalidProductException(INVALID_CATEGORY_MSG);
        }
    }

    private void checkProductImage(List<CreateProductImage> createProductImages) {
        if (!productImageValidator.isValid(createProductImages)) {
            throw new InvalidProductException(INVALID_MAIN_IMAGE_MSG);
        }
    }


    private void checkSeller(long sellerId) {
        sellerValidator.isValid(sellerId);
    }

    private void checkOption(CreateProductGroup createProductGroup) {
        CreateOption first = createProductGroup.getProductOptions().getFirst();

        if(createProductGroup.getOptionType().isSingle()){
            if(!first.getOptions().isEmpty()) throw new InvalidProductException(INVALID_REGISTER_PRODUCT_OPTION_MSG);
        }

        if(createProductGroup.getOptionType().isOneDepth()){
            if(first.getOptions().size() != 1) throw new InvalidProductException(INVALID_OPTION_LEVEL_MSG);
        }

        if(createProductGroup.getOptionType().isTwoDepth()){
            if(first.getOptions().size() != 2) throw new InvalidProductException(INVALID_OPTION_LEVEL_MSG);

            Set<OptionName> optionNameSet = createProductGroup.getProductOptions()
                .stream()
                .flatMap(c -> c.getOptions().stream())
                .map(
                    CreateOptionDetail::getOptionName)
                .collect(Collectors.toSet());


            if(!isValidTwoStepOptionCombination(optionNameSet)) throw new InvalidProductException("Two step options must be a valid combination of Color and Size, or Default_One and Default_Two.");
        }

    }


    private boolean isValidTwoStepOptionCombination(Set<OptionName> optionNames) {
        return (optionNames.contains(OptionName.COLOR) && optionNames.contains(OptionName.SIZE)) ||
            (optionNames.contains(OptionName.DEFAULT_ONE) && optionNames.contains(OptionName.DEFAULT_TWO));
    }



}
