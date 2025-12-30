package com.ryuqq.setof.domain.faq.aggregate;

import com.ryuqq.setof.domain.faq.exception.InvalidFaqCategoryStatusException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;
import java.time.Instant;

/**
 * FAQ 카테고리 Aggregate Root
 *
 * <p>FAQ 카테고리 도메인의 핵심 Aggregate Root입니다. FAQ를 그룹핑하는 카테고리 정보를 관리합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>Private 생성자 + Static Factory
 *   <li>Law of Demeter - Getter 체이닝 금지 (Helper 메서드 제공)
 *   <li>Tell, Don't Ask - 상태 변경 메서드 제공
 * </ul>
 */
public class FaqCategory {

    private final FaqCategoryId id;
    private FaqCategoryCode code;
    private String name;
    private String description;
    private int displayOrder;
    private FaqCategoryStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    /** Private 생성자 - Static Factory 통해서만 생성 */
    private FaqCategory(
            FaqCategoryId id,
            FaqCategoryCode code,
            String name,
            String description,
            int displayOrder,
            FaqCategoryStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // ===== Static Factory Methods =====

    /**
     * 신규 FAQ 카테고리 생성 (ACTIVE 상태로 시작)
     *
     * @param code 카테고리 코드
     * @param name 카테고리명
     * @param description 설명
     * @param displayOrder 표시 순서
     * @param now 현재 시간
     * @return 새로운 FaqCategory 인스턴스
     */
    public static FaqCategory forNew(
            FaqCategoryCode code, String name, String description, int displayOrder, Instant now) {
        validateName(name);
        return new FaqCategory(
                FaqCategoryId.forNew(),
                code,
                name,
                description,
                displayOrder,
                FaqCategoryStatus.ACTIVE,
                now,
                now,
                null);
    }

    /**
     * DB에서 조회한 데이터로 FAQ 카테고리 복원
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param name 카테고리명
     * @param description 설명
     * @param displayOrder 표시 순서
     * @param status 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @return 복원된 FaqCategory 인스턴스
     */
    public static FaqCategory reconstitute(
            FaqCategoryId id,
            FaqCategoryCode code,
            String name,
            String description,
            int displayOrder,
            FaqCategoryStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new FaqCategory(
                id, code, name, description, displayOrder, status, createdAt, updatedAt, deletedAt);
    }

    // ===== Business Methods (Tell, Don't Ask) =====

    /**
     * 카테고리 활성화
     *
     * @param now 현재 시간
     * @throws InvalidFaqCategoryStatusException 활성화할 수 없는 상태인 경우
     */
    public void activate(Instant now) {
        validateNotDeleted();
        if (!status.canActivate()) {
            throw InvalidFaqCategoryStatusException.cannotActivate(status);
        }
        if (status == FaqCategoryStatus.ACTIVE) {
            throw InvalidFaqCategoryStatusException.alreadyActive();
        }
        this.status = FaqCategoryStatus.ACTIVE;
        this.updatedAt = now;
    }

    /**
     * 카테고리 비활성화
     *
     * @param now 현재 시간
     * @throws InvalidFaqCategoryStatusException 비활성화할 수 없는 상태인 경우
     */
    public void deactivate(Instant now) {
        validateNotDeleted();
        if (!status.canDeactivate()) {
            throw InvalidFaqCategoryStatusException.cannotDeactivate(status);
        }
        if (status == FaqCategoryStatus.INACTIVE) {
            throw InvalidFaqCategoryStatusException.alreadyInactive();
        }
        this.status = FaqCategoryStatus.INACTIVE;
        this.updatedAt = now;
    }

    /**
     * 카테고리 정보 수정
     *
     * @param name 새로운 이름
     * @param description 새로운 설명
     * @param now 현재 시간
     */
    public void updateInfo(String name, String description, Instant now) {
        validateNotDeleted();
        validateName(name);
        this.name = name;
        this.description = description;
        this.updatedAt = now;
    }

    /**
     * 표시 순서 변경
     *
     * @param displayOrder 새로운 표시 순서
     * @param now 현재 시간
     */
    public void updateDisplayOrder(int displayOrder, Instant now) {
        validateNotDeleted();
        if (displayOrder < 0) {
            throw new IllegalArgumentException("표시 순서는 0 이상이어야 합니다: " + displayOrder);
        }
        this.displayOrder = displayOrder;
        this.updatedAt = now;
    }

    /**
     * 카테고리 코드 변경
     *
     * @param newCode 새로운 코드
     * @param now 현재 시간
     */
    public void changeCode(FaqCategoryCode newCode, Instant now) {
        validateNotDeleted();
        this.code = newCode;
        this.updatedAt = now;
    }

    /**
     * Soft Delete 처리
     *
     * @param now 현재 시간
     * @throws InvalidFaqCategoryStatusException 이미 삭제된 경우
     */
    public void softDelete(Instant now) {
        if (isDeleted()) {
            throw InvalidFaqCategoryStatusException.alreadyDeleted();
        }
        this.deletedAt = now;
        this.updatedAt = now;
    }

    // ===== Query Methods =====

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 표시 가능 여부 확인 (활성 상태이고 삭제되지 않음)
     *
     * @return 표시 가능하면 true
     */
    public boolean isDisplayable() {
        return !isDeleted() && status.isDisplayable();
    }

    /**
     * 활성 상태 여부 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return status == FaqCategoryStatus.ACTIVE;
    }

    // ===== Validation Helper =====

    private void validateNotDeleted() {
        if (isDeleted()) {
            throw InvalidFaqCategoryStatusException.alreadyDeleted();
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리명은 필수입니다");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("카테고리명은 100자 이하여야 합니다: " + name.length());
        }
    }

    // ===== Law of Demeter Helper Methods =====

    /**
     * ID 값 반환 (Law of Demeter)
     *
     * @return 카테고리 ID 값 (Long)
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 코드 값 반환 (Law of Demeter)
     *
     * @return 카테고리 코드 값 (String)
     */
    public String getCodeValue() {
        return code.value();
    }

    // ===== Standard Getters =====

    public FaqCategoryId getId() {
        return id;
    }

    public FaqCategoryCode getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public FaqCategoryStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
