package com.ryuqq.setof.domain.cart.aggregate;

import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.Objects;

/**
 * 장바구니 아이템 Aggregate Root.
 *
 * <p>장바구니의 개별 아이템을 나타냅니다. CartItem이 독립적인 Aggregate Root로 설계되어 있으며, 각 아이템은 회원-상품그룹-상품(SKU) 조합으로
 * 유일합니다.
 *
 * <p>CQRS: Write 모델은 cart 테이블만 사용하고, Read 모델은 다중 테이블 JOIN으로 풍부한 응답을 제공합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CartItem {

    private final CartItemId id;
    private final MemberId memberId;
    private final LegacyUserId legacyUserId;
    private final ProductGroupId productGroupId;
    private final ProductId productId;
    private final SellerId sellerId;
    private CartQuantity quantity;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private CartItem(
            CartItemId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            ProductGroupId productGroupId,
            ProductId productId,
            SellerId sellerId,
            CartQuantity quantity,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 장바구니 아이템 생성.
     *
     * @param memberId 회원 ID (UUIDv7)
     * @param legacyUserId 레거시 사용자 ID
     * @param productGroupId 상품그룹 ID
     * @param productId 상품(SKU) ID
     * @param sellerId 판매자 ID
     * @param quantity 수량
     * @param occurredAt 생성 시각
     * @return 새 CartItem
     */
    public static CartItem forNew(
            MemberId memberId,
            LegacyUserId legacyUserId,
            ProductGroupId productGroupId,
            ProductId productId,
            SellerId sellerId,
            CartQuantity quantity,
            Instant occurredAt) {
        return new CartItem(
                CartItemId.forNew(),
                memberId,
                legacyUserId,
                productGroupId,
                productId,
                sellerId,
                quantity,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /**
     * 영속성에서 복원.
     *
     * @return 복원된 CartItem
     */
    public static CartItem reconstitute(
            CartItemId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            ProductGroupId productGroupId,
            ProductId productId,
            SellerId sellerId,
            CartQuantity quantity,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new CartItem(
                id,
                memberId,
                legacyUserId,
                productGroupId,
                productId,
                sellerId,
                quantity,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * UpdateData 기반 수정.
     *
     * @param updateData 수정 데이터 (수량 + 시간 포함)
     */
    public void update(CartItemUpdateData updateData) {
        this.quantity = updateData.quantity();
        this.updatedAt = updateData.occurredAt();
    }

    /**
     * 수량 변경.
     *
     * @param newQuantity 새 수량
     * @param occurredAt 변경 시각
     */
    public void changeQuantity(CartQuantity newQuantity, Instant occurredAt) {
        this.quantity = newQuantity;
        this.updatedAt = occurredAt;
    }

    /**
     * 수량 증가 (Upsert 패턴).
     *
     * @param additionalQuantity 추가할 수량
     * @param occurredAt 변경 시각
     */
    public void increaseQuantity(int additionalQuantity, Instant occurredAt) {
        this.quantity = this.quantity.increase(additionalQuantity);
        this.updatedAt = occurredAt;
    }

    /**
     * 장바구니에서 삭제 (Soft Delete).
     *
     * @param occurredAt 삭제 시각
     */
    public void remove(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isNew() {
        return id.isNew();
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    /**
     * 동일한 상품인지 확인.
     *
     * @param productGroupId 상품그룹 ID
     * @param productId 상품 ID
     * @return 동일 상품 여부
     */
    public boolean isSameProduct(ProductGroupId productGroupId, ProductId productId) {
        return this.productGroupId.equals(productGroupId) && this.productId.equals(productId);
    }

    public CartItemId id() {
        return id;
    }

    public MemberId memberId() {
        return memberId;
    }

    public LegacyUserId legacyUserId() {
        return legacyUserId;
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public ProductId productId() {
        return productId;
    }

    public SellerId sellerId() {
        return sellerId;
    }

    public CartQuantity quantity() {
        return quantity;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Long idValue() {
        return id.value();
    }

    public Long memberIdValue() {
        return memberId.value();
    }

    public long legacyUserIdValue() {
        return legacyUserId.value();
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public Long productIdValue() {
        return productId.value();
    }

    public Long sellerIdValue() {
        return sellerId != null ? sellerId.value() : null;
    }

    public int quantityValue() {
        return quantity.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
