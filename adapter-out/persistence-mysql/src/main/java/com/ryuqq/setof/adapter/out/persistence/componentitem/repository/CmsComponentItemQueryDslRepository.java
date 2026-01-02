package com.ryuqq.setof.adapter.out.persistence.componentitem.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.CmsComponentItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.QCmsComponentItemJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CmsComponentItemQueryDslRepository - ComponentItem QueryDSL Repository
 *
 * <p>QueryDSL 기반 동적 쿼리 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class CmsComponentItemQueryDslRepository {

    private static final String STATUS_ACTIVE = "ACTIVE";

    private final JPAQueryFactory queryFactory;
    private final QCmsComponentItemJpaEntity componentItem =
            QCmsComponentItemJpaEntity.cmsComponentItemJpaEntity;

    public CmsComponentItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 컴포넌트 아이템 조회
     *
     * @param id 컴포넌트 아이템 ID
     * @return 컴포넌트 아이템 Entity
     */
    public Optional<CmsComponentItemJpaEntity> findById(Long id) {
        CmsComponentItemJpaEntity result =
                queryFactory.selectFrom(componentItem).where(componentItem.id.eq(id)).fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * Component ID로 활성 아이템 목록 조회 (노출 순서 정렬)
     *
     * @param componentId Component ID
     * @return 활성 ComponentItem 목록
     */
    public List<CmsComponentItemJpaEntity> findActiveByComponentId(Long componentId) {
        return queryFactory
                .selectFrom(componentItem)
                .where(
                        componentItem.componentId.eq(componentId),
                        componentItem.status.eq(STATUS_ACTIVE),
                        componentItem.deletedAt.isNull())
                .orderBy(componentItem.displayOrder.asc())
                .fetch();
    }

    /**
     * Component ID로 전체 아이템 목록 조회 (삭제 제외)
     *
     * @param componentId Component ID
     * @return ComponentItem 목록
     */
    public List<CmsComponentItemJpaEntity> findAllByComponentId(Long componentId) {
        return queryFactory
                .selectFrom(componentItem)
                .where(componentItem.componentId.eq(componentId), componentItem.deletedAt.isNull())
                .orderBy(componentItem.displayOrder.asc())
                .fetch();
    }

    /**
     * 여러 Component ID로 활성 아이템 목록 조회
     *
     * @param componentIds Component ID 목록
     * @return 활성 ComponentItem 목록
     */
    public List<CmsComponentItemJpaEntity> findActiveByComponentIds(List<Long> componentIds) {
        return queryFactory
                .selectFrom(componentItem)
                .where(
                        componentItem.componentId.in(componentIds),
                        componentItem.status.eq(STATUS_ACTIVE),
                        componentItem.deletedAt.isNull())
                .orderBy(componentItem.componentId.asc(), componentItem.displayOrder.asc())
                .fetch();
    }

    /**
     * Reference ID로 아이템 조회 (특정 상품/브랜드가 어떤 컴포넌트에 포함되어 있는지)
     *
     * @param referenceId 참조 ID (상품 그룹 ID 등)
     * @param itemType 아이템 타입
     * @return ComponentItem 목록
     */
    public List<CmsComponentItemJpaEntity> findByReferenceIdAndType(
            Long referenceId, String itemType) {
        return queryFactory
                .selectFrom(componentItem)
                .where(
                        componentItem.referenceId.eq(referenceId),
                        componentItem.itemType.eq(itemType),
                        componentItem.status.eq(STATUS_ACTIVE),
                        componentItem.deletedAt.isNull())
                .fetch();
    }
}
