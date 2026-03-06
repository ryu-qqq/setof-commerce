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
public class SellerAddressJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain SellerAddress 도메인 객체
     * @return SellerAddressJpaEntity
     */
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

    /**
     * Entity → Domain 변환.
     *
     * @param entity SellerAddressJpaEntity
     * @return SellerAddress 도메인 객체
     */
    public SellerAddress toDomain(SellerAddressJpaEntity entity) {
        Address address =
                entity.getZipcode() != null && entity.getAddress() != null
                        ? Address.of(
                                entity.getZipcode(), entity.getAddress(), entity.getAddressDetail())
                        : null;

        ContactInfo contactInfo =
                entity.getContactName() != null && entity.getContactPhone() != null
                        ? ContactInfo.of(entity.getContactName(), entity.getContactPhone())
                        : null;

        return SellerAddress.reconstitute(
                SellerAddressId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                AddressType.valueOf(entity.getAddressType()),
                AddressName.of(entity.getAddressName()),
                address,
                contactInfo,
                entity.isDefaultAddress(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
