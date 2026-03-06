package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsJpaEntity;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerCsId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.CsContact;
import com.ryuqq.setof.domain.seller.vo.OperatingHours;
import org.springframework.stereotype.Component;

/**
 * SellerCsJpaEntityMapper - 셀러 CS 정보 Entity-Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCsJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain SellerCs 도메인 객체
     * @return SellerCsJpaEntity
     */
    public SellerCsJpaEntity toEntity(SellerCs domain) {
        OperatingHours hours = domain.operatingHours();
        return SellerCsJpaEntity.create(
                domain.idValue(),
                domain.sellerIdValue(),
                domain.csPhone(),
                domain.csMobile(),
                domain.csEmail(),
                hours != null ? hours.startTime() : null,
                hours != null ? hours.endTime() : null,
                domain.operatingDays(),
                domain.kakaoChannelUrl(),
                domain.createdAt(),
                domain.updatedAt(),
                null);
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity SellerCsJpaEntity
     * @return SellerCs 도메인 객체
     */
    public SellerCs toDomain(SellerCsJpaEntity entity) {
        CsContact csContact =
                CsContact.of(entity.getCsPhone(), entity.getCsMobile(), entity.getCsEmail());
        OperatingHours operatingHours =
                OperatingHours.of(entity.getOperatingStartTime(), entity.getOperatingEndTime());

        return SellerCs.reconstitute(
                SellerCsId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                csContact,
                operatingHours,
                entity.getOperatingDays(),
                entity.getKakaoChannelUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
