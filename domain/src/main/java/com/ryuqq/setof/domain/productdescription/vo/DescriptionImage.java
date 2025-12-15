package com.ryuqq.setof.domain.productdescription.vo;

import java.time.Instant;

/**
 * 상품설명 이미지 Value Object
 *
 * <p>상품 상세설명에 포함되는 이미지 정보를 나타냅니다. 원본 URL과 CDN URL을 모두 저장하여 매핑을 관리합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Self-validation - Compact Constructor에서 검증
 * </ul>
 *
 * @param displayOrder 표시 순서 (1부터 시작)
 * @param originUrl 원본 이미지 URL
 * @param cdnUrl CDN 이미지 URL
 * @param uploadedAt 업로드 일시
 */
public record DescriptionImage(
        int displayOrder, ImageUrl originUrl, ImageUrl cdnUrl, Instant uploadedAt) {

    /** Compact Constructor - 검증 로직 */
    public DescriptionImage {
        validate(displayOrder, originUrl, cdnUrl, uploadedAt);
    }

    /**
     * Static Factory Method - 이미지 정보로 생성
     *
     * @param displayOrder 표시 순서
     * @param originUrl 원본 URL
     * @param cdnUrl CDN URL
     * @param uploadedAt 업로드 일시
     * @return DescriptionImage 인스턴스
     */
    public static DescriptionImage of(
            int displayOrder, ImageUrl originUrl, ImageUrl cdnUrl, Instant uploadedAt) {
        return new DescriptionImage(displayOrder, originUrl, cdnUrl, uploadedAt);
    }

    /**
     * Static Factory Method - 원본 URL만으로 생성 (CDN URL은 동일)
     *
     * @param displayOrder 표시 순서
     * @param originUrl 원본 URL
     * @param uploadedAt 업로드 일시
     * @return DescriptionImage 인스턴스
     */
    public static DescriptionImage withSameUrl(
            int displayOrder, ImageUrl originUrl, Instant uploadedAt) {
        return new DescriptionImage(displayOrder, originUrl, originUrl, uploadedAt);
    }

    /**
     * CDN URL 업데이트하여 새 인스턴스 반환
     *
     * @param newCdnUrl 새 CDN URL
     * @return CDN URL이 업데이트된 새 DescriptionImage 인스턴스
     */
    public DescriptionImage updateCdnUrl(ImageUrl newCdnUrl) {
        return new DescriptionImage(this.displayOrder, this.originUrl, newCdnUrl, this.uploadedAt);
    }

    /**
     * 표시 순서 변경하여 새 인스턴스 반환
     *
     * @param newOrder 새 표시 순서
     * @return 순서가 변경된 새 DescriptionImage 인스턴스
     */
    public DescriptionImage changeOrder(int newOrder) {
        if (newOrder <= 0) {
            throw new IllegalArgumentException("표시 순서는 1 이상이어야 합니다: " + newOrder);
        }
        return new DescriptionImage(newOrder, this.originUrl, this.cdnUrl, this.uploadedAt);
    }

    /**
     * 원본 URL 값 반환 (Law of Demeter 준수)
     *
     * @return 원본 URL 문자열
     */
    public String getOriginUrlValue() {
        return originUrl != null ? originUrl.value() : null;
    }

    /**
     * CDN URL 값 반환 (Law of Demeter 준수)
     *
     * @return CDN URL 문자열
     */
    public String getCdnUrlValue() {
        return cdnUrl != null ? cdnUrl.value() : null;
    }

    /**
     * CDN으로 변환되었는지 확인
     *
     * @return 원본 URL과 CDN URL이 다르면 true
     */
    public boolean isCdnConverted() {
        if (originUrl == null || cdnUrl == null) {
            return false;
        }
        return !originUrl.value().equals(cdnUrl.value());
    }

    private static void validate(
            int displayOrder, ImageUrl originUrl, ImageUrl cdnUrl, Instant uploadedAt) {
        if (displayOrder <= 0) {
            throw new IllegalArgumentException("표시 순서는 1 이상이어야 합니다: " + displayOrder);
        }
        if (originUrl == null) {
            throw new IllegalArgumentException("원본 URL은 필수입니다");
        }
        if (cdnUrl == null) {
            throw new IllegalArgumentException("CDN URL은 필수입니다");
        }
        if (uploadedAt == null) {
            throw new IllegalArgumentException("업로드 일시는 필수입니다");
        }
    }
}
