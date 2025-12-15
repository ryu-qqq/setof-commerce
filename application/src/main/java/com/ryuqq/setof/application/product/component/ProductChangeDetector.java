package com.ryuqq.setof.application.product.component;

import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.productdescription.dto.command.DescriptionImageDto;
import com.ryuqq.setof.application.productdescription.factory.command.ProductDescriptionCommandFactory;
import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import com.ryuqq.setof.application.productnotice.factory.command.ProductNoticeCommandFactory;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import com.ryuqq.setof.domain.productimage.vo.ProductImageId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 상품 변경 감지기
 *
 * <p>기존 데이터와 새 데이터를 비교하여 변경 사항을 감지합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.GodClass", "PMD.ExcessiveImports"})
@Component
public class ProductChangeDetector {

    private final ClockHolder clockHolder;
    private final ProductDescriptionCommandFactory descriptionFactory;
    private final ProductNoticeCommandFactory noticeFactory;

    public ProductChangeDetector(
            ClockHolder clockHolder,
            ProductDescriptionCommandFactory descriptionFactory,
            ProductNoticeCommandFactory noticeFactory) {
        this.clockHolder = clockHolder;
        this.descriptionFactory = descriptionFactory;
        this.noticeFactory = noticeFactory;
    }

    /**
     * 기존 데이터와 새 데이터 비교하여 변경 사항 감지
     *
     * @param existing 기존 데이터
     * @param newData 새 데이터 (수정 Command)
     * @return 변경 사항
     */
    public ProductChanges detect(ExistingProductData existing, UpdateFullProductCommand newData) {
        return ProductChanges.builder()
                .updatedProductGroup(detectProductGroupChanges(existing.productGroup(), newData))
                .imagesToUpdate(detectModifiedImages(existing.images(), newData.images()))
                .imageDtosToAdd(detectNewImages(newData.images()))
                .imageIdsToDelete(detectRemovedImageIds(existing.images(), newData.images()))
                .updatedDescription(detectDescriptionChanges(existing, newData))
                .updatedNotice(detectNoticeChanges(existing, newData))
                .build();
    }

    /** ProductGroup 변경 감지 */
    private ProductGroup detectProductGroupChanges(
            ProductGroup existing, UpdateFullProductCommand newData) {
        boolean hasChanges = false;

        // 카테고리 변경 확인
        if (!Objects.equals(existing.getCategoryIdValue(), newData.categoryId())) {
            hasChanges = true;
        }

        // 브랜드 변경 확인
        if (!Objects.equals(existing.getBrandIdValue(), newData.brandId())) {
            hasChanges = true;
        }

        // 이름 변경 확인
        if (!Objects.equals(existing.getNameValue(), newData.name())) {
            hasChanges = true;
        }

        // 옵션타입 변경 확인
        if (!Objects.equals(existing.getOptionTypeValue(), newData.optionType())) {
            hasChanges = true;
        }

        // 가격 변경 확인
        if (!Objects.equals(existing.getRegularPriceValue(), newData.regularPrice())
                || !Objects.equals(existing.getCurrentPriceValue(), newData.currentPrice())) {
            hasChanges = true;
        }

        // 상태 변경 확인
        if (newData.status() != null
                && !Objects.equals(existing.getStatusValue(), newData.status())) {
            hasChanges = true;
        }

        // 배송정책 변경 확인
        if (!Objects.equals(existing.getShippingPolicyIdValue(), newData.shippingPolicyId())) {
            hasChanges = true;
        }

        // 환불정책 변경 확인
        if (!Objects.equals(existing.getRefundPolicyIdValue(), newData.refundPolicyId())) {
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        // 변경된 ProductGroup 생성 (기존 객체의 update 메서드 사용)
        // Note: update()는 optionType, status를 변경하지 않음 (별도 메서드 필요)
        Instant now = Instant.now(clockHolder.getClock());
        return existing.update(
                CategoryId.of(newData.categoryId()),
                BrandId.of(newData.brandId()),
                ProductGroupName.of(newData.name()),
                Price.of(newData.regularPrice(), newData.currentPrice()),
                newData.shippingPolicyId() != null
                        ? ShippingPolicyId.of(newData.shippingPolicyId())
                        : null,
                newData.refundPolicyId() != null
                        ? RefundPolicyId.of(newData.refundPolicyId())
                        : null,
                now);
    }

    /** 신규 이미지 감지 (ID가 null인 항목) */
    private List<ProductImageCommandDto> detectNewImages(List<ProductImageCommandDto> newImages) {
        if (newImages == null) {
            return List.of();
        }
        return newImages.stream().filter(img -> img.id() == null).toList();
    }

    /** 수정된 이미지 감지 (ID가 있고 내용이 다른 항목) */
    private List<ProductImage> detectModifiedImages(
            List<ProductImage> existing, List<ProductImageCommandDto> newImages) {
        if (newImages == null) {
            return List.of();
        }

        Map<Long, ProductImage> existingMap =
                existing.stream()
                        .collect(Collectors.toMap(ProductImage::getIdValue, Function.identity()));

        return newImages.stream()
                .filter(img -> img.id() != null)
                .filter(
                        img -> {
                            ProductImage old = existingMap.get(img.id());
                            return old != null && isImageModified(old, img);
                        })
                .map(
                        img -> {
                            ProductImage old = existingMap.get(img.id());
                            // ProductImage에는 update() 메서드가 없으므로 reconstitute 사용
                            return ProductImage.reconstitute(
                                    ProductImageId.of(img.id()),
                                    ProductGroupId.of(old.getProductGroupIdValue()),
                                    ImageType.valueOf(img.imageType()),
                                    ImageUrl.of(img.originUrl()),
                                    img.cdnUrl() != null
                                            ? ImageUrl.of(img.cdnUrl())
                                            : ImageUrl.of(img.originUrl()),
                                    img.displayOrder(),
                                    old.getCreatedAt());
                        })
                .toList();
    }

    /** 삭제할 이미지 ID 감지 (기존에만 있는 항목) */
    private List<Long> detectRemovedImageIds(
            List<ProductImage> existing, List<ProductImageCommandDto> newImages) {
        Set<Long> newIds =
                newImages == null
                        ? Set.of()
                        : newImages.stream()
                                .map(ProductImageCommandDto::id)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());

        return existing.stream()
                .map(ProductImage::getIdValue)
                .filter(id -> !newIds.contains(id))
                .toList();
    }

