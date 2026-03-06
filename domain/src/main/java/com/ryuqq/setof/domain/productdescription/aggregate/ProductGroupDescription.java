package com.ryuqq.setof.domain.productdescription.aggregate;

import com.ryuqq.setof.domain.productdescription.id.ProductGroupDescriptionId;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProductGroupDescription - 상품그룹 상세설명 Aggregate Root.
 *
 * <p>상품의 상세설명 HTML과 이미지를 관리합니다. lazy load 분리를 위해 ProductGroup과 별도 Aggregate로 설계합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class ProductGroupDescription {

    private final ProductGroupDescriptionId id;
    private final ProductGroupId productGroupId;
    private DescriptionHtml content;
    private String cdnPath;
    private final List<DescriptionImage> images;
    private final Instant createdAt;
    private Instant updatedAt;

    private ProductGroupDescription(
            ProductGroupDescriptionId id,
            ProductGroupId productGroupId,
            DescriptionHtml content,
            String cdnPath,
            List<DescriptionImage> images,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.content = content;
        this.cdnPath = cdnPath;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 상세설명 생성.
     *
     * @param productGroupId 상품그룹 ID (필수)
     * @param content 상세설명 HTML
     * @param cdnPath CDN 경로 (nullable)
     * @param now 생성 시각
     * @return 새 ProductGroupDescription 인스턴스
     */
    public static ProductGroupDescription forNew(
            ProductGroupId productGroupId, DescriptionHtml content, String cdnPath, Instant now) {
        return new ProductGroupDescription(
                ProductGroupDescriptionId.forNew(),
                productGroupId,
                content,
                cdnPath,
                new ArrayList<>(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param productGroupId 상품그룹 ID
     * @param content 상세설명 HTML
     * @param cdnPath CDN 경로
     * @param images 이미지 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 ProductGroupDescription 인스턴스
     */
    public static ProductGroupDescription reconstitute(
            ProductGroupDescriptionId id,
            ProductGroupId productGroupId,
            DescriptionHtml content,
            String cdnPath,
            List<DescriptionImage> images,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductGroupDescription(
                id, productGroupId, content, cdnPath, images, createdAt, updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 수정 데이터 일괄 반영.
     *
     * <p>컨텐츠, CDN 경로, 이미지를 모두 교체합니다.
     *
     * @param updateData 수정 데이터 (컨텐츠, CDN 경로, 새 이미지 목록, 수정 시각)
     */
    public void update(DescriptionUpdateData updateData) {
        this.content = updateData.content();
        this.cdnPath = updateData.cdnPath();
        this.images.clear();
        this.images.addAll(updateData.newImages());
        this.updatedAt = updateData.updatedAt();
    }

    /**
     * 상세설명 내용 수정.
     *
     * @param content 새 상세설명 HTML
     * @param now 수정 시각
     */
    public void updateContent(DescriptionHtml content, Instant now) {
        this.content = content;
        this.updatedAt = now;
    }

    /**
     * CDN 경로 수정.
     *
     * @param cdnPath 새 CDN 경로
     * @param now 수정 시각
     */
    public void updateCdnPath(String cdnPath, Instant now) {
        this.cdnPath = cdnPath;
        this.updatedAt = now;
    }

    /**
     * 이미지 교체.
     *
     * @param newImages 새 이미지 목록
     * @param now 수정 시각
     */
    public void replaceImages(List<DescriptionImage> newImages, Instant now) {
        this.images.clear();
        if (newImages != null) {
            this.images.addAll(newImages);
        }
        this.updatedAt = now;
    }

    /** 내용 비어있는지 확인 */
    public boolean isEmpty() {
        return (content == null || content.isEmpty()) && images.isEmpty();
    }

    /** 이미지 보유 여부 확인 */
    public boolean hasImages() {
        return !images.isEmpty();
    }

    /** CDN 경로 존재 여부 확인 */
    public boolean hasCdnPath() {
        return cdnPath != null && !cdnPath.isBlank();
    }

    // ========== Accessor Methods ==========

    public ProductGroupDescriptionId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public DescriptionHtml content() {
        return content;
    }

    public String contentValue() {
        return content != null ? content.value() : null;
    }

    public String cdnPath() {
        return cdnPath;
    }

    public List<DescriptionImage> images() {
        return Collections.unmodifiableList(images);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
