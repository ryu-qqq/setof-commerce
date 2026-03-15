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

    /** 콘텐츠 페이지 활성 여부 nullable 조건 (null이면 조건 없음) */
    public BooleanExpression contentPageActiveEq(Boolean active) {
        return active != null ? contentPageJpaEntity.active.eq(active) : null;
    }

    /** 전시 기간 시작일 이후 조건 */
    public BooleanExpression contentPageDisplayStartAfter(Instant startDate) {
        return startDate != null ? contentPageJpaEntity.displayStartAt.goe(startDate) : null;
    }

    /** 전시 기간 종료일 이전 조건 */
    public BooleanExpression contentPageDisplayEndBefore(Instant endDate) {
        return endDate != null ? contentPageJpaEntity.displayEndAt.loe(endDate) : null;
    }

    /** 등록일 시작 이후 조건 */
    public BooleanExpression contentPageCreatedAtAfter(Instant createdAtStart) {
        return createdAtStart != null ? contentPageJpaEntity.createdAt.goe(createdAtStart) : null;
    }

    /** 등록일 종료 이전 조건 */
    public BooleanExpression contentPageCreatedAtBefore(Instant createdAtEnd) {
        return createdAtEnd != null ? contentPageJpaEntity.createdAt.loe(createdAtEnd) : null;
    }

    /** 제목 포함 검색 */
    public BooleanExpression contentPageTitleContains(String keyword) {
        return keyword != null && !keyword.isBlank()
                ? contentPageJpaEntity.title.containsIgnoreCase(keyword)
                : null;
    }

    /** ID 미만 조건 (No-Offset 페이징) */
    public BooleanExpression contentPageIdLt(Long lastDomainId) {
        return lastDomainId != null ? contentPageJpaEntity.id.lt(lastDomainId) : null;
    }
}
