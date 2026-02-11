package com.ryuqq.setof.storage.legacy.composite.web.content.repository;

import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyComponentEntity.legacyComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyContentEntity.legacyContentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyViewExtensionEntity.legacyViewExtensionEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.content.dto.query.LegacyContentSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.content.condition.LegacyWebContentCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebContentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebViewExtensionQueryDto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebContentCompositeQueryDslRepository - 레거시 웹 콘텐츠 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebContentCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebContentCompositeConditionBuilder conditionBuilder;

    public LegacyWebContentCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebContentCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 전시 중인 콘텐츠 ID 목록 조회.
     *
     * <p>Content + Component 조인하여 현재 전시 중인 콘텐츠 ID를 조회.
     *
     * @return 전시 중인 콘텐츠 ID 목록
     */
    public List<Long> fetchOnDisplayContentIds() {
        return queryFactory
                .select(legacyContentEntity.id)
                .from(legacyContentEntity)
                .innerJoin(legacyComponentEntity)
                .on(legacyComponentEntity.contentId.eq(legacyContentEntity.id))
                .where(
                        conditionBuilder.onDisplayContent(false),
                        conditionBuilder.onDisplayComponent(false))
                .distinct()
                .fetch();
    }

    /**
     * 콘텐츠 단건 조회 (메타데이터만).
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 Optional
     */
    public Optional<LegacyWebContentQueryDto> fetchContentById(Long contentId) {
        LegacyWebContentQueryDto dto =
                queryFactory
                        .select(createContentProjection())
                        .from(legacyContentEntity)
                        .where(conditionBuilder.contentIdEq(contentId))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 콘텐츠 조회 (삭제되지 않은 것만).
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 Optional
     */
    public Optional<LegacyWebContentQueryDto> fetchContent(Long contentId) {
        LegacyWebContentQueryDto dto =
                queryFactory
                        .select(createContentProjection())
                        .from(legacyContentEntity)
                        .where(
                                conditionBuilder.contentIdEq(contentId),
                                conditionBuilder.contentNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 콘텐츠에 속한 컴포넌트 목록 조회.
     *
     * @param condition 검색 조건
     * @return 컴포넌트 목록
     */
    public List<LegacyWebComponentQueryDto> fetchComponentsByContentId(
            LegacyContentSearchCondition condition) {
        return queryFactory
                .select(createComponentProjection())
                .from(legacyComponentEntity)
                .where(
                        conditionBuilder.componentContentIdEq(condition.contentId()),
                        conditionBuilder.onDisplayComponent(condition.bypass()))
                .orderBy(legacyComponentEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 뷰 확장 ID 목록으로 조회.
     *
     * @param viewExtensionIds 뷰 확장 ID 목록
     * @return 뷰 확장 목록
     */
    public List<LegacyWebViewExtensionQueryDto> fetchViewExtensionsByIds(
            List<Long> viewExtensionIds) {
        if (viewExtensionIds == null || viewExtensionIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(createViewExtensionProjection())
                .from(legacyViewExtensionEntity)
                .where(legacyViewExtensionEntity.id.in(viewExtensionIds))
                .fetch();
    }

    /**
     * 콘텐츠와 컴포넌트, 뷰 확장 조인 조회.
     *
     * <p>fetchContent 엔드포인트용 복합 쿼리.
     *
     * @param condition 검색 조건
     * @return 컴포넌트 목록 (뷰 확장 ID 포함)
     */
    public List<LegacyWebComponentQueryDto> fetchContentWithComponents(
            LegacyContentSearchCondition condition) {
        return queryFactory
                .select(createComponentProjection())
                .from(legacyContentEntity)
                .leftJoin(legacyComponentEntity)
                .on(legacyComponentEntity.contentId.eq(legacyContentEntity.id))
                .where(
                        conditionBuilder.contentIdEq(condition.contentId()),
                        conditionBuilder.onDisplayContent(condition.bypass()),
                        conditionBuilder.onDisplayComponent(condition.bypass()))
                .orderBy(legacyComponentEntity.displayOrder.asc())
                .fetch();
    }

    /** Projections.constructor()로 Content Projection 생성. */
    private ConstructorExpression<LegacyWebContentQueryDto> createContentProjection() {
        return Projections.constructor(
                LegacyWebContentQueryDto.class,
                legacyContentEntity.id,
                legacyContentEntity.title,
                legacyContentEntity.memo,
                legacyContentEntity.imageUrl,
                legacyContentEntity.displayStartDate,
                legacyContentEntity.displayEndDate);
    }

    /** Projections.constructor()로 Component Projection 생성. */
    private ConstructorExpression<LegacyWebComponentQueryDto> createComponentProjection() {
        return Projections.constructor(
                LegacyWebComponentQueryDto.class,
                legacyComponentEntity.id,
                legacyComponentEntity.contentId,
                legacyComponentEntity.componentName,
                legacyComponentEntity.componentType.stringValue(),
                legacyComponentEntity.listType.stringValue(),
                legacyComponentEntity.orderType.stringValue(),
                legacyComponentEntity.badgeType.stringValue(),
                legacyComponentEntity.filterYn.stringValue(),
                legacyComponentEntity.exposedProducts,
                legacyComponentEntity.displayOrder,
                legacyComponentEntity.displayStartDate,
                legacyComponentEntity.displayEndDate,
                legacyComponentEntity.viewExtensionId);
    }

    /** Projections.constructor()로 ViewExtension Projection 생성. */
    private ConstructorExpression<LegacyWebViewExtensionQueryDto> createViewExtensionProjection() {
        return Projections.constructor(
                LegacyWebViewExtensionQueryDto.class,
                legacyViewExtensionEntity.id,
                legacyViewExtensionEntity.viewExtensionType.stringValue(),
                legacyViewExtensionEntity.linkUrl,
                legacyViewExtensionEntity.buttonName,
                legacyViewExtensionEntity.productCountPerClick,
                legacyViewExtensionEntity.maxClickCount,
                legacyViewExtensionEntity.afterMaxActionType.stringValue(),
                legacyViewExtensionEntity.afterMaxActionLinkUrl);
    }
}
