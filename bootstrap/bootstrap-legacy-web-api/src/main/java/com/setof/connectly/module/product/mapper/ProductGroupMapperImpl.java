package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.group.ProductDto;
import com.setof.connectly.module.product.dto.group.ProductGroupDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductFetchDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import com.setof.connectly.module.product.dto.image.ProductImageDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ProductGroupMapperImpl implements ProductGroupMapper {

    @Override
    public ProductGroupResponse toProductGroupResponse(
            ProductGroupFetchDto productGroup, List<ProductCategoryDto> productCategoryList) {
        productGroup.setCategories(new LinkedHashSet<>(productCategoryList));

        ProductGroupDto productGroupDto = toProductGroupDto(productGroup);

        return ProductGroupResponse.builder()
                .productGroup(productGroupDto)
                .products(transProductDto(productGroup.getProducts()))
                .productNotices(productGroup.getProductNotices())
                .categories(productGroup.getCategories())
                .productGroupImages(sortProductImages(productGroup.getProductImages()))
                .detailDescription(productGroup.getDetailDescription())
                .build();
    }

    @Override
    public List<ProductGroupThumbnail> reOrderProductGroupThumbnail(
            List<Long> productGroupIds, List<ProductGroupThumbnail> productGroupThumbnails) {
        Map<Long, ProductGroupThumbnail> productGroupThumbnailMap =
                productGroupThumbnails.stream()
                        .collect(
                                Collectors.toMap(
                                        ProductGroupThumbnail::getProductGroupId,
                                        Function.identity()));

        LinkedList<ProductGroupThumbnail> orderedList = new LinkedList<>();

        productGroupIds.forEach(
                aLong -> {
                    ProductGroupThumbnail productGroupThumbnail =
                            productGroupThumbnailMap.get(aLong);
                    if (productGroupThumbnail != null) orderedList.add(productGroupThumbnail);
                });

        return orderedList;
    }

    private Set<ProductDto> transProductDto(List<ProductFetchDto> products) {
        Map<Long, Set<OptionDto>> groupedOptions = getGroupedOptions(products);
        Function<Long, ProductFetchDto> extractProductDetails = extractProductDetails(products);
        return getProducts(groupedOptions, extractProductDetails);
    }

    @NotNull
    private Function<Long, ProductFetchDto> extractProductDetails(List<ProductFetchDto> products) {
        return productId ->
                products.stream()
                        .filter(p -> p.getProductId() == productId)
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Product details not found for productId: "
                                                        + productId
                                                        + " in provided list."));
    }

    private Set<ProductImageDto> sortProductImages(Set<ProductImageDto> productImages) {
        Comparator<ProductImageDto> byImageType =
                Comparator.comparingInt(
                        img ->
                                img.getProductGroupImageType() == ProductGroupImageType.MAIN
                                        ? 0
                                        : 1);

        return productImages.stream()
                .sorted(byImageType)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<Long, Set<OptionDto>> getGroupedOptions(List<ProductFetchDto> products) {
        return products.stream()
                .collect(
                        Collectors.groupingBy(
                                ProductFetchDto::getProductId,
                                Collectors.mapping(
                                        p ->
                                                new OptionDto(
                                                        p.getOptionGroupId(),
                                                        p.getOptionDetailId(),
                                                        p.getOptionName(),
                                                        p.getOptionValue()),
                                        Collectors.toSet())));
    }

    private Set<ProductDto> getProducts(
            Map<Long, Set<OptionDto>> groupedOptions,
            Function<Long, ProductFetchDto> extractProductDetails) {
        return groupedOptions.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(
                        entry -> {
                            Long productId = entry.getKey();
                            Set<OptionDto> options =
                                    entry.getValue().stream()
                                            .filter(option -> option.getOptionGroupId() != 0)
                                            .collect(
                                                    Collectors.toCollection(
                                                            () ->
                                                                    new TreeSet<>(
                                                                            Comparator.comparing(
                                                                                    OptionDto
                                                                                            ::getOptionGroupId))));

                            if (!options.isEmpty()) {
                                options.addAll(entry.getValue());
                            }

                            ProductFetchDto productDetails = extractProductDetails.apply(productId);

                            return new ProductDto(
                                    productDetails.getProductId(),
                                    productDetails.getStockQuantity(),
                                    productDetails.getProductStatus(),
                                    getOptionName(options),
                                    options);
                        })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String getOptionName(Set<OptionDto> options) {
        return options.stream()
                .sorted(Comparator.comparing(OptionDto::getOptionGroupId))
                .map(OptionDto::getOptionValue)
                .collect(Collectors.joining(" "));
    }

    private ProductGroupDto toProductGroupDto(ProductGroupFetchDto productGroup) {
        return ProductGroupDto.builder()
                .productGroupId(productGroup.getProductGroupId())
                .productGroupName(productGroup.getProductGroupName())
                .sellerId(productGroup.getSellerId())
                .sellerName(productGroup.getSellerName())
                .categoryId(productGroup.getCategoryId())
                .optionType(productGroup.getOptionType())
                .brand(productGroup.getBrand())
                .price(productGroup.getPrice())
                .clothesDetailInfo(productGroup.getClothesDetail())
                .deliveryNotice(productGroup.getDeliveryNotice())
                .refundNotice(productGroup.getRefundNotice())
                .productGroupMainImageUrl(productGroup.getMainImageUrl())
                .productStatus(productGroup.getProductStatus())
                .reviewCount(productGroup.getProductReview().getReviewCount())
                .averageRating(productGroup.getProductReview().getAverageRating())
                .build();
    }
}
