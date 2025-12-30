package com.ryuqq.setof.domain.cart.aggregate;

import com.ryuqq.setof.domain.cart.exception.CartItemLimitExceededException;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import com.ryuqq.setof.domain.cart.exception.QuantityLimitExceededException;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cart Aggregate Root
 *
 * <p>장바구니를 나타내는 Aggregate Root입니다. 회원별로 하나의 장바구니를 가집니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드, 상태 변경 시 새 인스턴스 반환
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드를 통해서만
 * </ul>
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>동일 상품 담기 시 수량 합산
 *   <li>장바구니 최대 100개 아이템
 *   <li>단일 상품 최대 99개
 * </ul>
 */
public class Cart {

    /** 장바구니 최대 아이템 개수 */
    public static final int MAX_ITEM_COUNT = 100;

    private final CartId id;
    private final UUID memberId;
    private final List<CartItem> items;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Cart(
            CartId id, UUID memberId, List<CartItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 장바구니 생성 (비어있는 상태)
     *
     * @param memberId 회원 ID (UUID)
     * @param now 현재 시각
     * @return Cart 인스턴스
     */
    public static Cart forNew(UUID memberId, Instant now) {
        validateMemberId(memberId);
        return new Cart(
                null, // 신규 생성 시 ID 없음
                memberId,
                new ArrayList<>(),
                now,
                now);
    }

    /**
     * 영속화된 데이터로부터 복원 (Persistence Layer용)
     *
     * @param id 장바구니 ID
     * @param memberId 회원 ID (UUID)
     * @param items 장바구니 아이템 목록
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return Cart 인스턴스
     */
    public static Cart restore(
            CartId id, UUID memberId, List<CartItem> items, Instant createdAt, Instant updatedAt) {
        return new Cart(id, memberId, items, createdAt, updatedAt);
    }

    // ===== 상태 변경 메서드 (새 인스턴스 반환) =====

    /**
     * 아이템 추가 (동일 상품은 수량 합산)
     *
     * @param newItem 추가할 아이템
     * @param now 현재 시각
     * @return 아이템이 추가된 새 Cart 인스턴스
     * @throws CartItemLimitExceededException 최대 개수 초과 시
     * @throws QuantityLimitExceededException 최대 수량 초과 시
     */
    public Cart addItem(CartItem newItem, Instant now) {
        List<CartItem> updatedItems = new ArrayList<>(this.items);

        // 동일 상품 찾기
        Optional<CartItem> existingItem = findItemByProductStockId(newItem.productStockId());

        if (existingItem.isPresent()) {
            // 최대 수량 사전 검증 (mergeWith 호출 전)
            CartItem existing = existingItem.get();
            int mergedQuantity = existing.quantity() + newItem.quantity();
            if (mergedQuantity > CartItem.MAX_QUANTITY) {
                throw new QuantityLimitExceededException(
                        newItem.productStockId(), mergedQuantity, CartItem.MAX_QUANTITY);
            }

            // 동일 상품 존재 → 수량 합산
            CartItem merged = existing.withQuantity(mergedQuantity);

            // 기존 아이템 교체
            updatedItems.removeIf(item -> item.isSameProduct(newItem.productStockId()));
            updatedItems.add(merged);
        } else {
            // 신규 아이템 → 개수 제한 검증
            if (updatedItems.size() >= MAX_ITEM_COUNT) {
                throw new CartItemLimitExceededException(updatedItems.size(), MAX_ITEM_COUNT);
            }
            updatedItems.add(newItem);
        }

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 아이템 수량 변경
     *
     * @param cartItemId 변경할 아이템 ID
     * @param newQuantity 새 수량
     * @param now 현재 시각
     * @return 수량이 변경된 새 Cart 인스턴스
     * @throws CartItemNotFoundException 아이템이 없는 경우
     */
    public Cart updateQuantity(CartItemId cartItemId, int newQuantity, Instant now) {
        CartItem targetItem =
                findItemById(cartItemId)
                        .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        List<CartItem> updatedItems =
                this.items.stream()
                        .map(item -> item.hasId(cartItemId) ? item.withQuantity(newQuantity) : item)
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 아이템 선택 상태 변경
     *
     * @param cartItemId 변경할 아이템 ID
     * @param selected 새 선택 상태
     * @param now 현재 시각
     * @return 선택 상태가 변경된 새 Cart 인스턴스
     * @throws CartItemNotFoundException 아이템이 없는 경우
     */
    public Cart updateSelected(CartItemId cartItemId, boolean selected, Instant now) {
        CartItem targetItem =
                findItemById(cartItemId)
                        .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        List<CartItem> updatedItems =
                this.items.stream()
                        .map(item -> item.hasId(cartItemId) ? item.withSelected(selected) : item)
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 전체 선택/해제
     *
     * @param selected 선택 상태
     * @param now 현재 시각
     * @return 전체 선택 상태가 변경된 새 Cart 인스턴스
     */
    public Cart updateAllSelected(boolean selected, Instant now) {
        List<CartItem> updatedItems =
                this.items.stream()
                        .map(item -> item.withSelected(selected))
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 아이템 삭제
     *
     * @param cartItemId 삭제할 아이템 ID
     * @param now 현재 시각
     * @return 아이템이 삭제된 새 Cart 인스턴스
     * @throws CartItemNotFoundException 아이템이 없는 경우
     */
    public Cart removeItem(CartItemId cartItemId, Instant now) {
        CartItem targetItem =
                findItemById(cartItemId)
                        .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        List<CartItem> updatedItems =
                this.items.stream()
                        .filter(item -> !item.hasId(cartItemId))
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 여러 아이템 삭제
     *
     * @param cartItemIds 삭제할 아이템 ID 목록
     * @param now 현재 시각
     * @return 아이템이 삭제된 새 Cart 인스턴스
     */
    public Cart removeItems(List<CartItemId> cartItemIds, Instant now) {
        List<CartItem> updatedItems =
                this.items.stream()
                        .filter(item -> cartItemIds.stream().noneMatch(item::hasId))
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 선택된 아이템만 삭제
     *
     * @param now 현재 시각
     * @return 선택된 아이템이 삭제된 새 Cart 인스턴스
     */
    public Cart removeSelectedItems(Instant now) {
        List<CartItem> updatedItems =
                this.items.stream().filter(item -> !item.selected()).collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 장바구니 비우기
     *
     * @param now 현재 시각
     * @return 비워진 새 Cart 인스턴스
     */
    public Cart clear(Instant now) {
        return new Cart(id, memberId, new ArrayList<>(), createdAt, now);
    }

    /**
     * 아이템들 소프트 딜리트 (결제 시작 시 사용)
     *
     * <p>결제 진행 중에 장바구니 아이템을 소프트 딜리트 처리합니다. 결제 실패 시 복원할 수 있습니다.
     *
     * @param cartItemIds 소프트 딜리트할 아이템 ID 목록
     * @param now 삭제 시각
     * @return 소프트 딜리트 처리된 새 Cart 인스턴스
     */
    public Cart softDeleteItems(List<CartItemId> cartItemIds, Instant now) {
        List<CartItem> updatedItems =
                this.items.stream()
                        .map(
                                item -> {
                                    boolean shouldDelete =
                                            cartItemIds.stream().anyMatch(item::hasId);
                                    return shouldDelete ? item.withDeleted(now) : item;
                                })
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 소프트 딜리트된 아이템들 복원 (결제 실패 시 사용)
     *
     * <p>결제 실패 시 소프트 딜리트 처리된 장바구니 아이템을 복원합니다.
     *
     * @param cartItemIds 복원할 아이템 ID 목록
     * @param now 현재 시각
     * @return 복원된 새 Cart 인스턴스
     */
    public Cart restoreItems(List<CartItemId> cartItemIds, Instant now) {
        List<CartItem> updatedItems =
                this.items.stream()
                        .map(
                                item -> {
                                    boolean shouldRestore =
                                            cartItemIds.stream().anyMatch(item::hasId);
                                    return shouldRestore ? item.withRestored() : item;
                                })
                        .collect(Collectors.toList());

        return new Cart(id, memberId, updatedItems, createdAt, now);
    }

    /**
     * 활성 아이템만 조회 (소프트 딜리트 제외)
     *
     * @return 활성 상태의 아이템 목록
     */
    public List<CartItem> activeItems() {
        return items.stream().filter(CartItem::isActive).toList();
    }

    /**
     * 삭제된 아이템만 조회
     *
     * @return 소프트 딜리트된 아이템 목록
     */
    public List<CartItem> deletedItems() {
        return items.stream().filter(CartItem::isDeleted).toList();
    }

    /**
     * ID가 할당된 새 인스턴스 반환 (영속화 후)
     *
     * @param newId 할당된 ID
     * @return ID가 할당된 Cart
     */
    public Cart withId(CartId newId) {
        return new Cart(newId, memberId, items, createdAt, updatedAt);
    }

    // ===== 조회 메서드 =====

    /**
     * 선택된 아이템만 조회
     *
     * @return 선택된 아이템 목록
     */
    public List<CartItem> selectedItems() {
        return items.stream().filter(CartItem::selected).toList();
    }

    /**
     * 판매자별 아이템 그룹핑
     *
     * @return 판매자 ID → 해당 판매자의 아이템 목록
     */
    public Map<Long, List<CartItem>> groupItemsBySeller() {
        return items.stream().collect(Collectors.groupingBy(CartItem::sellerId));
    }

    /**
     * 선택된 아이템의 판매자별 그룹핑
     *
     * @return 판매자 ID → 선택된 아이템 목록
     */
    public Map<Long, List<CartItem>> groupSelectedItemsBySeller() {
        return selectedItems().stream().collect(Collectors.groupingBy(CartItem::sellerId));
    }

    /**
     * 아이템 ID로 조회
     *
     * @param cartItemId 아이템 ID
     * @return Optional 아이템
     */
    public Optional<CartItem> findItemById(CartItemId cartItemId) {
        return items.stream().filter(item -> item.hasId(cartItemId)).findFirst();
    }

    /**
     * 상품 재고 ID로 조회
     *
     * @param productStockId 상품 재고 ID
     * @return Optional 아이템
     */
    public Optional<CartItem> findItemByProductStockId(Long productStockId) {
        return items.stream().filter(item -> item.isSameProduct(productStockId)).findFirst();
    }

    /**
     * 총 금액 계산 (모든 아이템)
     *
     * @return 총 금액
     */
    public BigDecimal totalAmount() {
        return items.stream().map(CartItem::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 선택된 아이템 총 금액 계산
     *
     * @return 선택된 아이템 총 금액
     */
    public BigDecimal selectedTotalAmount() {
        return selectedItems().stream()
                .map(CartItem::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 아이템 개수
     *
     * @return 총 아이템 개수
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * 선택된 아이템 개수
     *
     * @return 선택된 아이템 개수
     */
    public int selectedItemCount() {
        return (int) items.stream().filter(CartItem::selected).count();
    }

    /**
     * 총 수량 (모든 아이템의 수량 합계)
     *
     * @return 총 수량
     */
    public int totalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.quantity();
        }
        return total;
    }

    /**
     * 장바구니가 비어있는지 확인
     *
     * @return 비어있으면 true
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * 신규 장바구니 여부 (ID 미할당)
     *
     * @return 신규이면 true
     */
    public boolean isNew() {
        return id == null;
    }

    // ===== Getter 메서드 =====

    public CartId id() {
        return id;
    }

    public UUID memberId() {
        return memberId;
    }

    public List<CartItem> items() {
        return Collections.unmodifiableList(items);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    // ===== Private Helper =====

    private static void validateMemberId(UUID memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("회원 ID는 null일 수 없습니다");
        }
    }
}
