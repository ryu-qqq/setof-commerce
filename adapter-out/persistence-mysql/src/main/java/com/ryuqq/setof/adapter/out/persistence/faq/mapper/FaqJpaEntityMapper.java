package com.ryuqq.setof.adapter.out.persistence.faq.mapper;

import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqContent;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;
import com.ryuqq.setof.domain.faq.vo.TopSetting;
import org.springframework.stereotype.Component;

/**
 * FaqJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer와 Domain Layer 간의 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public FaqJpaEntity toEntity(Faq domain) {
        return FaqJpaEntity.of(
                domain.getIdValue(),
                domain.getCategoryCodeValue(),
                domain.getQuestion(),
                domain.getAnswer(),
                domain.isTop(),
                domain.getTopOrder(),
                domain.getDisplayOrder(),
                domain.getStatus().name(),
                domain.getViewCount(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public Faq toDomain(FaqJpaEntity entity) {
        FaqId faqId = FaqId.of(entity.getId());
        FaqCategoryCode categoryCode = FaqCategoryCode.of(entity.getCategoryCode());
        FaqContent content = new FaqContent(entity.getQuestion(), entity.getAnswer());
        TopSetting topSetting =
                entity.isTop() ? TopSetting.top(entity.getTopOrder()) : TopSetting.notTop();
        FaqStatus status = FaqStatus.valueOf(entity.getStatus());

        return Faq.reconstitute(
                faqId,
                categoryCode,
                content,
                topSetting,
                entity.getDisplayOrder(),
                status,
                entity.getViewCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
