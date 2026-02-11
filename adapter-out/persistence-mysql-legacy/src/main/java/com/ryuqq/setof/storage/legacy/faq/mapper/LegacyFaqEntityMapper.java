package com.ryuqq.setof.storage.legacy.faq.mapper;

import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyFaqEntityMapper - 레거시 FAQ Entity-Domain 매퍼.
 *
 * <p>레거시 Entity → 새 Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * <p>레거시 FaqType(domain.legacy.faq) → 신규 FaqType(domain.faq.vo) 변환을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyFaqEntityMapper {

    /**
     * 레거시 Entity → Domain 변환.
     *
     * @param entity LegacyFaqEntity
     * @return Faq 도메인 객체
     */
    public Faq toDomain(LegacyFaqEntity entity) {
        FaqType faqType = toNewFaqType(entity.getFaqType());
        int displayOrder = entity.getDisplayOrder() != null ? entity.getDisplayOrder() : 0;
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());

        return Faq.reconstitute(
                FaqId.of(entity.getId()),
                faqType,
                FaqTitle.of(entity.getTitle()),
                FaqContents.of(entity.getContents()),
                FaqDisplayOrder.of(displayOrder),
                entity.getTopDisplayOrder(),
                createdAt,
                updatedAt);
    }

    /**
     * 레거시 FaqType → 신규 FaqType 변환.
     *
     * <p>Entity와 Domain이 동일한 FaqType을 사용하므로 그대로 반환합니다.
     *
     * @param faqType FaqType
     * @return FaqType
     */
    private FaqType toNewFaqType(FaqType faqType) {
        return faqType;
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
