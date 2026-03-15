package com.ryuqq.setof.adapter.out.persistence.wishlist.mapper;

import com.ryuqq.setof.adapter.out.persistence.wishlist.dto.WishlistItemQueryDto;
import com.ryuqq.setof.adapter.out.persistence.wishlist.entity.WishlistItemJpaEntity;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * WishlistItemJpaEntityMapper - 찜 항목 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환 및 QueryDto → Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistItemJpaEntityMapper {

    private static final String STATUS_SOLD_OUT = "SOLD_OUT";
    private static final ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");

    /**
     * Domain → Entity 변환.
     *
     * @param domain WishlistItem 도메인 객체
     * @return WishlistItemJpaEntity
     */
    public WishlistItemJpaEntity toEntity(WishlistItem domain) {
        return WishlistItemJpaEntity.create(
                domain.idValue(),
                domain.memberIdValue(),
                domain.legacyMemberId() != null ? domain.legacyMemberIdValue() : null,
                domain.productGroupIdValue(),
                domain.createdAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity WishlistItemJpaEntity
     * @return WishlistItem 도메인 객체
     */
    public WishlistItem toDomain(WishlistItemJpaEntity entity) {
        MemberId memberId = entity.getMemberId() != null ? MemberId.of(entity.getMemberId()) : null;
        LegacyMemberId legacyMemberId =
                entity.getLegacyMemberId() != null
                        ? LegacyMemberId.of(entity.getLegacyMemberId())
                        : null;

        return WishlistItem.reconstitute(
                WishlistItemId.of(entity.getId()),
                legacyMemberId,
                memberId,
                ProductGroupId.of(entity.getProductGroupId()),
                DeletionStatus.reconstitute(entity.getDeletedAt() != null, entity.getDeletedAt()),
                entity.getCreatedAt());
    }

    /**
     * QueryDto → WishlistItemResult 변환.
     *
     * <p>status 필드를 soldOutYn/displayYn으로 변환하고, int 가격을 BigDecimal로 변환합니다.
     *
     * @param dto 복합 조회 DTO
     * @return WishlistItemResult
     */
    public WishlistItemResult toResult(WishlistItemQueryDto dto) {
        String soldOutYn = STATUS_SOLD_OUT.equals(dto.getStatus()) ? "Y" : "N";
        String displayYn = dto.isBrandDisplayed() ? "Y" : "N";
        LocalDateTime insertDate =
                dto.getCreatedAt() != null
                        ? LocalDateTime.ofInstant(dto.getCreatedAt(), ZONE_KST)
                        : null;

        return WishlistItemResult.of(
                dto.getWishlistItemId(),
                dto.getProductGroupId(),
                dto.getSellerId(),
                dto.getProductGroupName(),
                dto.getBrandId(),
                dto.getBrandName(),
                dto.getProductImageUrl(),
                BigDecimal.valueOf(dto.getRegularPrice()),
                BigDecimal.valueOf(dto.getCurrentPrice()),
                dto.getDiscountRate(),
                soldOutYn,
                displayYn,
                insertDate);
    }
}
