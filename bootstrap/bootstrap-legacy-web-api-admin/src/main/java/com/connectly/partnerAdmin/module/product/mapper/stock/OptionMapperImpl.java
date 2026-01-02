package com.connectly.partnerAdmin.module.product.mapper.stock;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchDto;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;

@Component
public class OptionMapperImpl implements OptionMapper {

    @Override
    public Set<ProductFetchResponse> toProductFetchResponse(List<ProductFetchDto> products) {
        return transProductDto(products);
    }

    @Override
    public Set<ProductFetchResponse> toProductFetchResponse(long productGroupId, Set<Product> updatedProducts) {
        List<ProductFetchDto> productFetchDtos = toProductFetchDtos(productGroupId, updatedProducts);
        return transProductDto(productFetchDtos);
    }


    private List<ProductFetchDto> toProductFetchDtos(Long productGroupId, Set<Product> products) {
        return products.stream()
                .flatMap(product -> {
                    if (product.getProductOptions() == null) {
                        return Stream.empty();
                    }
                    return product.getProductOptions().stream()
                            .map(productOption -> {
                                OptionGroup optionGroup = productOption.getOptionGroup();
                                OptionDetail optionDetail = productOption.getOptionDetail();

                                return ProductFetchDto.builder()
                                        .productGroupId(productGroupId)
                                        .productId(product.getId())
                                        .stockQuantity(product.getStockQuantity())
                                        .productStatus(product.getProductStatus())
                                        .optionGroupId(optionGroup.getId())
                                        .optionDetailId(optionDetail.getId())
                                        .optionName(optionGroup.getOptionName())
                                        .optionValue(optionDetail.getOptionValue())
                                        .additionalPrice(productOption.getAdditionalPrice())
                                        .build();
                            })
                            .filter(Objects::nonNull);
                })
                .collect(Collectors.toList());
    }

    private Set<ProductFetchResponse> transProductDto(List<ProductFetchDto> products) {
        Map<Long, Set<OptionDto>> groupedOptions = getGroupedOptions(products);
        Map<Long, ProductFetchDto> productMap = products.stream()
                .collect(Collectors.toMap(ProductFetchDto::getProductId, Function.identity(), (existing, replacement) -> existing));

        return setOption(groupedOptions, productMap);
    }


    private Map<Long, Set<OptionDto>> getGroupedOptions(List<ProductFetchDto> products) {
        return products.stream()
                .collect(Collectors.groupingBy(ProductFetchDto::getProductId,
                        Collectors.flatMapping(p -> {
                            OptionDto optionDto = OptionDto.builder()
                                    .optionGroupId(p.getOptionGroupId())
                                    .optionDetailId(p.getOptionDetailId())
                                    .optionName(p.getOptionName())
                                    .optionValue(p.getOptionValue())
                                    .build();
                            return Stream.of(optionDto);
                        }, Collectors.toSet())));
    }

    private Set<ProductFetchResponse> setOption(Map<Long, Set<OptionDto>> groupedOptions, Map<Long, ProductFetchDto> productMap) {
        TreeSet<ProductFetchResponse> sortedResponses = groupedOptions.entrySet().stream().map(entry -> {

            Long productId = entry.getKey();
                    Set<OptionDto> options = entry.getValue();

                    Set<OptionDto> filteredOptions = options.stream()
                            .filter(option -> option.getOptionGroupId() != null && option.getOptionGroupId() != 0 && option.getOptionValue() != null)
                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OptionDto::getOptionGroupId))));

                    if (!filteredOptions.isEmpty()) {
                        filteredOptions.addAll(options);
                    }

                    ProductFetchDto productFetchDto = productMap.get(productId);
                    if (productFetchDto != null) {
                        if(productFetchDto.getOptionName() != null && productFetchDto.getOptionValue() == null) {
                            return null;
                        }

                        return ProductFetchResponse.builder()
                                .productGroupId(productFetchDto.getProductGroupId())
                                .productId(productFetchDto.getProductId())
                                .stockQuantity(productFetchDto.getStockQuantity())
                                .productStatus(productFetchDto.getProductStatus())
                                .option(getOptionName(filteredOptions))
                                .options(filteredOptions)
                                .additionalPrice(productFetchDto.getAdditionalPrice())
                                .build();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(
                        Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparingLong(ProductFetchResponse::getProductId))
                        )
                );

        return new LinkedHashSet<>(sortedResponses);
    }

    private String getOptionName(Set<OptionDto> options) {
        return options.stream()
                .sorted(Comparator.comparing(OptionDto::getOptionGroupId))
                .map(OptionDto::getOptionValue)
                .collect(Collectors.joining(" "));
    }


    @Override
    public void setProductIdInCreateOptions(List<CreateOption> createOptions, List<Product> products) {
        Map<String, Product> fullOptionNameKeyMap = productsToFullOptionNameKeyMap(products);
        Map<OptionName, OptionDto> optionGroupNameMap = toOptionGroupNameMap(products);
        createOptions.forEach(c ->{
            Map<String, CreateOptionDetail> createOptionFullOptionMap = createOptionsToFullOptionNameKeyMap(c.getOptions());
            setProductIdAndOptionDetailId(c, fullOptionNameKeyMap, optionGroupNameMap, createOptionFullOptionMap);
        });
    }

    private Map<String, Product> productsToFullOptionNameKeyMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getOption,
                        Function.identity(),
                        (existingValue, newValue)
                                -> existingValue));
    }

    private Map<OptionName, OptionDto> toOptionGroupNameMap(List<Product> products) {
        return products.stream()
                .flatMap(p -> p.toOptions()
                        .stream())
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toMap(
                        OptionDto::getOptionName,
                        Function.identity(),
                        (existingValue, newValue) -> existingValue));

    }


    private Map<String, CreateOptionDetail> createOptionsToFullOptionNameKeyMap(List<CreateOptionDetail> options){
        return options.stream()
                .collect(Collectors.toMap(CreateOptionDetail::getFullOptionName,
                        Function.identity(), (existingValue, newValue)
                                -> existingValue));
    }




    private void setProductIdAndOptionDetailId(CreateOption createOption, Map<String, Product> fullOptionNameKeyMap, Map<OptionName, OptionDto> optionGroupNameMap, Map<String, CreateOptionDetail> createOptionFullOptionMap) {
        String fullOption = createOption.getOption();
        Product product = fullOptionNameKeyMap.get(fullOption);
        if(product != null){
            createOption.setProductId(product.getId());

            product.toOptions().forEach(optionDto -> {
                String fullOptionName = optionDto.getFullOptionName();
                CreateOptionDetail createOptionDetail = createOptionFullOptionMap.get(fullOptionName);
                if(createOptionDetail != null){
                    createOptionDetail.setOptionDetailId(optionDto.getOptionDetailId());
                }
            });
        }else{
            Map<OptionName, CreateOptionDetail> optionNameMap = createOption.getOptionNameMap();
            optionNameMap.forEach((optionName, createOptionDetail) -> {
                OptionDto optionDto = optionGroupNameMap.get(optionName);
                if(optionDto != null){
                    createOptionDetail.setOptionGroupId(optionDto.getOptionGroupId());
                }
            });
        }
    }

}