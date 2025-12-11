package com.ryuqq.setof.domain.brand.aggregate;

import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandLogoUrl;
import com.ryuqq.setof.domain.brand.vo.BrandNameEn;
import com.ryuqq.setof.domain.brand.vo.BrandNameKo;
import com.ryuqq.setof.domain.brand.vo.BrandStatus;
import java.time.Instant;

/**
 * Brand Aggregate Root
 *
 * <p>브랜드 정보를 나타내는 도메인 엔티티입니다. MarketPlace에서 배치로 동기화되며, 이 프로젝트에서는 조회만 수행합니다.
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
public class Brand {

    private final BrandId id;
    private final BrandCode code;
    private final BrandNameKo nameKo;
    private final BrandNameEn nameEn;
    private final BrandLogoUrl logoUrl;
    private final BrandStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Brand(
            BrandId id,
            BrandCode code,
            BrandNameKo nameKo,
            BrandNameEn nameEn,
            BrandLogoUrl logoUrl,
            BrandStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.nameKo = nameKo;
        this.nameEn = nameEn;
        this.logoUrl = logoUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 브랜드 ID
     * @param code 브랜드 코드
     * @param nameKo 한글 브랜드명
     * @param nameEn 영문 브랜드명 (nullable)
     * @param logoUrl 로고 URL (nullable)
     * @param status 브랜드 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return Brand 인스턴스
     */
    public static Brand reconstitute(
            BrandId id,
            BrandCode code,
            BrandNameKo nameKo,
            BrandNameEn nameEn,
            BrandLogoUrl logoUrl,
            BrandStatus status,
            Instant createdAt,
            Instant updatedAt) {
        return new Brand(id, code, nameKo, nameEn, logoUrl, status, createdAt, updatedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 브랜드 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 브랜드 ID Long 값
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 브랜드 코드 값 반환 (Law of Demeter 준수)
     *
     * @return 브랜드 코드 문자열
     */
    public String getCodeValue() {
        return code.value();
    }

    /**
     * 한글 브랜드명 값 반환 (Law of Demeter 준수)
     *
     * @return 한글 브랜드명 문자열
     */
    public String getNameKoValue() {
        return nameKo.value();
    }

    /**
     * 영문 브랜드명 값 반환 (Law of Demeter 준수)
     *
     * @return 영문 브랜드명 문자열 (nullable)
     */
    public String getNameEnValue() {
        return nameEn.value();
    }

    /**
     * 로고 URL 값 반환 (Law of Demeter 준수)
     *
     * @return 로고 URL 문자열 (nullable)
     */
    public String getLogoUrlValue() {
        return logoUrl.value();
    }

    /**
     * 브랜드 상태 이름 반환 (Law of Demeter 준수)
     *
     * @return 브랜드 상태 문자열
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
     * 영문명 존재 여부 확인
     *
     * @return 영문 브랜드명이 존재하면 true
     */
    public boolean hasNameEn() {
        return nameEn.hasValue();
    }

    /**
     * 로고 URL 존재 여부 확인
     *
     * @return 로고 URL이 존재하면 true
     */
    public boolean hasLogoUrl() {
        return logoUrl.hasValue();
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public BrandId getId() {
        return id;
    }

    public BrandCode getCode() {
        return code;
    }

    public BrandNameKo getNameKo() {
        return nameKo;
    }

    public BrandNameEn getNameEn() {
        return nameEn;
    }

    public BrandLogoUrl getLogoUrl() {
        return logoUrl;
    }

    public BrandStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
