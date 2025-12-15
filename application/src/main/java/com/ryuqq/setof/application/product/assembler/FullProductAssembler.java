package com.ryuqq.setof.application.product.assembler;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FullProduct Assembler
 *
 * <p>전체 상품 Response 조립을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FullProductAssembler {

    /**
     * 전체 상품 Response 조립
     *
     * @param productGroup 상품그룹 Response
     * @param products 상품(SKU) Response 목록
     * @param images 이미지 Response 목록
     * @param description 상세설명 Response (nullable)
     * @param notice 고시정보 Response (nullable)
     * @param stocks 재고 Response 목록
     * @return 전체 상품 Response
     */
    public FullProductResponse toFullProductResponse(
            ProductGroupResponse productGroup,
            List<ProductResponse> products,
            List<ProductImageResponse> images,
            ProductDescriptionResponse description,
            ProductNoticeResponse notice,
            List<ProductStockResponse> stocks) {

        return new FullProductResponse(
                productGroup,
                products != null ? products : List.of(),
                images != null ? images : List.of(),
                description,
                notice,
                stocks != null ? stocks : List.of());
    }

    /**
     * 상품그룹 정보만으로 기본 Response 생성
     *
     * @param productGroup 상품그룹 Response
     * @param products 상품(SKU) Response 목록
     * @return 기본 정보만 포함된 전체 상품 Response
     */
    public FullProductResponse toBasicFullProductResponse(
            ProductGroupResponse productGroup, List<ProductResponse> products) {
        return new FullProductResponse(
                productGroup,
                products != null ? products : List.of(),
                List.of(),
                null,
                null,
                List.of());
    }
}
