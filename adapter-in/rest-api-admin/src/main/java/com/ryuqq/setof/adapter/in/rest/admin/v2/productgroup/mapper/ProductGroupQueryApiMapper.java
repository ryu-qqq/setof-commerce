package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.common.util.DateTimeFormatUtils;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.DescriptionImageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.NonReturnableConditionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDescriptionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupImageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductNoticeApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductNoticeEntryApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductOptionMatrixApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.RefundPolicyApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ResolvedProductOptionApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.SellerOptionGroupApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.SellerOptionValueApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ShippingPolicyApiResponse;
import com.ryuqq.setof.application.product.dto.response.ProductDetailResult;
import com.ryuqq.setof.application.product.dto.response.ResolvedProductOptionResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductOptionMatrixResult;
import com.ryuqq.setof.application.productgroup.dto.response.SellerOptionGroupResult;
import com.ryuqq.setof.application.productgroup.dto.response.SellerOptionValueResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.ProductGroupDescriptionResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ProductGroupImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupQueryApiMapper - Admin 상품그룹 조회 API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result -> API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>API-DTO-005: 날짜 String 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupQueryApiMapper {

    // ==================== 상세 변환 ====================

    public ProductGroupDetailApiResponse toDetailResponse(
            ProductGroupDetailCompositeResult result) {
        List<ProductGroupImageApiResponse> images =
                result.images().stream().map(this::toImageResponse).toList();

        ProductOptionMatrixApiResponse matrix =
                toOptionMatrixResponse(result.optionProductMatrix());

        ShippingPolicyApiResponse shippingPolicy =
                result.shippingPolicy() != null
                        ? toShippingPolicyResponse(result.shippingPolicy())
                        : null;

        RefundPolicyApiResponse refundPolicy =
                result.refundPolicy() != null
                        ? toRefundPolicyResponse(result.refundPolicy())
                        : null;

        ProductGroupDescriptionApiResponse description =
                result.description() != null ? toDescriptionResponse(result.description()) : null;

        ProductNoticeApiResponse notice =
                result.productNotice() != null ? toNoticeResponse(result.productNotice()) : null;

        return new ProductGroupDetailApiResponse(
                result.id(),
                result.sellerId(),
                result.sellerName(),
                result.brandId(),
                result.brandName(),
                result.categoryId(),
                result.categoryName(),
                result.categoryPath(),
                result.productGroupName(),
                result.optionType(),
                result.status(),
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.updatedAt()),
                images,
                matrix,
                shippingPolicy,
                refundPolicy,
                description,
                notice);
    }

    private ProductGroupImageApiResponse toImageResponse(ProductGroupImageResult result) {
        return new ProductGroupImageApiResponse(
                result.id(), result.imageUrl(), result.imageType(), result.sortOrder());
    }

    private ProductOptionMatrixApiResponse toOptionMatrixResponse(
            ProductOptionMatrixResult result) {
        List<SellerOptionGroupApiResponse> optionGroups =
                result.optionGroups().stream().map(this::toOptionGroupResponse).toList();
        List<ProductDetailApiResponse> products =
                result.products().stream().map(this::toProductDetailResponse).toList();
        return new ProductOptionMatrixApiResponse(optionGroups, products);
    }

    private SellerOptionGroupApiResponse toOptionGroupResponse(SellerOptionGroupResult result) {
        List<SellerOptionValueApiResponse> values =
                result.optionValues().stream().map(this::toOptionValueResponse).toList();
        return new SellerOptionGroupApiResponse(
                result.id(), result.optionGroupName(), result.sortOrder(), values);
    }

    private SellerOptionValueApiResponse toOptionValueResponse(SellerOptionValueResult result) {
        return new SellerOptionValueApiResponse(
                result.id(),
                result.sellerOptionGroupId(),
                result.optionValueName(),
                result.sortOrder());
    }

    private ProductDetailApiResponse toProductDetailResponse(ProductDetailResult result) {
        List<ResolvedProductOptionApiResponse> options =
                result.options().stream().map(this::toResolvedOptionResponse).toList();
        return new ProductDetailApiResponse(
                result.id(),
                result.skuCode(),
                result.regularPrice(),
                result.currentPrice(),
                result.discountRate(),
                result.stockQuantity(),
                result.status(),
                result.sortOrder(),
                options,
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.updatedAt()));
    }

    private ResolvedProductOptionApiResponse toResolvedOptionResponse(
            ResolvedProductOptionResult result) {
        return new ResolvedProductOptionApiResponse(
                result.sellerOptionGroupId(),
                result.optionGroupName(),
                result.sellerOptionValueId(),
                result.optionValueName());
    }

    private ShippingPolicyApiResponse toShippingPolicyResponse(ShippingPolicyResult result) {
        return new ShippingPolicyApiResponse(
                result.policyId(),
                null,
                result.policyName(),
                result.defaultPolicy(),
                result.active(),
                result.shippingFeeType(),
                result.shippingFeeTypeDisplayName(),
                result.baseFee() != null ? result.baseFee().intValue() : 0,
                result.freeThreshold() != null ? result.freeThreshold().intValue() : null,
                0,
                0,
                0,
                0,
                0,
                0,
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.createdAt()));
    }

    private RefundPolicyApiResponse toRefundPolicyResponse(RefundPolicyResult result) {
        List<NonReturnableConditionApiResponse> conditions =
                result.nonReturnableConditions() != null
                        ? result.nonReturnableConditions().stream()
                                .map(
                                        c ->
                                                new NonReturnableConditionApiResponse(
                                                        c.code(), c.displayName()))
                                .toList()
                        : List.of();

        return new RefundPolicyApiResponse(
                result.policyId(),
                null,
                result.policyName(),
                result.defaultPolicy(),
                result.active(),
                result.returnPeriodDays(),
                result.exchangePeriodDays(),
                conditions,
                false,
                false,
                0,
                null,
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.createdAt()));
    }

    private ProductGroupDescriptionApiResponse toDescriptionResponse(
            ProductGroupDescriptionResult result) {
        List<DescriptionImageApiResponse> images =
                result.images().stream().map(this::toDescriptionImageResponse).toList();
        return new ProductGroupDescriptionApiResponse(
                result.id(), result.content(), result.cdnPath(), images);
    }

    private DescriptionImageApiResponse toDescriptionImageResponse(DescriptionImageResult result) {
        return new DescriptionImageApiResponse(result.id(), result.imageUrl(), result.sortOrder());
    }

    private ProductNoticeApiResponse toNoticeResponse(ProductNoticeResult result) {
        List<ProductNoticeEntryApiResponse> entries =
                result.entries().stream().map(this::toNoticeEntryResponse).toList();
        return new ProductNoticeApiResponse(
                result.id(),
                entries,
                DateTimeFormatUtils.formatIso8601(result.createdAt()),
                DateTimeFormatUtils.formatIso8601(result.updatedAt()));
    }

    private ProductNoticeEntryApiResponse toNoticeEntryResponse(ProductNoticeEntryResult result) {
        return new ProductNoticeEntryApiResponse(
                result.id(), result.noticeFieldId(), result.fieldValue());
    }
}
