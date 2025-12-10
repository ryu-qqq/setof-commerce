package com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.AddressName;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverInfo;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 ShippingAddress 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>ShippingAddress -> ShippingAddressJpaEntity (저장용)
 *   <li>ShippingAddressJpaEntity -> ShippingAddress (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain ShippingAddress 도메인
     * @return ShippingAddressJpaEntity
     */
    public ShippingAddressJpaEntity toEntity(ShippingAddress domain) {
        return ShippingAddressJpaEntity.of(
                domain.getIdValue(),
                domain.getMemberId().toString(),
                domain.getAddressNameValue(),
                domain.getReceiverNameValue(),
                domain.getReceiverPhoneValue(),
                domain.getZipCodeValue(),
                domain.getRoadAddressValue(),
                domain.getJibunAddressValue(),
                domain.getDetailAddressValue(),
                domain.getDeliveryRequestValue(),
                domain.isDefault(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ShippingAddressJpaEntity
     * @return ShippingAddress 도메인
     */
    public ShippingAddress toDomain(ShippingAddressJpaEntity entity) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(entity.getId()),
                UUID.fromString(entity.getMemberId()),
                AddressName.of(entity.getAddressName()),
                ReceiverInfo.of(entity.getReceiverName(), entity.getReceiverPhone()),
                buildDeliveryAddress(entity),
                buildDeliveryRequest(entity),
                entity.isDefault(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

    // ========== Private Helper Methods ==========

    private DeliveryAddress buildDeliveryAddress(ShippingAddressJpaEntity entity) {
        return DeliveryAddress.of(
                entity.getRoadAddress(),
                entity.getJibunAddress(),
                entity.getDetailAddress(),
                entity.getZipCode());
    }

    private DeliveryRequest buildDeliveryRequest(ShippingAddressJpaEntity entity) {
        String deliveryRequestValue = entity.getDeliveryRequest();
        if (deliveryRequestValue == null || deliveryRequestValue.isBlank()) {
            return null;
        }
        return DeliveryRequest.of(deliveryRequestValue);
    }
}
