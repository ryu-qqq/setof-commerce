package com.ryuqq.setof.application.product.dto.response;

import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.util.List;

/**
 * 전체 상품 조회 Response
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 + 재고 통합 조회 응답
 *
 * @param productGroup 상품그룹 정보
 * @param products 상품(SKU) 목록
 * @param images 이미지 목록
 * @param description 상세설명 (nullable)
 * @param notice 고시정보 (nullable)
 * @param stocks 재고 정보 목록
 * @author development-team
 * @since 1.0.0
 */
public record FullProductResponse(
        ProductGroupResponse productGroup,
        List<ProductResponse> products,
        List<ProductImageResponse> images,
        ProductDescriptionResponse description,
        ProductNoticeResponse notice,
        List<ProductStockResponse> stocks) {}
