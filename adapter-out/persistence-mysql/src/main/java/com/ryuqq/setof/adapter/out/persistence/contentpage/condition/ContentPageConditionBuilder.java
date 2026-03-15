package com.ryuqq.setof.adapter.out.persistence.contentpage.condition;

import static com.ryuqq.setof.adapter.out.persistence.contentpage.entity.QContentPageJpaEntity.contentPageJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.Instant;
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

    /** 콘텐츠 페이지 전시 기간 조건 (displayStartAt <= now <= displayEndAt) */
    public BooleanExpression contentPageDisplayPeriodBetween() {
        Instant now = Instant.now();
        return contentPageJpaEntity
                .displayStartAt
                .loe(now)
                .and(contentPageJpaEntity.displayEndAt.goe(now));
    }
}
