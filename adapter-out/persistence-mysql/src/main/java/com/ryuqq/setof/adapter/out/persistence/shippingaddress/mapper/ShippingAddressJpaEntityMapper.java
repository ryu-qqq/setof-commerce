package com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressJpaEntityMapper - 배송지 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
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
public class ShippingAddressJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain ShippingAddress 도메인 객체
     * @return ShippingAddressJpaEntity
     */
    public ShippingAddressJpaEntity toEntity(ShippingAddress domain) {
        return ShippingAddressJpaEntity.create(
                domain.idValue(),
                domain.memberIdValue(),
                domain.legacyMemberIdValue(),
                domain.receiverNameValue(),
                domain.shippingAddressNameValue(),
                domain.address().line1(),
                domain.address().line2(),
                domain.address().zipcode(),
                domain.country().name(),
                domain.deliveryRequestValue(),
                domain.phoneNumberValue(),
                domain.isDefault(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity ShippingAddressJpaEntity
     * @return ShippingAddress 도메인 객체
     */
    public ShippingAddress toDomain(ShippingAddressJpaEntity entity) {
        MemberId memberId = entity.getMemberId() != null ? MemberId.of(entity.getMemberId()) : null;
        LegacyMemberId legacyMemberId =
                entity.getLegacyMemberId() != null
                        ? LegacyMemberId.of(entity.getLegacyMemberId())
                        : null;

        return ShippingAddress.reconstitute(
                ShippingAddressId.of(entity.getId()),
                memberId,
                legacyMemberId,
                ReceiverName.of(entity.getReceiverName()),
                ShippingAddressName.of(entity.getShippingAddressName()),
                Address.of(entity.getZipCode(), entity.getAddressLine1(), entity.getAddressLine2()),
                Country.valueOf(entity.getCountry()),
                DeliveryRequest.of(entity.getDeliveryRequest()),
                PhoneNumber.of(entity.getPhoneNumber()),
                entity.isDefaultAddress(),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
