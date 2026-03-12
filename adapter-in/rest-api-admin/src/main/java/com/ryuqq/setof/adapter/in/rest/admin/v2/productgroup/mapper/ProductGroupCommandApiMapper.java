package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupBasicInfoApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupFullApiRequest;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupCommandApiMapper - 상품 그룹 Command API 변환 매퍼.
 *
 * <p>API Request와 Application Command 간 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>CQRS 분리: Command 전용 Mapper (QueryApiMapper와 분리).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupCommandApiMapper {

    /**
     * RegisterProductGroupApiRequest -> RegisterProductGroupCommand 변환.
     *
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterProductGroupCommand toCommand(RegisterProductGroupApiRequest request) {
        return new RegisterProductGroupCommand(
                request.productGroupId(),
                request.sellerId(),
                request.brandId(),
                request.categoryId(),
                request.shippingPolicyId(),
                request.refundPolicyId(),
                request.productGroupName(),
                resolveOptionType(request.optionType(), request.optionGroups()),
                request.regularPrice(),
                request.currentPrice(),
                request.images().stream()
                        .map(
                                img ->
                                        new RegisterProductGroupCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList(),
                request.optionGroups() != null
                        ? request.optionGroups().stream()
                                .map(
                                        group ->
                                                new RegisterProductGroupCommand.OptionGroupCommand(
                                                        group.optionGroupName(),
                                                        group.sortOrder(),
                                                        group.optionValues().stream()
                                                                .map(
                                                                        value ->
                                                                                new RegisterProductGroupCommand
                                                                                        .OptionValueCommand(
                                                                                        value
                                                                                                .optionValueName(),
                                                                                        value
                                                                                                .sortOrder()))
                                                                .toList()))
                                .toList()
                        : null,
                request.products().stream()
                        .map(
                                product ->
                                        new RegisterProductGroupCommand.ProductCommand(
                                                product.productId(),
                                                product.skuCode(),
                                                product.regularPrice(),
                                                product.currentPrice(),
                                                product.stockQuantity(),
                                                product.sortOrder(),
                                                product.selectedOptions().stream()
                                                        .map(
                                                                so ->
                                                                        new SelectedOption(
                                                                                so
                                                                                        .optionGroupName(),
                                                                                so
                                                                                        .optionValueName()))
                                                        .toList()))
                        .toList(),
                request.description() != null
                        ? new RegisterProductGroupCommand.DescriptionCommand(
                                request.description().content(),
                                request.description().descriptionImages() != null
                                        ? request.description().descriptionImages().stream()
                                                .map(
                                                        img ->
                                                                new RegisterProductGroupCommand
                                                                        .DescriptionImageCommand(
                                                                        img.imageUrl(),
                                                                        img.sortOrder()))
                                                .toList()
                                        : List.of())
                        : null,
                request.notice() != null
                        ? new RegisterProductGroupCommand.NoticeCommand(
                                request.notice().entries().stream()
                                        .map(
                                                entry ->
                                                        new RegisterProductGroupCommand
                                                                .NoticeEntryCommand(
                                                                entry.noticeFieldId(),
                                                                entry.fieldName(),
                                                                entry.fieldValue()))
                                        .toList())
                        : null);
    }

    /**
     * UpdateProductGroupFullApiRequest -> UpdateProductGroupFullCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 -> PathVariable에서 전달.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductGroupFullCommand toCommand(
            Long productGroupId, UpdateProductGroupFullApiRequest request) {
        return new UpdateProductGroupFullCommand(
                productGroupId,
                request.productGroupName(),
                request.brandId(),
                request.categoryId(),
                request.shippingPolicyId(),
                request.refundPolicyId(),
                resolveOptionType(request.optionType(), request.optionGroups()),
                request.regularPrice(),
                request.currentPrice(),
                request.images().stream()
                        .map(
                                img ->
                                        new UpdateProductGroupFullCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList(),
                request.optionGroups() != null
                        ? request.optionGroups().stream()
                                .map(
                                        group ->
                                                new UpdateProductGroupFullCommand
                                                        .OptionGroupCommand(
                                                        group.sellerOptionGroupId(),
                                                        group.optionGroupName(),
                                                        group.sortOrder(),
                                                        group.optionValues().stream()
                                                                .map(
                                                                        value ->
                                                                                new UpdateProductGroupFullCommand
                                                                                        .OptionValueCommand(
                                                                                        value
                                                                                                .sellerOptionValueId(),
                                                                                        value
                                                                                                .optionValueName(),
                                                                                        value
                                                                                                .sortOrder()))
                                                                .toList()))
                                .toList()
                        : null,
                request.products().stream()
                        .map(
                                product ->
                                        new UpdateProductGroupFullCommand.ProductCommand(
                                                product.productId(),
                                                product.skuCode(),
                                                product.regularPrice(),
                                                product.currentPrice(),
                                                product.stockQuantity(),
                                                product.sortOrder(),
                                                product.selectedOptions().stream()
                                                        .map(
                                                                so ->
                                                                        new SelectedOption(
                                                                                so
                                                                                        .optionGroupName(),
                                                                                so
                                                                                        .optionValueName()))
                                                        .toList()))
                        .toList(),
                request.description() != null
                        ? new UpdateProductGroupFullCommand.DescriptionCommand(
                                request.description().content(),
                                request.description().descriptionImages() != null
                                        ? request.description().descriptionImages().stream()
                                                .map(
                                                        img ->
                                                                new UpdateProductGroupFullCommand
                                                                        .DescriptionImageCommand(
                                                                        img.imageUrl(),
                                                                        img.sortOrder()))
                                                .toList()
                                        : List.of())
                        : null,
                request.notice() != null
                        ? new UpdateProductGroupFullCommand.NoticeCommand(
                                request.notice().entries().stream()
                                        .map(
                                                entry ->
                                                        new UpdateProductGroupFullCommand
                                                                .NoticeEntryCommand(
                                                                entry.noticeFieldId(),
                                                                entry.fieldName(),
                                                                entry.fieldValue()))
                                        .toList())
                        : null);
    }

    /**
     * UpdateProductGroupBasicInfoApiRequest -> UpdateProductGroupBasicInfoCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductGroupBasicInfoCommand toCommand(
            Long productGroupId, UpdateProductGroupBasicInfoApiRequest request) {
        return new UpdateProductGroupBasicInfoCommand(
                productGroupId,
                request.productGroupName(),
                request.brandId(),
                request.categoryId(),
                request.shippingPolicyId(),
                request.refundPolicyId());
    }

    /**
     * optionType을 결정합니다. 명시적으로 전달된 값이 있으면 그대로 사용하고, 없으면 optionGroups 수로 추론합니다.
     *
     * @param optionType 클라이언트 전달 값 (nullable)
     * @param optionGroups 옵션 그룹 목록 (nullable)
     * @return 결정된 optionType 문자열
     */
    private String resolveOptionType(String optionType, List<?> optionGroups) {
        if (optionType != null && !optionType.isBlank()) {
            return optionType;
        }
        if (optionGroups == null || optionGroups.isEmpty()) {
            return OptionType.NONE.name();
        }
        if (optionGroups.size() == 1) {
            return OptionType.SINGLE.name();
        }
        return OptionType.COMBINATION.name();
    }
}
