package com.ryuqq.setof.application.product.assembler;

import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand.RegisterProductCommand;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Money;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroup Assembler
 *
 * <p>Command DTO와 Domain 객체, Response DTO 간 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductGroupAssembler {

    /**
     * RegisterProductGroupCommand를 ProductGroup 도메인으로 변환
     *
     * @param command 등록 커맨드
     * @param now 현재 시각
     * @return ProductGroup 도메인 객체
     */
    public ProductGroup toDomain(RegisterProductGroupCommand command, Instant now) {
        SellerId sellerId = SellerId.of(command.sellerId());
        CategoryId categoryId = CategoryId.of(command.categoryId());
        BrandId brandId = BrandId.of(command.brandId());
        ProductGroupName name = ProductGroupName.of(command.name());
        OptionType optionType = OptionType.valueOf(command.optionType());
        Price price = Price.of(command.regularPrice(), command.currentPrice());

        ShippingPolicyId shippingPolicyId =
                command.shippingPolicyId() != null
                        ? ShippingPolicyId.of(command.shippingPolicyId())
                        : null;
        RefundPolicyId refundPolicyId =
                command.refundPolicyId() != null
                        ? RefundPolicyId.of(command.refundPolicyId())
                        : null;

        return ProductGroup.create(
                sellerId,
                categoryId,
                brandId,
                name,
                optionType,
                price,
                shippingPolicyId,
                refundPolicyId,
                now);
    }

    /**
     * RegisterProductCommand를 Product 도메인으로 변환
     *
     * @param productGroupId 상품그룹 ID
     * @param optionType 옵션 타입
     * @param command 상품 등록 커맨드
     * @param now 현재 시각
     * @return Product 도메인 객체
     */
    public Product toProductDomain(
            Long productGroupId,
            OptionType optionType,
            RegisterProductCommand command,
            Instant now) {

        ProductGroupId pgId = ProductGroupId.of(productGroupId);
        Money additionalPrice =
                command.additionalPrice() != null
                        ? Money.of(command.additionalPrice())
                        : Money.zero();

        return switch (optionType) {
            case SINGLE -> Product.createSingle(pgId, additionalPrice, now);
            case ONE_LEVEL ->
                    Product.createOneLevel(
                            pgId,
                            command.option1Name(),
                            command.option1Value(),
                            additionalPrice,
                            now);
            case TWO_LEVEL ->
                    Product.createTwoLevel(
                            pgId,
                            command.option1Name(),
                            command.option1Value(),
                            command.option2Name(),
                            command.option2Value(),
                            additionalPrice,
                            now);
        };
    }

    /**
     * ProductGroup 도메인을 ProductGroupResponse로 변환
     *
     * @param productGroup ProductGroup 도메인 객체
     * @param products 소속 Product 목록
     * @return ProductGroupResponse
     */
    public ProductGroupResponse toProductGroupResponse(
            ProductGroup productGroup, List<Product> products) {
        List<ProductResponse> productResponses =
                products.stream().map(this::toProductResponse).toList();

        return ProductGroupResponse.of(
                productGroup.getIdValue(),
                productGroup.getSellerIdValue(),
                productGroup.getCategoryIdValue(),
                productGroup.getBrandIdValue(),
                productGroup.getNameValue(),
                productGroup.getOptionTypeValue(),
                productGroup.getRegularPriceValue(),
                productGroup.getCurrentPriceValue(),
                productGroup.getStatusValue(),
                productGroup.getShippingPolicyIdValue(),
                productGroup.getRefundPolicyIdValue(),
                productResponses);
    }

    /**
     * Product 도메인을 ProductResponse로 변환
     *
     * @param product Product 도메인 객체
     * @return ProductResponse
     */
    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.of(
                product.getIdValue(),
                product.getProductGroupIdValue(),
                product.getOptionTypeValue(),
                product.getOption1Name(),
                product.getOption1Value(),
                product.getOption2Name(),
                product.getOption2Value(),
                product.getAdditionalPriceValue(),
                product.isSoldOut(),
                product.isDisplayed());
    }

    /**
     * ProductGroup 도메인을 ProductGroupSummaryResponse로 변환
     *
     * @param productGroup ProductGroup 도메인 객체
     * @param productCount 상품(SKU) 개수
     * @return ProductGroupSummaryResponse
     */
    public ProductGroupSummaryResponse toProductGroupSummaryResponse(
            ProductGroup productGroup, int productCount) {
        return ProductGroupSummaryResponse.of(
                productGroup.getIdValue(),
                productGroup.getSellerIdValue(),
                productGroup.getNameValue(),
                productGroup.getOptionTypeValue(),
                productGroup.getCurrentPriceValue(),
                productGroup.getStatusValue(),
                productCount);
    }

    /**
     * ProductGroup 도메인 목록을 ProductGroupSummaryResponse 목록으로 변환
     *
     * <p>productCount는 별도로 조회해야 하므로 0으로 설정
     *
     * @param productGroups ProductGroup 도메인 목록
     * @return ProductGroupSummaryResponse 목록
     */
    public List<ProductGroupSummaryResponse> toProductGroupSummaryResponses(
            List<ProductGroup> productGroups) {
        return productGroups.stream().map(pg -> toProductGroupSummaryResponse(pg, 0)).toList();
    }
}
