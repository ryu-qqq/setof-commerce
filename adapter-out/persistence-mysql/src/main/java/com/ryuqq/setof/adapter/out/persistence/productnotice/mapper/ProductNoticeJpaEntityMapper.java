package com.ryuqq.setof.adapter.out.persistence.productnotice.mapper;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeEntryId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeJpaEntityMapper - 상품고시 Entity-Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain ProductNotice 도메인 객체
     * @return ProductNoticeJpaEntity
     */
    public ProductNoticeJpaEntity toEntity(ProductNotice domain) {
        return ProductNoticeJpaEntity.create(
                domain.idValue(),
                domain.productGroupIdValue(),
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * ProductNoticeEntry Domain → Entry Entity 변환.
     *
     * @param entry ProductNoticeEntry 도메인 객체
     * @return ProductNoticeEntryJpaEntity
     */
    public ProductNoticeEntryJpaEntity toEntryEntity(ProductNoticeEntry entry) {
        return ProductNoticeEntryJpaEntity.create(
                entry.idValue(),
                entry.productNoticeIdValue(),
                entry.noticeFieldIdValue(),
                entry.fieldName(),
                entry.fieldValueText(),
                entry.sortOrder());
    }

    /**
     * Entity + Entry Entities → Domain 변환.
     *
     * @param entity ProductNoticeJpaEntity
     * @param entryEntities ProductNoticeEntryJpaEntity 목록
     * @return ProductNotice 도메인 객체
     */
    public ProductNotice toDomain(
            ProductNoticeJpaEntity entity, List<ProductNoticeEntryJpaEntity> entryEntities) {
        List<ProductNoticeEntry> entries = entryEntities.stream().map(this::toEntryDomain).toList();
        return ProductNotice.reconstitute(
                ProductNoticeId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                entries,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Entry Entity → ProductNoticeEntry Domain 변환.
     *
     * @param entryEntity ProductNoticeEntryJpaEntity
     * @return ProductNoticeEntry 도메인 객체
     */
    public ProductNoticeEntry toEntryDomain(ProductNoticeEntryJpaEntity entryEntity) {
        return ProductNoticeEntry.reconstitute(
                ProductNoticeEntryId.of(entryEntity.getId()),
                ProductNoticeId.of(entryEntity.getProductNoticeId()),
                NoticeFieldId.of(entryEntity.getNoticeFieldId()),
                NoticeFieldValue.of(entryEntity.getFieldName(), entryEntity.getFieldValue()),
                entryEntity.getSortOrder());
    }
}
