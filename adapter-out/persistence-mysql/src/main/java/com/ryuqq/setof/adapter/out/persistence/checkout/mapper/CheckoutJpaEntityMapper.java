package com.ryuqq.setof.adapter.out.persistence.checkout.mapper;

import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutJpaEntity;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.checkout.vo.CheckoutStatus;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * CheckoutJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Checkout 간 변환을 담당합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>CheckoutJpaEntity와 CheckoutItemJpaEntity 분리 변환
 *   <li>Parent-Child 관계를 FK 필드로 연결
 * </ul>
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Checkout -> CheckoutJpaEntity (저장용)
 *   <li>CheckoutJpaEntity + Items -> Checkout (조회용)
 *   <li>CheckoutItem <-> CheckoutItemJpaEntity 변환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (저장용, Parent만)
     *
     * @param domain Checkout 도메인
     * @return CheckoutJpaEntity (items 미포함)
     */
    public CheckoutJpaEntity toEntity(Checkout domain) {
        return CheckoutJpaEntity.of(
                domain.id().value(),
                domain.memberId(),
                domain.status().name(),
                extractReceiverName(domain),
                extractReceiverPhone(domain),
                extractAddress(domain),
                extractAddressDetail(domain),
                extractZipCode(domain),
                extractMemo(domain),
                domain.totalAmount().value(),
                domain.discountAmount().value(),
                domain.finalAmount().value(),
                domain.expiredAt(),
                domain.completedAt(),
                domain.createdAt(),
                domain.createdAt());
    }

    /**
     * Entity + Items -> Domain 변환 (조회용)
     *
     * @param entity CheckoutJpaEntity
     * @param itemEntities CheckoutItemJpaEntity 목록
     * @return Checkout 도메인
     */
    public Checkout toDomain(CheckoutJpaEntity entity, List<CheckoutItemJpaEntity> itemEntities) {
        List<CheckoutItem> items = toItems(itemEntities);
        ShippingAddressSnapshot shippingAddress = toShippingAddressSnapshot(entity);

        return Checkout.restore(
                CheckoutId.of(entity.getId()),
                entity.getMemberId(),
                CheckoutStatus.valueOf(entity.getStatus()),
                items,
                shippingAddress,
                CheckoutMoney.of(entity.getTotalAmount()),
                CheckoutMoney.of(entity.getDiscountAmount()),
                CheckoutMoney.of(entity.getFinalAmount()),
                entity.getCreatedAt(),
                entity.getExpiredAt(),
                entity.getCompletedAt());
    }

    /**
     * Domain Items -> Item Entities 변환 (저장용)
     *
     * @param checkoutId Parent Checkout UUID
     * @param items CheckoutItem 도메인 목록
     * @return CheckoutItemJpaEntity 목록
     */
    public List<CheckoutItemJpaEntity> toItemEntities(UUID checkoutId, List<CheckoutItem> items) {
        return items.stream().map(item -> toItemEntity(checkoutId, item)).toList();
    }

    /**
     * Item Entities -> Domain Items 변환 (조회용)
     *
     * @param entities CheckoutItemJpaEntity 목록
     * @return CheckoutItem 도메인 목록
     */
    public List<CheckoutItem> toItems(List<CheckoutItemJpaEntity> entities) {
        return entities.stream().map(this::toItem).toList();
    }

    private CheckoutItemJpaEntity toItemEntity(UUID checkoutId, CheckoutItem item) {
        return CheckoutItemJpaEntity.of(
                UUID.randomUUID(),
                checkoutId,
                item.productStockId(),
                item.productId(),
                item.sellerId(),
                item.quantity(),
                item.unitPrice().value(),
                item.totalPrice().value(),
                item.productName(),
                item.productImage(),
                item.optionName(),
                item.brandName(),
                item.sellerName());
    }

    private CheckoutItem toItem(CheckoutItemJpaEntity entity) {
        return CheckoutItem.of(
                entity.getProductStockId(),
                entity.getProductId(),
                entity.getSellerId(),
                entity.getQuantity(),
                CheckoutMoney.of(entity.getUnitPrice()),
                entity.getProductName(),
                entity.getProductImage(),
                entity.getOptionName(),
                entity.getBrandName(),
                entity.getSellerName());
    }

    private ShippingAddressSnapshot toShippingAddressSnapshot(CheckoutJpaEntity entity) {
        if (entity.getReceiverName() == null) {
            return null;
        }
        return ShippingAddressSnapshot.of(
                entity.getReceiverName(),
                entity.getReceiverPhone(),
                entity.getAddress(),
                entity.getAddressDetail(),
                entity.getZipCode(),
                entity.getMemo());
    }

    private String extractReceiverName(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().receiverName() : null;
    }

    private String extractReceiverPhone(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().receiverPhone() : null;
    }

    private String extractAddress(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().address() : null;
    }

    private String extractAddressDetail(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().addressDetail() : null;
    }

    private String extractZipCode(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().zipCode() : null;
    }

    private String extractMemo(Checkout domain) {
        return domain.shippingAddress() != null ? domain.shippingAddress().memo() : null;
    }
}
