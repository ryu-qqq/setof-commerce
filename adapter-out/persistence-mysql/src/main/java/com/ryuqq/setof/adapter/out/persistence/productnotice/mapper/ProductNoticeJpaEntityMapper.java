package com.ryuqq.setof.adapter.out.persistence.productnotice.mapper;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticeJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (메인 Entity만)
     *
     * @param domain ProductNotice 도메인
     * @return ProductNoticeJpaEntity
     */
    public ProductNoticeJpaEntity toEntity(ProductNotice domain) {
        return ProductNoticeJpaEntity.of(
                domain.getIdValue(),
                domain.getProductGroupId(),
                domain.getTemplateIdValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Domain 항목 -> Entity 항목 변환
     *
     * @param item NoticeItem 도메인
     * @param productNoticeId 상품고시 ID
     * @return ProductNoticeItemJpaEntity
     */
    public ProductNoticeItemJpaEntity toItemEntity(NoticeItem item, Long productNoticeId) {
        return ProductNoticeItemJpaEntity.of(
                null, // 신규 생성 시 ID는 null
                productNoticeId,
                item.fieldKey(),
                item.fieldValue(),
                item.displayOrder());
    }

    /**
     * Domain 항목 목록 -> Entity 항목 목록 변환
     *
     * @param items NoticeItem 도메인 목록
     * @param productNoticeId 상품고시 ID
     * @return ProductNoticeItemJpaEntity 목록
     */
    public List<ProductNoticeItemJpaEntity> toItemEntities(
            List<NoticeItem> items, Long productNoticeId) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        return items.stream().map(item -> toItemEntity(item, productNoticeId)).toList();
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ProductNoticeJpaEntity
     * @param itemEntities 항목 Entity 목록
     * @return ProductNotice 도메인
     */
    public ProductNotice toDomain(
            ProductNoticeJpaEntity entity, List<ProductNoticeItemJpaEntity> itemEntities) {
        List<NoticeItem> items = toItemDomains(itemEntities);
        return ProductNotice.reconstitute(
                ProductNoticeId.of(entity.getId()),
                entity.getProductGroupId(),
                NoticeTemplateId.of(entity.getTemplateId()),
                items,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Entity 항목 목록 -> Domain 항목 목록 변환
     *
     * @param itemEntities 항목 Entity 목록
     * @return NoticeItem 도메인 목록
     */
    private List<NoticeItem> toItemDomains(List<ProductNoticeItemJpaEntity> itemEntities) {
        if (itemEntities == null || itemEntities.isEmpty()) {
            return List.of();
        }
        return itemEntities.stream().map(this::toItemDomain).toList();
    }

    /**
     * Entity 항목 -> Domain 항목 변환
     *
     * @param entity 항목 Entity
     * @return NoticeItem 도메인
     */
    private NoticeItem toItemDomain(ProductNoticeItemJpaEntity entity) {
        return NoticeItem.of(
                entity.getFieldKey(), entity.getFieldValue(), entity.getDisplayOrder());
    }
}
