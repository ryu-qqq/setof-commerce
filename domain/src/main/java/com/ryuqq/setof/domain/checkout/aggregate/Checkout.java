package com.ryuqq.setof.domain.checkout.aggregate;

import com.ryuqq.setof.domain.checkout.exception.CheckoutStatusException;
import com.ryuqq.setof.domain.checkout.exception.InvalidCheckoutItemException;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.checkout.vo.CheckoutStatus;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checkout Aggregate Root
 *
 * <p>결제 세션을 나타내는 Aggregate Root입니다. 장바구니 → 결제 페이지 진입 시 생성되며, 결제 완료 후 주문(Order)으로 전환됩니다.
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
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *    │
 *    └→ EXPIRED
 * </pre>
 */
public class Checkout {

    private final CheckoutId id;
    private final String memberId;
    private final CheckoutStatus status;
    private final List<CheckoutItem> items;
    private final ShippingAddressSnapshot shippingAddress;
    private final CheckoutMoney totalAmount;
    private final CheckoutMoney discountAmount;
    private final CheckoutMoney finalAmount;
    private final Instant createdAt;
    private final Instant expiredAt;
    private final Instant completedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Checkout(
            CheckoutId id,
            String memberId,
            CheckoutStatus status,
            List<CheckoutItem> items,
            ShippingAddressSnapshot shippingAddress,
            CheckoutMoney totalAmount,
            CheckoutMoney discountAmount,
            CheckoutMoney finalAmount,
            Instant createdAt,
            Instant expiredAt,
            Instant completedAt) {
        this.id = id;
        this.memberId = memberId;
        this.status = status;
        this.items = items != null ? List.copyOf(items) : Collections.emptyList();
        this.shippingAddress = shippingAddress;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.completedAt = completedAt;
    }

    /**
     * 신규 결제 세션 생성
     *
     * @param memberId 회원 ID (UUIDv7 String)
     * @param items 결제 항목 목록
     * @param shippingAddress 배송지 정보
     * @param discountAmount 할인 금액
     * @param expirationMinutes 만료 시간 (분)
     * @param now 현재 시각
     * @return Checkout 인스턴스
     */
    public static Checkout forNew(
            String memberId,
            List<CheckoutItem> items,
            ShippingAddressSnapshot shippingAddress,
            CheckoutMoney discountAmount,
            int expirationMinutes,
            Instant now) {

        validateItems(items);
        validateMemberId(memberId);

        CheckoutMoney totalAmount = calculateTotalAmount(items);
        CheckoutMoney discount = discountAmount != null ? discountAmount : CheckoutMoney.zero();
        CheckoutMoney finalAmount = calculateFinalAmount(totalAmount, discount);

        return new Checkout(
                CheckoutId.forNew(),
                memberId,
                CheckoutStatus.defaultStatus(),
                items,
                shippingAddress,
                totalAmount,
                discount,
                finalAmount,
                now,
                now.plusSeconds(expirationMinutes * 60L),
                null);
    }

    /** 영속화된 데이터로부터 복원 (Persistence Layer용) */
    public static Checkout restore(
            CheckoutId id,
            String memberId,
            CheckoutStatus status,
            List<CheckoutItem> items,
            ShippingAddressSnapshot shippingAddress,
            CheckoutMoney totalAmount,
            CheckoutMoney discountAmount,
            CheckoutMoney finalAmount,
            Instant createdAt,
            Instant expiredAt,
            Instant completedAt) {
        return new Checkout(
                id,
                memberId,
                status,
                items,
                shippingAddress,
                totalAmount,
                discountAmount,
                finalAmount,
                createdAt,
                expiredAt,
                completedAt);
    }

    // ===== 상태 전이 메서드 =====

    /**
     * 결제 처리 시작
     *
     * @return PROCESSING 상태의 새 Checkout 인스턴스
     * @throws CheckoutStatusException 상태 전이 불가 시
     */
    public Checkout startProcessing() {
        if (!status.canStartProcessing()) {
            throw CheckoutStatusException.notProcessable(id, status);
        }
        return new Checkout(
                id,
                memberId,
                CheckoutStatus.PROCESSING,
                items,
                shippingAddress,
                totalAmount,
                discountAmount,
                finalAmount,
                createdAt,
                expiredAt,
                null);
    }

