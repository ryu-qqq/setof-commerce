package com.ryuqq.setof.application.product.facade;

import com.ryuqq.setof.application.product.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.manager.query.ProductSkuReadManager;
import com.ryuqq.setof.application.productdescription.assembler.ProductDescriptionAssembler;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.manager.query.ProductDescriptionReadManager;
import com.ryuqq.setof.application.productimage.assembler.ProductImageAssembler;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.manager.query.ProductImageReadManager;
import com.ryuqq.setof.application.productnotice.assembler.ProductNoticeAssembler;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.application.productnotice.manager.query.ProductNoticeReadManager;
import com.ryuqq.setof.application.productstock.assembler.ProductStockAssembler;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.application.productstock.manager.query.ProductStockReadManager;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품 조회 Facade
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 + 재고 통합 조회를 조율합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductQueryFacade {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductSkuReadManager productSkuReadManager;
    private final ProductImageReadManager imageReadManager;
    private final ProductDescriptionReadManager descriptionReadManager;
    private final ProductNoticeReadManager noticeReadManager;
    private final ProductStockReadManager stockReadManager;

    private final ProductGroupAssembler productGroupAssembler;
    private final ProductImageAssembler imageAssembler;
    private final ProductDescriptionAssembler descriptionAssembler;
    private final ProductNoticeAssembler noticeAssembler;
    private final ProductStockAssembler stockAssembler;

    public ProductQueryFacade(
            ProductGroupReadManager productGroupReadManager,
            ProductSkuReadManager productSkuReadManager,
            ProductImageReadManager imageReadManager,
            ProductDescriptionReadManager descriptionReadManager,
            ProductNoticeReadManager noticeReadManager,
            ProductStockReadManager stockReadManager,
            ProductGroupAssembler productGroupAssembler,
            ProductImageAssembler imageAssembler,
            ProductDescriptionAssembler descriptionAssembler,
            ProductNoticeAssembler noticeAssembler,
            ProductStockAssembler stockAssembler) {
        this.productGroupReadManager = productGroupReadManager;
        this.productSkuReadManager = productSkuReadManager;
        this.imageReadManager = imageReadManager;
        this.descriptionReadManager = descriptionReadManager;
        this.noticeReadManager = noticeReadManager;
        this.stockReadManager = stockReadManager;
        this.productGroupAssembler = productGroupAssembler;
        this.imageAssembler = imageAssembler;
        this.descriptionAssembler = descriptionAssembler;
        this.noticeAssembler = noticeAssembler;
        this.stockAssembler = stockAssembler;
    }

    /**
     * 전체 상품 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 전체 상품 정보
     */
    public FullProductResponse getFullProduct(Long productGroupId) {
        // 1. ProductGroup 조회
        ProductGroup productGroup = productGroupReadManager.findById(productGroupId);

        // 2. Product(SKU) 목록 조회
        List<Product> products = productSkuReadManager.findByProductGroupId(productGroupId);

        // 3. Images 조회
        List<ProductImage> images = imageReadManager.findByProductGroupId(productGroupId);

        // 4. Description 조회 (Optional)
        ProductDescription description =
                descriptionReadManager.findByProductGroupId(productGroupId).orElse(null);

        // 5. Notice 조회 (Optional)
        ProductNotice notice = noticeReadManager.findByProductGroupId(productGroupId).orElse(null);

        // 6. Stocks 조회 (Product IDs로 조회)
        List<Long> productIds = products.stream().map(Product::getIdValue).toList();
        List<ProductStock> stocks =
                productIds.isEmpty() ? List.of() : stockReadManager.findByProductIds(productIds);

        // 7. Response 변환 및 조립
        return assembleResponse(productGroup, products, images, description, notice, stocks);
    }

    /** Response DTO 조립 */
    private FullProductResponse assembleResponse(
            ProductGroup productGroup,
            List<Product> products,
            List<ProductImage> images,
            ProductDescription description,
            ProductNotice notice,
            List<ProductStock> stocks) {

        ProductGroupResponse productGroupResponse =
                productGroupAssembler.toProductGroupResponse(productGroup, products);

        List<ProductResponse> productResponses =
                products.stream().map(productGroupAssembler::toProductResponse).toList();

        List<ProductImageResponse> imageResponses = imageAssembler.toResponses(images);

        ProductDescriptionResponse descriptionResponse =
                description != null ? descriptionAssembler.toResponse(description) : null;

        ProductNoticeResponse noticeResponse =
                notice != null ? noticeAssembler.toResponse(notice) : null;

        List<ProductStockResponse> stockResponses = stockAssembler.toResponses(stocks);

        return new FullProductResponse(
                productGroupResponse,
                productResponses,
                imageResponses,
                descriptionResponse,
                noticeResponse,
                stockResponses);
    }
}
