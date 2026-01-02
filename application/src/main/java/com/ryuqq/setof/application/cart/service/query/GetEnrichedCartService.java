package com.ryuqq.setof.application.cart.service.query;

import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.cart.dto.response.CartItemResponse;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartItemResponse;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartItemResponse.CategoryInfo;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartResponse;
import com.ryuqq.setof.application.cart.port.in.query.GetCartUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetEnrichedCartUseCase;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse.BreadcrumbItem;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryPathUseCase;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.application.productstock.port.out.query.ProductStockQueryPort;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Enriched 장바구니 조회 Service
 *
 * <p>상품 상세 정보가 포함된 장바구니를 조회합니다.
 *
 * <p>조회 순서:
 *
 * <ol>
 *   <li>장바구니 기본 정보 조회
 *   <li>상품그룹 정보 일괄 조회 (상품명, 브랜드ID, 카테고리ID, 가격 등)
 *   <li>브랜드 정보 일괄 조회 (브랜드명)
 *   <li>셀러 정보 일괄 조회 (셀러명)
 *   <li>상품 이미지 일괄 조회 (대표 이미지)
 *   <li>재고 정보 일괄 조회 (재고 수량)
 *   <li>카테고리 경로 일괄 조회 (breadcrumb)
 *   <li>정보 조합
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetEnrichedCartService implements GetEnrichedCartUseCase {

    private static final String MAIN_IMAGE_TYPE = "MAIN";

    private final GetCartUseCase getCartUseCase;
    private final GetProductGroupUseCase getProductGroupUseCase;
    private final GetBrandUseCase getBrandUseCase;
    private final GetSellerUseCase getSellerUseCase;
    private final GetProductImageUseCase getProductImageUseCase;
    private final ProductStockQueryPort productStockQueryPort;
    private final GetCategoryPathUseCase getCategoryPathUseCase;

    public GetEnrichedCartService(
            GetCartUseCase getCartUseCase,
            GetProductGroupUseCase getProductGroupUseCase,
            GetBrandUseCase getBrandUseCase,
            GetSellerUseCase getSellerUseCase,
            GetProductImageUseCase getProductImageUseCase,
            ProductStockQueryPort productStockQueryPort,
            GetCategoryPathUseCase getCategoryPathUseCase) {
        this.getCartUseCase = getCartUseCase;
        this.getProductGroupUseCase = getProductGroupUseCase;
        this.getBrandUseCase = getBrandUseCase;
        this.getSellerUseCase = getSellerUseCase;
        this.getProductImageUseCase = getProductImageUseCase;
        this.productStockQueryPort = productStockQueryPort;
        this.getCategoryPathUseCase = getCategoryPathUseCase;
    }

    @Override
    public EnrichedCartResponse getEnrichedCart(UUID memberId) {
        CartResponse cartResponse = getCartUseCase.getCart(memberId);
        List<CartItemResponse> items = cartResponse.items();

        if (items.isEmpty()) {
            return EnrichedCartResponse.from(cartResponse, Collections.emptyList());
        }

        Map<Long, ProductGroupResponse> productGroupMap = fetchProductGroups(items);
        Map<Long, BrandResponse> brandMap = fetchBrands(productGroupMap);
        Map<Long, SellerResponse> sellerMap = fetchSellers(items);
        Map<Long, String> imageMap = fetchMainImages(items);
        Map<Long, Integer> stockMap = fetchStocks(items);
        Map<Long, List<CategoryInfo>> categoryMap = fetchCategories(productGroupMap);

        List<EnrichedCartItemResponse> enrichedItems =
                items.stream()
                        .map(
                                item ->
                                        enrichItem(
                                                item,
                                                productGroupMap,
                                                brandMap,
                                                sellerMap,
                                                imageMap,
                                                stockMap,
                                                categoryMap))
                        .toList();

        return EnrichedCartResponse.from(cartResponse, enrichedItems);
    }

    private Map<Long, ProductGroupResponse> fetchProductGroups(List<CartItemResponse> items) {
        Set<Long> productGroupIds =
                items.stream().map(CartItemResponse::productGroupId).collect(Collectors.toSet());

        Map<Long, ProductGroupResponse> result = new HashMap<>();
        for (Long productGroupId : productGroupIds) {
            try {
                ProductGroupResponse response = getProductGroupUseCase.execute(productGroupId);
                result.put(productGroupId, response);
            } catch (Exception e) {
                // 조회 실패 시 무시 (기본값 사용)
            }
        }
        return result;
    }

    private Map<Long, BrandResponse> fetchBrands(Map<Long, ProductGroupResponse> productGroupMap) {
        Set<Long> brandIds =
                productGroupMap.values().stream()
                        .map(ProductGroupResponse::brandId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet());

        Map<Long, BrandResponse> result = new HashMap<>();
        for (Long brandId : brandIds) {
            try {
                BrandResponse response = getBrandUseCase.execute(brandId);
                result.put(brandId, response);
            } catch (Exception e) {
                // 조회 실패 시 무시 (기본값 사용)
            }
        }
        return result;
    }

    private Map<Long, SellerResponse> fetchSellers(List<CartItemResponse> items) {
        Set<Long> sellerIds =
                items.stream().map(CartItemResponse::sellerId).collect(Collectors.toSet());

        Map<Long, SellerResponse> result = new HashMap<>();
        for (Long sellerId : sellerIds) {
            try {
                SellerResponse response = getSellerUseCase.execute(sellerId);
                result.put(sellerId, response);
            } catch (Exception e) {
                // 조회 실패 시 무시 (기본값 사용)
            }
        }
        return result;
    }

    private Map<Long, String> fetchMainImages(List<CartItemResponse> items) {
        Set<Long> productGroupIds =
                items.stream().map(CartItemResponse::productGroupId).collect(Collectors.toSet());

        Map<Long, String> result = new HashMap<>();
        for (Long productGroupId : productGroupIds) {
            try {
                List<ProductImageResponse> images =
                        getProductImageUseCase.getByProductGroupId(productGroupId);
                images.stream()
                        .filter(img -> MAIN_IMAGE_TYPE.equals(img.imageType()))
                        .findFirst()
                        .or(() -> images.stream().findFirst())
                        .ifPresent(img -> result.put(productGroupId, img.cdnUrl()));
            } catch (Exception e) {
                // 조회 실패 시 무시 (기본값 사용)
            }
        }
        return result;
    }

    private Map<Long, Integer> fetchStocks(List<CartItemResponse> items) {
        List<Long> productIds = items.stream().map(CartItemResponse::productId).distinct().toList();

        List<ProductStock> stocks = productStockQueryPort.findByProductIds(productIds);

        return stocks.stream()
                .collect(
                        Collectors.toMap(
                                ProductStock::getProductIdValue,
                                ProductStock::getQuantityValue,
                                (existing, replacement) -> existing));
    }

    private Map<Long, List<CategoryInfo>> fetchCategories(
            Map<Long, ProductGroupResponse> productGroupMap) {
        Set<Long> categoryIds =
                productGroupMap.values().stream()
                        .map(ProductGroupResponse::categoryId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet());

        Map<Long, List<CategoryInfo>> result = new HashMap<>();
        for (Long categoryId : categoryIds) {
            try {
                CategoryPathResponse pathResponse =
                        getCategoryPathUseCase.getCategoryPath(categoryId);
                List<CategoryInfo> categoryInfos =
                        pathResponse.breadcrumbs().stream().map(this::toCategoryInfo).toList();
                result.put(categoryId, categoryInfos);
            } catch (Exception e) {
                // 조회 실패 시 무시 (기본값 사용)
            }
        }
        return result;
    }

    private CategoryInfo toCategoryInfo(BreadcrumbItem item) {
        return new CategoryInfo(item.id(), item.nameKo(), item.depth());
    }

    private EnrichedCartItemResponse enrichItem(
            CartItemResponse item,
            Map<Long, ProductGroupResponse> productGroupMap,
            Map<Long, BrandResponse> brandMap,
            Map<Long, SellerResponse> sellerMap,
            Map<Long, String> imageMap,
            Map<Long, Integer> stockMap,
            Map<Long, List<CategoryInfo>> categoryMap) {

        ProductGroupResponse productGroup = productGroupMap.get(item.productGroupId());
        String productGroupName = productGroup != null ? productGroup.name() : null;
        Long brandId = productGroup != null ? productGroup.brandId() : null;
        Long categoryId = productGroup != null ? productGroup.categoryId() : null;
        BigDecimal regularPrice = productGroup != null ? productGroup.regularPrice() : null;
        BigDecimal salePrice = productGroup != null ? productGroup.currentPrice() : null;

        BrandResponse brand = brandId != null ? brandMap.get(brandId) : null;
        String brandName = brand != null ? brand.nameKo() : null;

        SellerResponse seller = sellerMap.get(item.sellerId());
        String sellerName = seller != null ? seller.sellerName() : null;

        String optionValue = extractOptionValue(productGroup, item.productId());
        String imageUrl = imageMap.get(item.productGroupId());
        Integer stockQuantity = stockMap.get(item.productId());
        boolean soldOut = stockQuantity != null && stockQuantity <= 0;

        int discountRate = calculateDiscountRate(regularPrice, salePrice);

        List<CategoryInfo> categories =
                categoryId != null
                        ? categoryMap.getOrDefault(categoryId, Collections.emptyList())
                        : Collections.emptyList();

        return EnrichedCartItemResponse.from(
                item,
                productGroupName,
                brandId,
                brandName,
                sellerName,
                optionValue,
                imageUrl,
                stockQuantity,
                soldOut,
                regularPrice,
                salePrice,
                discountRate,
                categories);
    }

    private String extractOptionValue(ProductGroupResponse productGroup, Long productId) {
        if (productGroup == null || productGroup.products() == null) {
            return null;
        }

        Optional<ProductResponse> product =
                productGroup.products().stream()
                        .filter(p -> p.productId().equals(productId))
                        .findFirst();

        if (product.isEmpty()) {
            return null;
        }

        ProductResponse p = product.get();
        StringBuilder sb = new StringBuilder();

        if (p.option1Value() != null && !p.option1Value().isEmpty()) {
            sb.append(p.option1Value());
        }
        if (p.option2Value() != null && !p.option2Value().isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append(" / ");
            }
            sb.append(p.option2Value());
        }

        return sb.isEmpty() ? null : sb.toString();
    }

    private int calculateDiscountRate(BigDecimal regularPrice, BigDecimal salePrice) {
        if (regularPrice == null
                || salePrice == null
                || regularPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        if (regularPrice.compareTo(salePrice) <= 0) {
            return 0;
        }

        BigDecimal discount = regularPrice.subtract(salePrice);
        BigDecimal rate =
                discount.multiply(BigDecimal.valueOf(100))
                        .divide(regularPrice, 0, RoundingMode.DOWN);

        return rate.intValue();
    }
}
