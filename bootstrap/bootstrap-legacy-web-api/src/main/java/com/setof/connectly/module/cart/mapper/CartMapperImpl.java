package com.setof.connectly.module.cart.mapper;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public Cart toEntity(long userId, CartDetails cartDetails) {
        return new Cart(userId, cartDetails);
    }

    @Override
    public List<CartResponse> toCartResponses(
            List<CartResponse> carts, List<ProductCategoryDto> productCategories) {
        Map<Long, ProductCategoryDto> categoryMap = createCategoryMap(productCategories);
        carts.forEach(
                cartResponse -> {
                    Set<OptionDto> options = cartResponse.getOptions();
                    String optionName = getOptionName(options);
                    cartResponse.setOptionValue(optionName);
                    buildCategoryPath(cartResponse, categoryMap);
                });

        return carts;
    }

    private String getOptionName(Set<OptionDto> options) {
        return options.stream()
                .sorted(Comparator.comparing(OptionDto::getOptionGroupId))
                .map(OptionDto::getOptionValue)
                .collect(Collectors.joining(" "));
    }

    public Map<Long, ProductCategoryDto> createCategoryMap(List<ProductCategoryDto> categories) {
        return categories.stream()
                .collect(Collectors.toMap(ProductCategoryDto::getCategoryId, category -> category));
    }

    public void buildCategoryPath(
            CartResponse cartResponse, Map<Long, ProductCategoryDto> categoryMap) {
        String[] categoryIds = cartResponse.getPath().split(",");

        Set<ProductCategoryDto> categoryDtoList =
                Arrays.stream(categoryIds)
                        .mapToLong(Long::parseLong)
                        .mapToObj(categoryMap::get)
                        .collect(Collectors.toSet());

        cartResponse.setCategories(categoryDtoList);
    }
}
