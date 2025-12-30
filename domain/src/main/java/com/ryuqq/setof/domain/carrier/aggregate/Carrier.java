package com.ryuqq.setof.domain.carrier.aggregate;

import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import com.ryuqq.setof.domain.carrier.vo.CarrierName;
import com.ryuqq.setof.domain.carrier.vo.CarrierStatus;
import java.time.Instant;

/**
 * Carrier Aggregate Root
 *
 * <p>택배사 정보를 나타내는 도메인 엔티티입니다. DB에서 관리되며 동적으로 추가/수정이 가능합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class Carrier {

    private final CarrierId id;
    private final CarrierCode code;
    private final CarrierName name;
    private final CarrierStatus status;
    private final String trackingUrlTemplate;
    private final Integer displayOrder;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Carrier(
            CarrierId id,
            CarrierCode code,
            CarrierName name,
            CarrierStatus status,
            String trackingUrlTemplate,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.trackingUrlTemplate = trackingUrlTemplate;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 택배사 생성
     *
     * @param code 택배사 코드
     * @param name 택배사명
     * @param trackingUrlTemplate 배송 조회 URL 템플릿 (nullable)
     * @param displayOrder 표시 순서 (nullable)
     * @param now 현재 시각
     * @return Carrier 인스턴스
     */
    public static Carrier forNew(
            CarrierCode code,
            CarrierName name,
            String trackingUrlTemplate,
            Integer displayOrder,
            Instant now) {
        return new Carrier(
                null,
                code,
                name,
                CarrierStatus.defaultStatus(),
                trackingUrlTemplate,
                displayOrder,
                now,
                now);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * @param id 택배사 ID
     * @param code 택배사 코드
     * @param name 택배사명
     * @param status 상태
     * @param trackingUrlTemplate 배송 조회 URL 템플릿
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return Carrier 인스턴스
     */
    public static Carrier reconstitute(
            CarrierId id,
            CarrierCode code,
            CarrierName name,
            CarrierStatus status,
            String trackingUrlTemplate,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new Carrier(
                id, code, name, status, trackingUrlTemplate, displayOrder, createdAt, updatedAt);
    }

    // ========== 상태 변경 메서드 ==========

    /**
     * 택배사 정보 수정
     *
     * @param newName 새 택배사명
     * @param newTrackingUrlTemplate 새 배송 조회 URL 템플릿
     * @param newDisplayOrder 새 표시 순서
     * @param now 현재 시각
     * @return 수정된 Carrier 인스턴스
     */
    public Carrier update(
            CarrierName newName,
            String newTrackingUrlTemplate,
            Integer newDisplayOrder,
            Instant now) {
        return new Carrier(
                this.id,
                this.code,
                newName,
                this.status,
                newTrackingUrlTemplate,
                newDisplayOrder,
                this.createdAt,
                now);
    }

    /**
     * 택배사 활성화
     *
     * @param now 현재 시각
     * @return 활성화된 Carrier 인스턴스
     */
    public Carrier activate(Instant now) {
        if (status.isActive()) {
            return this;
        }
        return new Carrier(
                this.id,
                this.code,
                this.name,
                CarrierStatus.ACTIVE,
                this.trackingUrlTemplate,
                this.displayOrder,
                this.createdAt,
                now);
    }

    /**
     * 택배사 비활성화
     *
     * @param now 현재 시각
     * @return 비활성화된 Carrier 인스턴스
     */
    public Carrier deactivate(Instant now) {
        if (!status.isActive()) {
            return this;
        }
        return new Carrier(
                this.id,
                this.code,
                this.name,
                CarrierStatus.INACTIVE,
                this.trackingUrlTemplate,
                this.displayOrder,
                this.createdAt,
                now);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 택배사 ID 값 반환
     *
     * @return 택배사 ID Long 값 (null if new)
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 택배사 코드 값 반환
     *
     * @return 택배사 코드 문자열
     */
    public String getCodeValue() {
        return code.value();
    }

    /**
     * 택배사명 값 반환
     *
     * @return 택배사명 문자열
     */
    public String getNameValue() {
        return name.value();
    }

    /**
     * 상태 이름 반환
     *
     * @return 상태 문자열
     */
    public String getStatusValue() {
        return status.name();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 활성 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    /**
     * 배송 조회 URL 존재 여부 확인
     *
     * @return 배송 조회 URL이 존재하면 true
     */
    public boolean hasTrackingUrl() {
        return trackingUrlTemplate != null && !trackingUrlTemplate.isBlank();
    }

    /**
     * 운송장 번호로 배송 조회 URL 생성
     *
     * @param invoiceNumber 운송장 번호
     * @return 배송 조회 URL (템플릿이 없으면 null)
     */
    public String buildTrackingUrl(String invoiceNumber) {
        if (!hasTrackingUrl()) {
            return null;
        }
        return trackingUrlTemplate.replace("{invoiceNumber}", invoiceNumber);
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 없으면 true (신규)
     */
    public boolean isNew() {
        return id == null;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public CarrierId getId() {
        return id;
    }

    public CarrierCode getCode() {
        return code;
    }

    public CarrierName getName() {
        return name;
    }

    public CarrierStatus getStatus() {
        return status;
    }

    public String getTrackingUrlTemplate() {
        return trackingUrlTemplate;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
