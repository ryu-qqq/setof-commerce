package com.setof.connectly.module.display.repository.content;

import static com.setof.connectly.module.display.entity.QContent.content;
import static com.setof.connectly.module.display.entity.component.QComponent.component;
import static com.setof.connectly.module.display.entity.component.QViewExtension.viewExtension;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.content.QContentGroupResponse;
import com.setof.connectly.module.display.dto.query.ContentQueryDto;
import com.setof.connectly.module.display.dto.query.QComponentQueryDto;
import com.setof.connectly.module.display.dto.query.QContentQueryDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContentFindRepositoryImpl implements ContentFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> fetchOnDisplayContents() {
        return queryFactory
                .select(content.id)
                .from(content)
                .innerJoin(component)
                .on(component.contentId.eq(content.id))
                .where(displayPeriodBetweenTime(Yn.N), deleteYnEq())
                .distinct()
                .fetch();
    }

    @Override
    public Optional<ContentQueryDto> fetchContentQueryInfo(long contentId, Yn bypass) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                content.id,
                                content.title,
                                content.displayYn,
                                content.displayPeriod,
                                content.imageUrl,
                                component.id,
                                component.componentName,
                                component.exposedProducts,
                                component.componentDetails,
                                component.displayOrder,
                                viewExtension.viewExtensionDetails.viewExtensionType,
                                viewExtension.viewExtensionDetails)
                        .from(content)
                        .leftJoin(component)
                        .on(component.contentId.eq(content.id))
                        .leftJoin(viewExtension)
                        .on(viewExtension.id.eq(component.viewExtension.id))
                        .where(
                                contentIdEq(contentId),
                                deleteYnEq(),
                                displayPeriodBetweenTime(bypass))
                        .orderBy(content.id.desc())
                        .transform(
                                GroupBy.groupBy(content.id)
                                        .as(
                                                new QContentQueryDto(
                                                        content.id,
                                                        content.title.coalesce(""),
                                                        content.imageUrl,
                                                        content.displayPeriod,
                                                        GroupBy.list(
                                                                new QComponentQueryDto(
                                                                        component.contentId,
                                                                        component.id,
                                                                        component.componentName,
                                                                        component.exposedProducts,
                                                                        component.componentDetails,
                                                                        component.displayPeriod,
                                                                        component.displayOrder,
                                                                        viewExtension.id,
                                                                        viewExtension
                                                                                .viewExtensionDetails,
                                                                        component.displayYn)))))
                        .get(contentId));
    }

    @Override
    public Optional<ContentGroupResponse> fetchContent(long contentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QContentGroupResponse(
                                        content.id,
                                        content.displayPeriod,
                                        content.title,
                                        content.memo,
                                        content.imageUrl))
                        .from(content)
                        .where(contentIdEq(contentId))
                        .fetchOne());
    }

    private BooleanExpression contentIdEq(long contentId) {
        return content.id.eq(contentId);
    }

    private BooleanExpression deleteYnEq() {
        return content.deleteYn.eq(Yn.N).and(component.deleteYn.eq(Yn.N));
    }

    private BooleanExpression displayPeriodBetweenTime(Yn bypass) {
        if (bypass != null) {
            if (bypass.isYes()) return null;
        }

        LocalDateTime now = LocalDateTime.now();
        return content.displayPeriod
                .displayStartDate
                .loe(now)
                .and(content.displayPeriod.displayEndDate.goe(now))
                .and(component.displayPeriod.displayStartDate.loe(now))
                .and(component.displayPeriod.displayEndDate.goe(now));
    }
}
