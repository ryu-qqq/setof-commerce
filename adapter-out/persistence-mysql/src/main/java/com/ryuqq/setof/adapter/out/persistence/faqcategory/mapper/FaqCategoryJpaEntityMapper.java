package com.ryuqq.setof.adapter.out.persistence.faqcategory.mapper;

import com.ryuqq.setof.adapter.out.persistence.faqcategory.entity.FaqCategoryJpaEntity;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;
import org.springframework.stereotype.Component;

/**
 * FaqCategoryJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer와 Domain Layer 간의 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public FaqCategoryJpaEntity toEntity(FaqCategory domain) {
        return FaqCategoryJpaEntity.of(
                domain.getIdValue(),
                domain.getCodeValue(),
                domain.getName(),
                domain.getDescription(),
                domain.getDisplayOrder(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public FaqCategory toDomain(FaqCategoryJpaEntity entity) {
        FaqCategoryId id = FaqCategoryId.of(entity.getId());
        FaqCategoryCode code = FaqCategoryCode.of(entity.getCode());
        FaqCategoryStatus status = FaqCategoryStatus.valueOf(entity.getStatus());

        return FaqCategory.reconstitute(
                id,
                code,
                entity.getName(),
                entity.getDescription(),
                entity.getDisplayOrder(),
                status,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
