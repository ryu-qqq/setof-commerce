package com.connectly.partnerAdmin.module.product.mapper.stock;

import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class ProductMapperImpl implements ProductMapper{



    @Override
    public Set<Product> toProducts(List<CreateOption> options){
        LinkedHashMap<OptionName, OptionGroup> optionGroupMap = new LinkedHashMap<>();
        LinkedHashMap<String, OptionDetail> optionDetailMap = new LinkedHashMap<>();

        Set<Product> products = new LinkedHashSet<>();

        for (CreateOption createOption : options) {
            createProduct(createOption, optionGroupMap, optionDetailMap, products);
        }

        return products;
    }



    private void createProduct(CreateOption createOption, Map<OptionName, OptionGroup> optionGroupMap, LinkedHashMap<String, OptionDetail> optionDetailMap, Set<Product> products) {
        Set<ProductOption> productOptions = new LinkedHashSet<>();

        BigDecimal additionalPrice = createOption.getAdditionalPrice();
        for (CreateOptionDetail createOptionDetail : createOption.getOptions()) {

            OptionGroup optionGroup;
            OptionDetail optionDetail;

            if (createOptionDetail.getOptionGroupId() != null) {
                optionGroup = optionGroupMap.computeIfAbsent(createOptionDetail.getOptionName(), k -> new OptionGroup(createOptionDetail.getOptionGroupId(), createOptionDetail.getOptionName()));
                if(createOptionDetail.getOptionDetailId() != null){
                    optionDetail = optionDetailMap.computeIfAbsent(createOptionDetail.getOptionValue(), k -> createAndLinkOptionDetail(createOptionDetail.getOptionDetailId(), optionGroup, createOptionDetail.getOptionValue()));
                } else {
                    optionDetail = optionDetailMap.computeIfAbsent(createOptionDetail.getOptionValue(), k -> createAndLinkOptionDetail(optionGroup, createOptionDetail.getOptionValue()));
                }
            } else {
                optionGroup = optionGroupMap.computeIfAbsent(createOptionDetail.getOptionName(), OptionGroup::new);
                optionDetail = optionDetailMap.computeIfAbsent(createOptionDetail.getOptionValue(), k -> createAndLinkOptionDetail(optionGroup, createOptionDetail.getOptionValue()));
            }

            productOptions.add(new ProductOption(optionGroup, optionDetail, additionalPrice));
        }

        Product andAddProductOption = createAndAddProductOption(createOption.getProductId(), createOption.getQuantity(), productOptions);
        products.add(andAddProductOption);
    }


    private OptionDetail createAndLinkOptionDetail(long optionDetailId, OptionGroup optionGroup, String optionValue) {
        return new OptionDetail(optionDetailId, optionValue, optionGroup);
    }

    private OptionDetail createAndLinkOptionDetail(OptionGroup optionGroup, String optionValue) {
        return new OptionDetail(optionValue, optionGroup);
    }

    private Product createAndAddProductOption(Long productId, int qty, Set<ProductOption> productOptions) {
        Product product;
        if (productId != null) {
            product = new Product(productId, qty);
        } else {
            product = new Product(qty);
        }
        productOptions.forEach(product::addProductOption);
        return product;
    }

}
