package com.ryuqq.setof.adapter.out.persistence.composite.productgroup.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupOffsetSearchCriteria;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import java.util.List;
import org.springframework.stereotype.Component;

/** ProductGroup Composite QueryDSL 조건 빌더. */
@Component
public class ProductGroupCompositeConditionBuilder {

    private static final QProductGroupJpaEntity productGroup =
            QProductGroupJpaEntity.productGroupJpaEntity;

    public BooleanExpression idEq(Long id) {
        return id != null ? productGroup.id.eq(id) : null;
    }

    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? productGroup.id.in(ids) : null;
    }

    public BooleanExpression sellerIdIn(List<Long> sellerIds) {
        return sellerIds != null && !sellerIds.isEmpty()
                ? productGroup.sellerId.in(sellerIds)
                : null;
    }

    public BooleanExpression brandIdIn(List<Long> brandIds) {
        return brandIds != null && !brandIds.isEmpty() ? productGroup.brandId.in(brandIds) : null;
    }

    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty()
                ? productGroup.categoryId.in(categoryIds)
                : null;
    }

    public BooleanExpression statusIn(ProductGroupOffsetSearchCriteria criteria) {
        if (!criteria.hasStatusFilter()) {
            return null;
        }
        List<String> statusNames =
                criteria.statuses().stream().map(ProductGroupStatus::name).toList();
        return productGroup.status.in(statusNames);
    }

    public BooleanExpression statusNotDeleted() {
        return productGroup.status.ne(ProductGroupStatus.DELETED.name());
    }

    public BooleanExpression createdAtGoe(ProductGroupOffsetSearchCriteria criteria) {
        if (!criteria.hasDateRange()) {
            return null;
        }
        return criteria.dateRange().startInstant() != null
                ? productGroup.createdAt.goe(criteria.dateRange().startInstant())
                : null;
    }

    public BooleanExpression createdAtLoe(ProductGroupOffsetSearchCriteria criteria) {
        if (!criteria.hasDateRange()) {
            return null;
        }
        return criteria.dateRange().endInstant() != null
                ? productGroup.createdAt.loe(criteria.dateRange().endInstant())
                : null;
    }

    public BooleanExpression searchCondition(ProductGroupOffsetSearchCriteria criteria) {
        if (!criteria.hasSearchCondition()) {
            return null;
        }
        if (!criteria.hasSearchField()) {
            return productGroup.productGroupName.containsIgnoreCase(criteria.searchWord());
        }
        return switch (criteria.searchField()) {
            case PRODUCT_GROUP_NAME ->
                    productGroup.productGroupName.containsIgnoreCase(criteria.searchWord());
        };
    }
}
