package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.command.ShippingSnapshotCommandPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentSnapshotShippingAddressEntity;
import com.ryuqq.setof.storage.legacy.payment.repository.LegacyPaymentSnapshotShippingAddressJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyShippingSnapshotAdapter - 배송지 스냅샷 Legacy 어댑터.
 *
 * <p>Order에서 paymentId + receiverInfo를 추출하여 payment_snapshot_shipping_address 테이블에 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyShippingSnapshotAdapter implements ShippingSnapshotCommandPort {

    private final LegacyPaymentSnapshotShippingAddressJpaRepository repository;

    public LegacyShippingSnapshotAdapter(
            LegacyPaymentSnapshotShippingAddressJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void persist(Order order) {
        ReceiverInfo receiverInfo = order.receiverInfo();
        LegacyPaymentSnapshotShippingAddressEntity entity =
                LegacyPaymentSnapshotShippingAddressEntity.create(
                        order.paymentId(),
                        receiverInfo.receiverName(),
                        receiverInfo.addressLine1(),
                        receiverInfo.addressLine2(),
                        receiverInfo.receiverPhone(),
                        receiverInfo.zipCode(),
                        receiverInfo.country(),
                        receiverInfo.deliveryRequest());
        repository.save(entity);
    }
}
