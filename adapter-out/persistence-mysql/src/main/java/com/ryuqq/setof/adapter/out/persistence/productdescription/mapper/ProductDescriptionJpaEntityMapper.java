package com.ryuqq.setof.adapter.out.persistence.productdescription.mapper;

import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionJpaEntity;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productdescription.vo.ImageUrl;
import com.ryuqq.setof.domain.productdescription.vo.ProductDescriptionId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductDescriptionJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (메인 Entity만)
     *
     * @param domain ProductDescription 도메인
     * @return ProductDescriptionJpaEntity
     */
    public ProductDescriptionJpaEntity toEntity(ProductDescription domain) {
        return ProductDescriptionJpaEntity.of(
                domain.getIdValue(),
                domain.getProductGroupIdValue(),
                domain.getHtmlContentValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Domain 이미지 -> Entity 이미지 변환
     *
     * @param image DescriptionImage 도메인
     * @param productDescriptionId 상품설명 ID
     * @return ProductDescriptionImageJpaEntity
     */
    public ProductDescriptionImageJpaEntity toImageEntity(
            DescriptionImage image, Long productDescriptionId) {
        return ProductDescriptionImageJpaEntity.of(
                null, // 신규 생성 시 ID는 null
                productDescriptionId,
                image.displayOrder(),
                image.getOriginUrlValue(),
                image.getCdnUrlValue(),
                image.uploadedAt());
    }

    /**
     * Domain 이미지 목록 -> Entity 이미지 목록 변환
     *
     * @param images DescriptionImage 도메인 목록
     * @param productDescriptionId 상품설명 ID
     * @return ProductDescriptionImageJpaEntity 목록
     */
    public List<ProductDescriptionImageJpaEntity> toImageEntities(
            List<DescriptionImage> images, Long productDescriptionId) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream().map(image -> toImageEntity(image, productDescriptionId)).toList();
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ProductDescriptionJpaEntity
     * @param imageEntities 이미지 Entity 목록
     * @return ProductDescription 도메인
     */
    public ProductDescription toDomain(
            ProductDescriptionJpaEntity entity,
            List<ProductDescriptionImageJpaEntity> imageEntities) {
        List<DescriptionImage> images = toImageDomains(imageEntities);
        return ProductDescription.reconstitute(
                ProductDescriptionId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                entity.getHtmlContent() != null
                        ? HtmlContent.of(entity.getHtmlContent())
                        : HtmlContent.empty(),
                images,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Entity 이미지 목록 -> Domain 이미지 목록 변환
     *
     * @param imageEntities 이미지 Entity 목록
     * @return DescriptionImage 도메인 목록
     */
    private List<DescriptionImage> toImageDomains(
            List<ProductDescriptionImageJpaEntity> imageEntities) {
        if (imageEntities == null || imageEntities.isEmpty()) {
            return List.of();
        }
        return imageEntities.stream().map(this::toImageDomain).toList();
    }

    /**
     * Entity 이미지 -> Domain 이미지 변환
     *
     * @param entity 이미지 Entity
     * @return DescriptionImage 도메인
     */
    private DescriptionImage toImageDomain(ProductDescriptionImageJpaEntity entity) {
        return DescriptionImage.of(
                entity.getDisplayOrder(),
                ImageUrl.of(entity.getOriginUrl()),
                ImageUrl.of(entity.getCdnUrl()),
                entity.getUploadedAt());
    }
}
