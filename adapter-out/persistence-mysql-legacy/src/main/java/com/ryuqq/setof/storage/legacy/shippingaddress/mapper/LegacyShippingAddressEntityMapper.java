package com.ryuqq.setof.storage.legacy.shippingaddress.mapper;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.shippingaddress.entity.LegacyShippingAddressEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyShippingAddressEntityMapper - 레거시 배송지 Entity <-> Domain/Result 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyShippingAddressEntityMapper {

    public ShippingAddressResult toResult(LegacyShippingAddressEntity entity) {
        return ShippingAddressResult.of(
                entity.getId(),
                entity.getReceiverName(),
                entity.getShippingAddressName(),
                entity.getAddressLine1(),
                entity.getAddressLine2(),
                entity.getZipCode(),
                entity.getCountry(),
                entity.getDeliveryRequest(),
                entity.getPhoneNumber(),
                toDefaultYnString(entity.getDefaultYn()));
    }

    public List<ShippingAddressResult> toResults(List<LegacyShippingAddressEntity> entities) {
        return entities.stream().map(this::toResult).toList();
    }

    public ShippingAddress toDomain(LegacyShippingAddressEntity entity) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(entity.getId()),
                null,
                LegacyMemberId.of(entity.getUserId()),
                ReceiverName.of(entity.getReceiverName()),
                ShippingAddressName.of(entity.getShippingAddressName()),
                Address.of(entity.getZipCode(), entity.getAddressLine1(), entity.getAddressLine2()),
                parseCountry(entity.getCountry()),
                DeliveryRequest.of(entity.getDeliveryRequest()),
                PhoneNumber.of(entity.getPhoneNumber()),
                entity.getDefaultYn() == Yn.Y,
                DeletionStatus.active(),
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    public LegacyShippingAddressEntity toEntity(ShippingAddress domain) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate =
                domain.createdAt() != null
                        ? LocalDateTime.ofInstant(domain.createdAt(), ZoneId.systemDefault())
                        : now;
        LocalDateTime updateDate =
                domain.updatedAt() != null
                        ? LocalDateTime.ofInstant(domain.updatedAt(), ZoneId.systemDefault())
                        : now;

        if (domain.isNew()) {
            return LegacyShippingAddressEntity.create(
                    domain.legacyMemberIdValue(),
                    domain.receiverNameValue(),
                    domain.shippingAddressNameValue(),
                    domain.address().line1(),
                    domain.address().line2(),
                    domain.address().zipcode(),
                    domain.country() != null ? domain.country().name() : Country.KR.name(),
                    domain.deliveryRequestValue(),
                    domain.phoneNumberValue(),
                    domain.isDefault() ? Yn.Y : Yn.N,
                    insertDate,
                    updateDate);
        }

        return LegacyShippingAddressEntity.reconstitute(
                domain.idValue(),
                domain.legacyMemberIdValue(),
                domain.receiverNameValue(),
                domain.shippingAddressNameValue(),
                domain.address().line1(),
                domain.address().line2(),
                domain.address().zipcode(),
                domain.country() != null ? domain.country().name() : Country.KR.name(),
                domain.deliveryRequestValue(),
                domain.phoneNumberValue(),
                domain.isDefault() ? Yn.Y : Yn.N,
                insertDate,
                updateDate);
    }

    private String toDefaultYnString(Yn defaultYn) {
        if (defaultYn == null) {
            return Yn.N.name();
        }
        return defaultYn.name();
    }

    private Country parseCountry(String country) {
        if (country == null || country.isBlank()) {
            return Country.KR;
        }
        try {
            return Country.valueOf(country);
        } catch (IllegalArgumentException e) {
            return Country.KR;
        }
    }

    private java.time.Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return java.time.Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
