package com.ryuqq.setof.storage.legacy.seller.mapper;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.DisplayName;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacySellerEntityMapper - 레거시 셀러 Entity-Domain 매퍼.
 *
 * <p>레거시 Entity → 새 Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * <p>레거시 스키마 차이:
 *
 * <ul>
 *   <li>displayName 컬럼 없음 → sellerName으로 대체
 *   <li>active 컬럼 없음 → true 기본값
 *   <li>deletedAt 컬럼 없음 → null (항상 활성 상태)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacySellerEntityMapper {

    /**
     * 레거시 Entity → Domain 변환.
     *
     * @param entity LegacySellerEntity
     * @return Seller 도메인 객체
     */
    public Seller toDomain(LegacySellerEntity entity) {
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());

        return Seller.reconstitute(
                SellerId.of(entity.getId()),
                SellerName.of(entity.getSellerName()),
                DisplayName.of(entity.getSellerName()),
                LogoUrl.of(entity.getSellerLogoUrl()),
                Description.of(entity.getSellerDescription()),
                true,
                null,
                createdAt,
                updatedAt);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
