package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import com.ryuqq.setof.domain.seller.vo.ContactInfo;
import org.springframework.stereotype.Component;

/**
 * SellerAddressJpaEntityMapper - 셀러 주소 Entity-Domain 매퍼.
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
public class SellerAddressJpaEntityMapper {

    public SellerAddressJpaEntity toEntity(SellerAddress domain) {
        return SellerAddressJpaEntity.create(
                domain.idValue(),
                domain.sellerIdValue(),
                domain.addressType().name(),
                domain.addressNameValue(),
                domain.addressZipCode(),
                domain.addressRoad(),
                domain.addressDetail(),
                domain.contactInfoName(),
                domain.contactInfoPhone(),
                domain.isDefaultAddress(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    public SellerAddress toDomain(SellerAddressJpaEntity entity) {
        return SellerAddress.reconstitute(
                SellerAddressId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                AddressType.valueOf(entity.getAddressType()),
                AddressName.of(entity.getAddressName()),
                Address.of(entity.getZipcode(), entity.getAddress(), entity.getAddressDetail()),
                ContactInfo.of(entity.getContactName(), entity.getContactPhone()),
                entity.isDefaultAddress(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
