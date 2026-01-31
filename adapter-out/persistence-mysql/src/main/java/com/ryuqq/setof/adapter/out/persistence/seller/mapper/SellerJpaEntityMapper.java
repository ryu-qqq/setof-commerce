package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.DisplayName;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import org.springframework.stereotype.Component;

/**
 * SellerJpaEntityMapper - 셀러 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 */
@Component
public class SellerJpaEntityMapper {

    public SellerJpaEntity toEntity(Seller domain) {
        return SellerJpaEntity.create(
                domain.idValue(),
                domain.sellerNameValue(),
                domain.displayNameValue(),
                domain.logoUrlValue(),
                domain.descriptionValue(),
                domain.isActive(),
                domain.authTenantId(),
                domain.authOrganizationId(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    public Seller toDomain(SellerJpaEntity entity) {
        return Seller.reconstitute(
                SellerId.of(entity.getId()),
                SellerName.of(entity.getSellerName()),
                DisplayName.of(entity.getDisplayName()),
                LogoUrl.of(entity.getLogoUrl()),
                Description.of(entity.getDescription()),
                entity.isActive(),
                entity.getDeletedAt(),
                entity.getAuthTenantId(),
                entity.getAuthOrganizationId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