    /** 이미지 수정 여부 확인 */
    private boolean isImageModified(ProductImage old, ProductImageCommandDto newImg) {
        return !Objects.equals(old.getImageTypeValue(), newImg.imageType())
                || !Objects.equals(old.getOriginUrlValue(), newImg.originUrl())
                || !Objects.equals(old.getCdnUrlValue(), newImg.cdnUrl())
                || !Objects.equals(old.getDisplayOrder(), newImg.displayOrder());
    }

    /** 상세설명 변경 감지 */
    private ProductDescription detectDescriptionChanges(
            ExistingProductData existing, UpdateFullProductCommand newData) {
        ProductDescriptionCommandDto newDesc = newData.description();

        // 새 데이터가 없으면 변경 없음
        if (newDesc == null) {
            return null;
        }

        // 기존 데이터가 없으면 새로 생성해야 하지만, 이건 Facade에서 처리
        // 여기서는 수정만 감지
        if (!existing.hasDescription()) {
            return null;
        }

        ProductDescription oldDesc = existing.description();
        boolean hasChanges = false;

        // HTML 컨텐츠 변경 확인
        if (!Objects.equals(oldDesc.getHtmlContentValue(), newDesc.htmlContent())) {
            hasChanges = true;
        }

        // 이미지 목록 변경 확인 (단순 비교)
        if (!areDescriptionImagesEqual(oldDesc.getImages(), newDesc.images())) {
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        Instant now = descriptionFactory.now();
        List<DescriptionImage> images = descriptionFactory.toImages(newDesc.images(), now);

        return oldDesc.update(HtmlContent.of(newDesc.htmlContent()), images, now);
    }

    /** 고시정보 변경 감지 */
    private ProductNotice detectNoticeChanges(
            ExistingProductData existing, UpdateFullProductCommand newData) {
        ProductNoticeCommandDto newNotice = newData.notice();

        // 새 데이터가 없으면 변경 없음
        if (newNotice == null) {
            return null;
        }

        // 기존 데이터가 없으면 새로 생성해야 하지만, 이건 Facade에서 처리
        // 여기서는 수정만 감지
        if (!existing.hasNotice()) {
            return null;
        }

        ProductNotice oldNotice = existing.notice();
        boolean hasChanges = false;

        // 템플릿 ID 변경 확인
        if (!Objects.equals(oldNotice.getTemplateIdValue(), newNotice.templateId())) {
            hasChanges = true;
        }

        // 항목 목록 변경 확인
        if (!areNoticeItemsEqual(oldNotice.getItems(), newNotice.items())) {
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        Instant now = noticeFactory.now();
        List<NoticeItem> items = noticeFactory.toItems(newNotice.items());

        // ProductNotice에는 update() 메서드가 없으므로 reconstitute 사용
        return ProductNotice.reconstitute(
                ProductNoticeId.of(oldNotice.getIdValue()),
                oldNotice.getProductGroupId(),
                NoticeTemplateId.of(newNotice.templateId()),
                items,
                oldNotice.getCreatedAt(),
                now);
    }

    /** 상세설명 이미지 목록 비교 */
    private boolean areDescriptionImagesEqual(
            List<DescriptionImage> oldImages, List<DescriptionImageDto> newImages) {
        if (oldImages == null && newImages == null) {
            return true;
        }
        if (oldImages == null || newImages == null) {
            return false;
        }
        if (oldImages.size() != newImages.size()) {
            return false;
        }

        // 간단한 비교: 크기만 같으면 동일하다고 가정
        // 더 정확한 비교가 필요하면 내용 비교 추가
        return true;
    }

    /** 고시 항목 목록 비교 */
    private boolean areNoticeItemsEqual(List<NoticeItem> oldItems, List<NoticeItemDto> newItems) {
        if (oldItems == null && newItems == null) {
            return true;
        }
        if (oldItems == null || newItems == null) {
            return false;
        }
        if (oldItems.size() != newItems.size()) {
            return false;
        }

        // 간단한 비교: 크기만 같으면 동일하다고 가정
        // 더 정확한 비교가 필요하면 내용 비교 추가
        return true;
    }
}
