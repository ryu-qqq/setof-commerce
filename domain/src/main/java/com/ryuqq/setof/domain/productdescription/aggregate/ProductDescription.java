package com.ryuqq.setof.domain.productdescription.aggregate;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productdescription.vo.ImageUrl;
import com.ryuqq.setof.domain.productdescription.vo.ProductDescriptionId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ProductDescription Aggregate Root
 *
 * <p>상품 상세설명을 나타내는 도메인 엔티티입니다. HTML 컨텐츠와 이미지 매핑(원본 URL ↔ CDN URL)을 관리합니다.
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
@SuppressWarnings("PMD.DomainLayerDemeterStrict")
public class ProductDescription {

    private static final int MAX_IMAGE_COUNT = 50;

    private final ProductDescriptionId id;
    private final ProductGroupId productGroupId;
    private final HtmlContent htmlContent;
    private final List<DescriptionImage> images;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ProductDescription(
            ProductDescriptionId id,
            ProductGroupId productGroupId,
            HtmlContent htmlContent,
            List<DescriptionImage> images,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.htmlContent = htmlContent;
        this.images = images != null ? List.copyOf(images) : List.of();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 상품설명 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param htmlContent HTML 컨텐츠
     * @param images 이미지 목록
     * @param createdAt 생성일시
     * @return ProductDescription 인스턴스
     */
    public static ProductDescription create(
            ProductGroupId productGroupId,
            HtmlContent htmlContent,
            List<DescriptionImage> images,
            Instant createdAt) {
        validateCreate(productGroupId, images);
        List<DescriptionImage> sortedImages = sortImages(images);
        return new ProductDescription(
                ProductDescriptionId.forNew(),
                productGroupId,
                htmlContent,
                sortedImages,
                createdAt,
                createdAt);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 상품설명 ID
     * @param productGroupId 상품그룹 ID
     * @param htmlContent HTML 컨텐츠
     * @param images 이미지 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return ProductDescription 인스턴스
     */
    public static ProductDescription reconstitute(
            ProductDescriptionId id,
            ProductGroupId productGroupId,
            HtmlContent htmlContent,
            List<DescriptionImage> images,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductDescription(
                id, productGroupId, htmlContent, images, createdAt, updatedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * HTML 컨텐츠 업데이트
     *
     * @param newHtmlContent 새로운 HTML 컨텐츠
     * @param updatedAt 수정일시
     * @return 업데이트된 ProductDescription 인스턴스
     */
    public ProductDescription updateHtmlContent(HtmlContent newHtmlContent, Instant updatedAt) {
        return new ProductDescription(
                this.id,
                this.productGroupId,
                newHtmlContent,
                this.images,
                this.createdAt,
                updatedAt);
    }

    /**
     * 이미지 목록 업데이트
     *
     * @param newImages 새로운 이미지 목록
     * @param updatedAt 수정일시
     * @return 업데이트된 ProductDescription 인스턴스
     */
    public ProductDescription updateImages(List<DescriptionImage> newImages, Instant updatedAt) {
        validateImages(newImages);
        List<DescriptionImage> sortedImages = sortImages(newImages);
        return new ProductDescription(
                this.id,
                this.productGroupId,
                this.htmlContent,
                sortedImages,
                this.createdAt,
                updatedAt);
    }

    /**
     * 이미지 추가
     *
     * @param image 추가할 이미지
     * @param updatedAt 수정일시
     * @return 이미지가 추가된 ProductDescription 인스턴스
     * @throws IllegalArgumentException 이미지 개수 초과 시
     */
    public ProductDescription addImage(DescriptionImage image, Instant updatedAt) {
        if (this.images.size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(
                    String.format("최대 이미지 개수(%d)를 초과할 수 없습니다", MAX_IMAGE_COUNT));
        }
        List<DescriptionImage> newImages = new ArrayList<>(this.images);
        newImages.add(image);
        List<DescriptionImage> sortedImages = sortImages(newImages);
        return new ProductDescription(
                this.id,
                this.productGroupId,
                this.htmlContent,
                sortedImages,
                this.createdAt,
                updatedAt);
    }

    /**
     * 이미지 CDN URL 업데이트
     *
     * @param originUrl 원본 URL
     * @param cdnUrl CDN URL
     * @param updatedAt 수정일시
     * @return CDN URL이 업데이트된 ProductDescription 인스턴스
     */
    public ProductDescription updateImageCdnUrl(
            ImageUrl originUrl, ImageUrl cdnUrl, Instant updatedAt) {
        List<DescriptionImage> updatedImages =
                this.images.stream()
                        .map(
                                image -> {
                                    if (image.originUrl().equals(originUrl)) {
                                        return image.updateCdnUrl(cdnUrl);
                                    }
                                    return image;
                                })
                        .toList();

        HtmlContent updatedHtml = this.htmlContent.replaceUrl(originUrl.value(), cdnUrl.value());

        return new ProductDescription(
                this.id,
                this.productGroupId,
                updatedHtml,
                updatedImages,
                this.createdAt,
                updatedAt);
    }

    /**
     * 전체 업데이트 (HTML + 이미지)
     *
     * @param newHtmlContent 새로운 HTML 컨텐츠
     * @param newImages 새로운 이미지 목록
     * @param updatedAt 수정일시
     * @return 업데이트된 ProductDescription 인스턴스
     */
    public ProductDescription update(
            HtmlContent newHtmlContent, List<DescriptionImage> newImages, Instant updatedAt) {
        validateImages(newImages);
        List<DescriptionImage> sortedImages = sortImages(newImages);
        return new ProductDescription(
                this.id,
                this.productGroupId,
                newHtmlContent,
                sortedImages,
                this.createdAt,
                updatedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 컨텐츠 존재 여부 확인
     *
     * @return HTML 또는 이미지가 있으면 true
     */
    public boolean hasContent() {
        return htmlContent.hasContent() || !images.isEmpty();
    }

    /**
     * 이미지 존재 여부 확인
     *
     * @return 이미지가 있으면 true
     */
    public boolean hasImages() {
        return !images.isEmpty();
    }

    /**
     * 이미지 개수 반환
     *
     * @return 이미지 개수
     */
    public int getImageCount() {
        return images.size();
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    /**
     * 모든 이미지가 CDN 변환되었는지 확인
     *
     * @return 모든 이미지가 CDN 변환되었으면 true
     */
    public boolean allImagesCdnConverted() {
        return images.stream().allMatch(DescriptionImage::isCdnConverted);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 상품설명 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품설명 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 상품그룹 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품그룹 ID Long 값
     */
    public Long getProductGroupIdValue() {
        return productGroupId != null ? productGroupId.value() : null;
    }

    /**
     * HTML 컨텐츠 값 반환 (Law of Demeter 준수)
     *
     * @return HTML 문자열
     */
    public String getHtmlContentValue() {
        return htmlContent != null ? htmlContent.value() : null;
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(
            ProductGroupId productGroupId, List<DescriptionImage> images) {
        if (productGroupId == null) {
            throw new IllegalArgumentException("ProductGroupId is required");
        }
        validateImages(images);
    }

    private static void validateImages(List<DescriptionImage> images) {
        if (images != null && images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(
                    String.format("이미지 개수가 최대 허용치(%d)를 초과합니다: %d", MAX_IMAGE_COUNT, images.size()));
        }
    }

    private static List<DescriptionImage> sortImages(List<DescriptionImage> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .sorted(Comparator.comparingInt(DescriptionImage::displayOrder))
                .toList();
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductDescriptionId getId() {
        return id;
    }

    public ProductGroupId getProductGroupId() {
        return productGroupId;
    }

    public HtmlContent getHtmlContent() {
        return htmlContent;
    }

    public List<DescriptionImage> getImages() {
        return images;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
