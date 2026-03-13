package com.ryuqq.setof.adapter.out.persistence.contentpage.condition;

import static com.ryuqq.setof.adapter.out.persistence.contentpage.entity.QContentPageJpaEntity.contentPageJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

@Component
public class ContentPageConditionBuilder {

    public BooleanExpression contentPageIdEq(Long contentPageId) {
        return contentPageId != null ? contentPageJpaEntity.id.eq(contentPageId) : null;
    }

    public BooleanExpression contentPageActiveEq(boolean active) {
        return contentPageJpaEntity.active.eq(active);
    }

    public BooleanExpression contentPageNotDeleted() {
        return contentPageJpaEntity.deletedAt.isNull();
    }
}
