package com.connectly.partnerAdmin.module.display.repository.content;


import com.connectly.partnerAdmin.module.common.enums.PeriodType;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.content.QContentResponse;
import com.connectly.partnerAdmin.module.display.filter.ContentFilter;
import com.connectly.partnerAdmin.module.display.dto.query.ContentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.QComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.QContentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import com.connectly.partnerAdmin.module.utils.QueryDslUtil;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.display.entity.component.QComponent.component;
import static com.connectly.partnerAdmin.module.display.entity.component.QViewExtension.viewExtension;
import static com.connectly.partnerAdmin.module.display.entity.content.QContent.content;


@Repository
public class ContentFetchRepositoryImpl extends AbstractCommonRepository implements ContentFetchRepository {


    protected ContentFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<ContentQueryDto> fetchContentQueryInfo(long contentId) {
        return Optional.ofNullable(
                getQueryFactory()
                .select(content.id,
                        content.title.coalesce(""),
                        content.memo.coalesce(""),
                        content.displayYn,
                        content.displayPeriod,
                        content.imageUrl.coalesce(""),
                        component.id,
                        component.componentName.coalesce(""),
                        component.exposedProducts,
                        component.componentDetails,
                        component.displayOrder,
                        viewExtension.viewExtensionDetails.viewExtensionType,
                        viewExtension.viewExtensionDetails)
                .from(content)
                .leftJoin(component)
                        .on(component.contentId.eq(content.id))
                        .on(component.deleteYn.eq(Yn.N))
                .leftJoin(viewExtension)
                    .on(viewExtension.id.eq(component.viewExtension.id))
                .where(contentIdEq(contentId), deleteYnEq())
                .transform(
                        GroupBy.groupBy(content.id).as(
                            new QContentQueryDto(
                                    content.id,
                                    content.title.coalesce(""),
                                    content.memo.coalesce(""),
                                    content.displayYn,
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
                                                    viewExtension.viewExtensionDetails,
                                                    component.displayYn
                                            )
                                    )
                            )
                        )
                ).get(contentId));
    }

    @Override
    public Optional<Content> fetchContentEntity(long contentId) {
        return Optional.ofNullable(
                getQueryFactory().selectFrom(content)
                        .where(contentIdEq(contentId), deleteYnEq())
                        .fetchOne()
        );
    }

    @Override
    public List<ContentResponse> fetchContents(ContentFilter filterDto, Pageable pageable) {
        List<Long> contentIds = fetchContentsId(filterDto, pageable);
        return fetchContents(contentIds, pageable);
    }

    private List<ContentResponse> fetchContents(List<Long> contentIds, Pageable pageable){
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, content);

        return getQueryFactory().selectFrom(content)
                .where(
                        contentIdIn(contentIds),
                        deleteYnEq()
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .transform(
                        GroupBy.groupBy(content.id).list(
                                new QContentResponse(
                                        content.id,
                                        content.displayYn,
                                        content.title,
                                        content.displayPeriod,
                                        content.insertOperator,
                                        content.insertDate,
                                        content.updateOperator,
                                        content.updateDate
                                )
                        )
                );
    }



    @Override
    public JPAQuery<Long> fetchContentQuery(ContentFilter filter, Pageable pageable){
        return getQueryFactory()
                .select(content.count())
                .from(content)
                .where(
                        displayYnEq(filter.getDisplayYn()),
                        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        deleteYnEq()
                )
                .distinct();
    }


    private List<Long> fetchContentsId(ContentFilter filter, Pageable pageable){
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, content);

        return getQueryFactory().select(content.id)
                .from(content)
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .where(
                        displayYnEq(filter.getDisplayYn()),
                        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        deleteYnEq()
                )
                .fetch();
    }

    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable, Path<?> path) {
        List<OrderSpecifier<?>> allOrderSpecifiers = super.getAllOrderSpecifiers(pageable, path);
        if (allOrderSpecifiers.isEmpty()) {
            allOrderSpecifiers.add(content.id.desc());
        }
        return allOrderSpecifiers;
    }


    private BooleanExpression displayYnEq(Yn displayYn){
        if(displayYn != null) return content.displayYn.eq(displayYn);
        return null;
    }



    private BooleanExpression periodTypeEq(PeriodType periodType, LocalDateTime startDate, LocalDateTime endDate) {
        if (periodType != null && periodType.isInsert()) {
            return content.insertDate.goe(startDate)
                    .and(content.insertDate.loe(endDate));
        }
        return content.displayPeriod.displayStartDate.between(startDate, endDate)
                .or(content.displayPeriod.displayEndDate.between(startDate, endDate))
                .or(content.displayPeriod.displayStartDate.loe(startDate).and(content.displayPeriod.displayEndDate.goe(endDate)));
    }




    private BooleanExpression contentIdIn(List<Long> contentIds) {
        return content.id.in(contentIds);
    }


    private BooleanExpression contentIdEq(long contentId) {
        return content.id.eq(contentId);
    }

    private BooleanExpression contentIdGt(long lastContentId) {
        return lastContentId > 0 ? content.id.gt(lastContentId) : null;
    }


    private BooleanExpression contentIdLt(long lastContentId) {
        return lastContentId > 0 ? content.id.lt(lastContentId) : null;
    }

    private BooleanExpression deleteYnEq() {
        return QueryDslUtil.notDeleted(content.deleteYn, Yn.N);
    }

}
