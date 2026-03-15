package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.exception.InsufficientBudgetException;
import com.ryuqq.setof.domain.discount.exception.InvalidDiscountConfigException;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DiscountPolicy - 할인 정책 Aggregate Root.
 *
 * <p>즉시할인과 쿠폰 모두의 기반이 되는 핵심 할인 규칙을 표현합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>RATE 타입 → discountRate 필수 (0~100), discountAmount는 null
 *   <li>FIXED_AMOUNT 타입 → discountAmount 필수, discountRate는 null
 *   <li>COUPON 타입 정책은 stackingGroup = COUPON이어야 함
 *   <li>예산 사용액 ≤ 총 예산
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class DiscountPolicy {

    private final DiscountPolicyId id;
    private DiscountPolicyName name;
    private String description;
    private DiscountMethod discountMethod;
    private DiscountRate discountRate;
    private Money discountAmount;
    private Money maxDiscountAmount;
    private boolean discountCapped;
    private Money minimumOrderAmount;
    private final ApplicationType applicationType;
    private final PublisherType publisherType;
    private final SellerId sellerId;
    private final StackingGroup stackingGroup;
    private Priority priority;
    private DiscountPeriod period;
    private DiscountBudget budget;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final List<DiscountTarget> targets;
    private final Instant createdAt;
    private Instant updatedAt;

    private DiscountPolicy(
            DiscountPolicyId id,
            DiscountPolicyName name,
            String description,
            DiscountMethod discountMethod,
            DiscountRate discountRate,
            Money discountAmount,
            Money maxDiscountAmount,
            boolean discountCapped,
            Money minimumOrderAmount,
            ApplicationType applicationType,
            PublisherType publisherType,
            SellerId sellerId,
            StackingGroup stackingGroup,
            Priority priority,
            DiscountPeriod period,
            DiscountBudget budget,
            boolean active,
            DeletionStatus deletionStatus,
            List<DiscountTarget> targets,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discountMethod = discountMethod;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.discountCapped = discountCapped;
        this.minimumOrderAmount = minimumOrderAmount;
        this.applicationType = applicationType;
        this.publisherType = publisherType;
        this.sellerId = sellerId;
        this.stackingGroup = stackingGroup;
        this.priority = priority;
        this.period = period;
        this.budget = budget;
        this.active = active;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.targets = targets != null ? new ArrayList<>(targets) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 할인 정책 생성.
     *
     * @param name 정책명 (필수)
     * @param description 설명 (nullable)
     * @param discountMethod 할인 방식 (필수)
     * @param discountRate 할인율 (RATE 타입 시 필수)
     * @param discountAmount 정액 할인금 (FIXED 타입 시 필수)
     * @param maxDiscountAmount 최대 할인 한도액 (nullable)
     * @param discountCapped 한도 적용 여부
     * @param minimumOrderAmount 최소 주문금액 (nullable)
     * @param applicationType 적용 방식 (필수)
     * @param publisherType 생성 주체 (필수)
     * @param sellerId 셀러 ID (셀러 할인 시)
     * @param stackingGroup 스태킹 그룹 (필수)
     * @param priority 우선순위 (필수)
     * @param period 유효 기간 (필수)
     * @param budget 예산 (필수)
     * @param now 생성 시각
     * @return 새 DiscountPolicy 인스턴스
     */
    public static DiscountPolicy forNew(
            DiscountPolicyName name,
            String description,
            DiscountMethod discountMethod,
            DiscountRate discountRate,
            Money discountAmount,
            Money maxDiscountAmount,
            boolean discountCapped,
            Money minimumOrderAmount,
            ApplicationType applicationType,
            PublisherType publisherType,
            SellerId sellerId,
            StackingGroup stackingGroup,
            Priority priority,
            DiscountPeriod period,
            DiscountBudget budget,
            Instant now) {
        validateDiscountConfig(discountMethod, discountRate, discountAmount);
        validateApplicationTypeAndStackingGroup(applicationType, stackingGroup);

        return new DiscountPolicy(
                DiscountPolicyId.forNew(),
                name,
                description,
                discountMethod,
                discountRate,
                discountAmount,
                maxDiscountAmount,
                discountCapped,
                minimumOrderAmount,
                applicationType,
                publisherType,
                sellerId,
                stackingGroup,
                priority,
                period,
                budget,
                true,
                DeletionStatus.active(),
                new ArrayList<>(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @return 복원된 DiscountPolicy 인스턴스
     */
    public static DiscountPolicy reconstitute(
            DiscountPolicyId id,
            DiscountPolicyName name,
            String description,
            DiscountMethod discountMethod,
            DiscountRate discountRate,
            Money discountAmount,
            Money maxDiscountAmount,
            boolean discountCapped,
            Money minimumOrderAmount,
            ApplicationType applicationType,
            PublisherType publisherType,
            SellerId sellerId,
            StackingGroup stackingGroup,
            Priority priority,
            DiscountPeriod period,
            DiscountBudget budget,
            boolean active,
            Instant deletedAt,
            List<DiscountTarget> targets,
            Instant createdAt,
            Instant updatedAt) {
        DeletionStatus status =
                deletedAt != null ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();
        return new DiscountPolicy(
                id,
                name,
                description,
                discountMethod,
                discountRate,
                discountAmount,
                maxDiscountAmount,
                discountCapped,
                minimumOrderAmount,
                applicationType,
                publisherType,
                sellerId,
                stackingGroup,
                priority,
                period,
                budget,
                active,
                status,
                targets,
                createdAt,
                updatedAt);
    }

    // ========== Validation ==========

    private static void validateDiscountConfig(
            DiscountMethod method, DiscountRate rate, Money amount) {
        if (method == DiscountMethod.RATE) {
            if (rate == null) {
                throw new InvalidDiscountConfigException("RATE 할인은 할인율이 필수입니다");
            }
            if (amount != null) {
                throw new InvalidDiscountConfigException("RATE 할인은 정액 할인금을 설정할 수 없습니다");
            }
        } else if (method == DiscountMethod.FIXED_AMOUNT) {
            if (amount == null || amount.isZero()) {
                throw new InvalidDiscountConfigException("FIXED_AMOUNT 할인은 할인금이 필수입니다");
            }
            if (rate != null) {
                throw new InvalidDiscountConfigException("FIXED_AMOUNT 할인은 할인율을 설정할 수 없습니다");
            }
        }
    }

    private static void validateApplicationTypeAndStackingGroup(
            ApplicationType applicationType, StackingGroup stackingGroup) {
        if (applicationType == ApplicationType.COUPON && stackingGroup != StackingGroup.COUPON) {
            throw new InvalidDiscountConfigException("COUPON 타입 정책은 COUPON 스태킹 그룹이어야 합니다");
        }
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 할인 정책 수정.
     *
     * @param data 수정할 데이터
     * @param now 수정 시각
     */
    public void update(DiscountPolicyUpdateData data, Instant now) {
        validateDiscountConfig(data.discountMethod(), data.discountRate(), data.discountAmount());

        this.name = data.name();
        this.description = data.description();
        this.discountMethod = data.discountMethod();
        this.discountRate = data.discountRate();
        this.discountAmount = data.discountAmount();
        this.maxDiscountAmount = data.maxDiscountAmount();
        this.discountCapped = data.discountCapped();
        this.minimumOrderAmount = data.minimumOrderAmount();
        this.priority = data.priority();
        this.period = data.period();
        this.budget = data.budget();
        this.updatedAt = now;
    }

    /**
     * 할인 정책 활성화.
     *
     * @param now 활성화 시각
     */
    public void activate(Instant now) {
        this.active = true;
        this.updatedAt = now;
    }

    /**
     * 할인 정책 비활성화.
     *
     * @param now 비활성화 시각
     */
    public void deactivate(Instant now) {
        this.active = false;
        this.updatedAt = now;
    }

    /**
     * 할인 정책 삭제 (Soft Delete).
     *
     * @param now 삭제 시각
     */
    public void delete(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    // ========== Target Management ==========

    /**
     * 할인 대상 추가.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @param now 추가 시각
     */
    public void addTarget(DiscountTargetType targetType, long targetId, Instant now) {
        boolean exists = targets.stream().anyMatch(t -> t.matches(targetType, targetId));
        if (exists) {
            return;
        }
        targets.add(DiscountTarget.forNew(targetType, targetId));
        this.updatedAt = now;
    }

    /**
     * 할인 대상 제거 (비활성화).
     *
     * @param targetId 대상 엔티티 ID
     * @param now 제거 시각
     */
    public void removeTarget(DiscountTargetId targetId, Instant now) {
        targets.stream()
                .filter(t -> t.id().equals(targetId) && t.isActive())
                .findFirst()
                .ifPresent(
                        t -> {
                            t.deactivate();
                            this.updatedAt = now;
                        });
    }

    /**
     * 할인 대상 전체 교체 (Diff 패턴).
     *
     * <p>요청된 타겟 목록을 기존 활성 타겟과 비교하여 added/removed/retained으로 분류합니다.
     *
     * <ul>
     *   <li>요청에 있으나 기존에 없음 → added (신규 생성)
     *   <li>기존에 있으나 요청에 없음 → removed (비활성화)
     *   <li>기존에 있고 요청에도 있음 → retained (유지)
     * </ul>
     *
     * @param targetType 대상 유형
     * @param targetIds 새로 교체할 대상 ID 목록
     * @param now 변경 시각
     * @return DiscountTargetDiff 변경 비교 결과
     */
    public DiscountTargetDiff replaceTargets(
            DiscountTargetType targetType, List<Long> targetIds, Instant now) {

        List<DiscountTarget> added = new ArrayList<>();
        List<DiscountTarget> removed = new ArrayList<>();
        List<DiscountTarget> retained = new ArrayList<>();

        java.util.Set<Long> requestedIds = new java.util.HashSet<>(targetIds);

        // 기존 활성 타겟 분류: retained vs removed
        for (DiscountTarget existing : targets) {
            if (!existing.isActive()) {
                continue;
            }
            if (existing.targetType() == targetType && requestedIds.contains(existing.targetId())) {
                retained.add(existing);
                requestedIds.remove(existing.targetId());
            } else if (existing.targetType() == targetType) {
                existing.deactivate();
                removed.add(existing);
            }
        }

        // 남은 requestedIds → added
        for (Long targetId : requestedIds) {
            DiscountTarget newTarget = DiscountTarget.forNew(targetType, targetId);
            targets.add(newTarget);
            added.add(newTarget);
        }

        if (!added.isEmpty() || !removed.isEmpty()) {
            this.updatedAt = now;
        }

        return DiscountTargetDiff.of(added, removed, retained, now);
    }

    // ========== Discount Calculation ==========

    /**
     * 주어진 기준가에 대한 할인 금액 계산.
     *
     * <p>RATE: basePrice * rate / 100 (maxDiscountAmount 적용)
     *
     * <p>FIXED_AMOUNT: 고정 할인금 (basePrice 초과 방지)
     *
     * @param basePrice 할인 적용 기준가
     * @return 할인 금액
     */
    public Money calculateDiscountAmount(Money basePrice) {
        if (basePrice == null || basePrice.isZero()) {
            return Money.zero();
        }

        Money discount;
        if (discountMethod == DiscountMethod.RATE) {
            int rawDiscount = (int) (basePrice.value() * discountRate.toFraction());
            discount = Money.of(rawDiscount);
            if (discountCapped
                    && maxDiscountAmount != null
                    && discount.isGreaterThan(maxDiscountAmount)) {
                discount = maxDiscountAmount;
            }
        } else {
            discount = discountAmount;
        }

        if (discount.isGreaterThan(basePrice)) {
            discount = basePrice;
        }

        return discount;
    }

    // ========== Budget Management ==========

    /**
     * 예산 소진.
     *
     * @param amount 소진 금액
     * @param now 소진 시각
     * @throws InsufficientBudgetException 예산 부족 시
     */
    public void consumeBudget(Money amount, Instant now) {
        if (!budget.hasSufficient(amount)) {
            throw new InsufficientBudgetException(id.value());
        }
        this.budget = budget.consume(amount);
        this.updatedAt = now;
    }

    /** 예산이 충분한지 확인 */
    public boolean hasSufficientBudget(Money amount) {
        return budget.hasSufficient(amount);
    }

    // ========== Condition Checks ==========

    /** 주어진 시점에서 적용 가능한지 확인 (활성 + 기간 내 + 미삭제 + 예산 잔여) */
    public boolean isApplicableAt(Instant now) {
        return active
                && !deletionStatus.isDeleted()
                && period.isActiveAt(now)
                && !budget.isExhausted();
    }

    /** 특정 대상에 해당하는지 확인 */
    public boolean hasTarget(DiscountTargetType targetType, long targetId) {
        return targets.stream().anyMatch(t -> t.matches(targetType, targetId));
    }

    /** 최소 주문금액 조건 충족 여부 */
    public boolean meetsMinimumOrder(Money orderAmount) {
        if (minimumOrderAmount == null || minimumOrderAmount.isZero()) {
            return true;
        }
        return orderAmount.isGreaterThanOrEqual(minimumOrderAmount);
    }

    // ========== Accessor Methods ==========

    public DiscountPolicyId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public DiscountPolicyName name() {
        return name;
    }

    public String nameValue() {
        return name.value();
    }

    public String description() {
        return description;
    }

    public DiscountMethod discountMethod() {
        return discountMethod;
    }

    public DiscountRate discountRate() {
        return discountRate;
    }

    public Double discountRateValue() {
        return discountRate != null ? discountRate.value() : null;
    }

    public Money discountAmount() {
        return discountAmount;
    }

    public Integer discountAmountValue() {
        return discountAmount != null ? discountAmount.value() : null;
    }

    public Money maxDiscountAmount() {
        return maxDiscountAmount;
    }

    public Integer maxDiscountAmountValue() {
        return maxDiscountAmount != null ? maxDiscountAmount.value() : null;
    }

    public boolean isDiscountCapped() {
        return discountCapped;
    }

    public Money minimumOrderAmount() {
        return minimumOrderAmount;
    }

    public Integer minimumOrderAmountValue() {
        return minimumOrderAmount != null ? minimumOrderAmount.value() : null;
    }

    public ApplicationType applicationType() {
        return applicationType;
    }

    public PublisherType publisherType() {
        return publisherType;
    }

    public SellerId sellerId() {
        return sellerId;
    }

    public Long sellerIdValue() {
        return sellerId != null ? sellerId.value() : null;
    }

    public StackingGroup stackingGroup() {
        return stackingGroup;
    }

    public Priority priority() {
        return priority;
    }

    public int priorityValue() {
        return priority.value();
    }

    public DiscountPeriod period() {
        return period;
    }

    public DiscountBudget budget() {
        return budget;
    }

    public boolean isActive() {
        return active;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }

    public List<DiscountTarget> targets() {
        return Collections.unmodifiableList(targets);
    }

    public List<DiscountTarget> activeTargets() {
        return targets.stream().filter(DiscountTarget::isActive).toList();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
