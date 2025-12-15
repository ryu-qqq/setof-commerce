package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.ProductGroupSearchV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductSkuV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupV2ApiRequest.UpdateProductSkuV2ApiRequest;
import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.productdescription.dto.command.DescriptionImageDto;
import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroup Admin V2 API Mapper
 *
 * <p>API Request DTO를 Application Command로 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductGroupAdminV2ApiMapper {

    /**
     * 등록 요청을 Command로 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public RegisterFullProductCommand toRegisterCommand(RegisterProductGroupV2ApiRequest request) {
        return new RegisterFullProductCommand(
                request.sellerId(),
                request.categoryId(),
                request.brandId(),
                request.name(),
                request.optionType(),
                request.regularPrice(),
                request.currentPrice(),
                request.shippingPolicyId(),
                request.refundPolicyId(),
                toProductSkuCommands(request.products()),
                toProductImageCommands(request.images()),
                toProductDescriptionCommand(request.description()),
                toProductNoticeCommand(request.notice()));
    }

    /**
     * 수정 요청을 Command로 변환
     *
     * @param productGroupId 상품그룹 ID
     * @param request API 요청
     * @return Application Command
     */
    public UpdateFullProductCommand toUpdateCommand(
            Long productGroupId, UpdateProductGroupV2ApiRequest request) {
        return new UpdateFullProductCommand(
                productGroupId,
                request.categoryId(),
                request.brandId(),
                request.name(),
                request.optionType(),
                request.regularPrice(),
                request.currentPrice(),
                request.status(),
                request.shippingPolicyId(),
                request.refundPolicyId(),
                toUpdateProductSkuCommands(request.products()),
                toUpdateProductImageCommands(request.images()),
                toUpdateProductDescriptionCommand(request.description()),
                toUpdateProductNoticeCommand(request.notice()));
    }

    /**
     * 상태 변경 요청을 Command로 변환
     *
     * @param productGroupId 상품그룹 ID
     * @param sellerId 셀러 ID
     * @param request API 요청
     * @return Application Command
     */
    public UpdateProductGroupStatusCommand toStatusCommand(
            Long productGroupId, Long sellerId, UpdateProductGroupStatusV2ApiRequest request) {
        return new UpdateProductGroupStatusCommand(productGroupId, sellerId, request.status());
    }

    /**
     * 검색 요청을 Query로 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public ProductGroupSearchQuery toSearchQuery(ProductGroupSearchV2ApiRequest request) {
        return new ProductGroupSearchQuery(
                request.sellerId(),
                request.categoryId(),
                request.brandId(),
                request.name(),
                request.status(),
                request.pageOrDefault(),
                request.sizeOrDefault());
    }

    // ========== Private Helper Methods ==========

    private List<ProductSkuCommandDto> toProductSkuCommands(List<ProductSkuV2ApiRequest> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(
                        p ->
                                new ProductSkuCommandDto(
                                        p.option1Name(),
                                        p.option1Value(),
                                        p.option2Name(),
                                        p.option2Value(),
                                        p.additionalPrice(),
                                        p.initialStock()))
                .toList();
    }

    private List<ProductSkuCommandDto> toUpdateProductSkuCommands(
            List<UpdateProductSkuV2ApiRequest> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(
                        p ->
                                new ProductSkuCommandDto(
                                        p.option1Name(),
                                        p.option1Value(),
                                        p.option2Name(),
                                        p.option2Value(),
                                        p.additionalPrice(),
                                        p.initialStock()))
                .toList();
    }

    private List<ProductImageCommandDto> toProductImageCommands(
            List<ProductImageV2ApiRequest> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(
                        i ->
                                new ProductImageCommandDto(
                                        null,
                                        i.imageType(),
                                        i.originUrl(),
                                        i.cdnUrl(),
                                        i.displayOrder()))
                .toList();
    }

    private List<ProductImageCommandDto> toUpdateProductImageCommands(
            List<UpdateProductImageV2ApiRequest> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(
                        i ->
                                new ProductImageCommandDto(
                                        i.id(),
                                        i.imageType(),
                                        i.originUrl(),
                                        i.cdnUrl(),
                                        i.displayOrder()))
                .toList();
    }

    private ProductDescriptionCommandDto toProductDescriptionCommand(
            ProductDescriptionV2ApiRequest description) {
        if (description == null) {
            return null;
        }
        List<DescriptionImageDto> images =
                description.images() != null
                        ? description.images().stream()
                                .map(
                                        i ->
                                                new DescriptionImageDto(
                                                        i.displayOrder(),
                                                        i.originUrl(),
                                                        i.cdnUrl()))
                                .toList()
                        : List.of();
        return new ProductDescriptionCommandDto(null, description.htmlContent(), images);
    }

    private ProductDescriptionCommandDto toUpdateProductDescriptionCommand(
            UpdateProductDescriptionV2ApiRequest description) {
        if (description == null) {
            return null;
        }
        List<DescriptionImageDto> images =
                description.images() != null
                        ? description.images().stream()
                                .map(
                                        i ->
                                                new DescriptionImageDto(
                                                        i.displayOrder(),
                                                        i.originUrl(),
                                                        i.cdnUrl()))
                                .toList()
                        : List.of();
        return new ProductDescriptionCommandDto(
                description.id(), description.htmlContent(), images);
    }

    private ProductNoticeCommandDto toProductNoticeCommand(ProductNoticeV2ApiRequest notice) {
        if (notice == null) {
            return null;
        }
        List<NoticeItemDto> items =
                notice.items() != null
                        ? notice.items().stream()
                                .map(
                                        i ->
                                                new NoticeItemDto(
                                                        i.fieldKey(),
                                                        i.fieldValue(),
                                                        i.displayOrder()))
                                .toList()
                        : List.of();
        return new ProductNoticeCommandDto(null, notice.templateId(), items);
    }

    private ProductNoticeCommandDto toUpdateProductNoticeCommand(
            UpdateProductNoticeV2ApiRequest notice) {
        if (notice == null) {
            return null;
        }
        List<NoticeItemDto> items =
                notice.items() != null
                        ? notice.items().stream()
                                .map(
                                        i ->
                                                new NoticeItemDto(
                                                        i.fieldKey(),
                                                        i.fieldValue(),
                                                        i.displayOrder()))
                                .toList()
                        : List.of();
        return new ProductNoticeCommandDto(notice.id(), notice.templateId(), items);
    }
}