    /**
     * 결제 완료
     *
     * @param now 현재 시각
     * @return COMPLETED 상태의 새 Checkout 인스턴스
     * @throws CheckoutStatusException 상태 전이 불가 시
     */
    public Checkout complete(Instant now) {
        if (!status.canComplete()) {
            throw CheckoutStatusException.notCompletable(id, status);
        }
        return new Checkout(
                id,
                memberId,
                CheckoutStatus.COMPLETED,
                items,
                shippingAddress,
                totalAmount,
                discountAmount,
                finalAmount,
                createdAt,
                expiredAt,
                now);
    }

    /**
     * 세션 만료 처리
     *
     * @return EXPIRED 상태의 새 Checkout 인스턴스
     * @throws CheckoutStatusException 상태 전이 불가 시
     */
    public Checkout expire() {
        if (!status.canExpire()) {
            throw CheckoutStatusException.alreadyCompleted(id);
        }
        return new Checkout(
                id,
                memberId,
                CheckoutStatus.EXPIRED,
                items,
                shippingAddress,
                totalAmount,
                discountAmount,
                finalAmount,
                createdAt,
                expiredAt,
                null);
    }

    // ===== 도메인 로직 =====

    /**
     * 만료 여부 확인
     *
     * @param now 현재 시각
     * @return 만료되었으면 true
     */
    public boolean isExpired(Instant now) {
        return !status.isFinal() && now.isAfter(expiredAt);
    }

    /**
     * 결제 가능 여부 확인
     *
     * @param now 현재 시각
     * @return 결제 가능하면 true
     */
    public boolean canPay(Instant now) {
        return status == CheckoutStatus.PENDING && !isExpired(now);
    }

    /**
     * 판매자별 항목 그룹핑
     *
     * @return 판매자 ID → 해당 판매자의 항목 목록
     */
    public Map<Long, List<CheckoutItem>> groupItemsBySeller() {
        return items.stream().collect(Collectors.groupingBy(CheckoutItem::sellerId));
    }

    /**
     * 판매자별 금액 합계
     *
     * @return 판매자 ID → 해당 판매자의 총 금액
     */
    public Map<Long, CheckoutMoney> calculateAmountBySeller() {
        return items.stream()
                .collect(
                        Collectors.groupingBy(
                                CheckoutItem::sellerId,
                                Collectors.reducing(
                                        CheckoutMoney.zero(),
                                        CheckoutItem::totalPrice,
                                        CheckoutMoney::add)));
    }

    /**
     * 고유 판매자 ID 목록
     *
     * @return 판매자 ID 목록 (중복 제거)
     */
    public List<Long> distinctSellerIds() {
        return items.stream().map(CheckoutItem::sellerId).distinct().toList();
    }

    /**
     * 항목 개수
     *
     * @return 총 항목 개수
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * 총 수량 (모든 항목의 수량 합계)
     *
     * @return 총 수량
     */
    public int totalQuantity() {
        return items.stream().mapToInt(CheckoutItem::quantity).sum();
    }

    /**
     * 재고 요구 수량 맵 반환
     *
     * @return productStockId → 필요 수량 매핑
     */
    public Map<Long, Integer> getStockRequirements() {
        return items.stream()
                .collect(
                        Collectors.toMap(
                                CheckoutItem::productStockId,
                                CheckoutItem::quantity,
                                Integer::sum));
    }

    // ===== Getter 메서드 =====

    public CheckoutId id() {
        return id;
    }

    public String memberId() {
        return memberId;
    }

    public CheckoutStatus status() {
        return status;
    }

    public List<CheckoutItem> items() {
        return java.util.Collections.unmodifiableList(items);
    }

    public ShippingAddressSnapshot shippingAddress() {
        return shippingAddress;
    }

    public CheckoutMoney totalAmount() {
        return totalAmount;
    }

    public CheckoutMoney discountAmount() {
        return discountAmount;
    }

    public CheckoutMoney finalAmount() {
        return finalAmount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant expiredAt() {
        return expiredAt;
    }

    public Instant completedAt() {
        return completedAt;
    }

    // ===== Private Helper =====

    private static void validateItems(List<CheckoutItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidCheckoutItemException("결제 항목이 비어있습니다");
        }
    }

    private static void validateMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("회원 ID는 필수입니다");
        }
    }

    private static CheckoutMoney calculateTotalAmount(List<CheckoutItem> items) {
        return items.stream()
                .map(CheckoutItem::totalPrice)
                .reduce(CheckoutMoney.zero(), CheckoutMoney::add);
    }

    private static CheckoutMoney calculateFinalAmount(CheckoutMoney total, CheckoutMoney discount) {
        if (discount.isGreaterThanOrEqual(total)) {
            return CheckoutMoney.zero();
        }
        return total.subtract(discount);
    }
}
