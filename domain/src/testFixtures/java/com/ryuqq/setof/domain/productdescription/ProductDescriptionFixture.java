package com.ryuqq.setof.domain.productdescription;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productdescription.vo.ImageUrl;
import com.ryuqq.setof.domain.productdescription.vo.ProductDescriptionId;
import java.time.Instant;
import java.util.List;

/**
 * ProductDescription 도메인 테스트 Fixture
 *
 * <p>테스트에서 사용되는 ProductDescription 관련 객체 생성 유틸리티
 */
public final class ProductDescriptionFixture {

    private ProductDescriptionFixture() {
        // Utility class
    }

    // ========== Default Values ==========
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_HTML_CONTENT = "<p>상품 상세 설명입니다.</p>";
    public static final String DEFAULT_ORIGIN_URL = "https://origin.example.com/image1.jpg";
    public static final String DEFAULT_CDN_URL = "https://cdn.example.com/image1.jpg";
    public static final Instant DEFAULT_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ========== ProductDescriptionId ==========

    public static ProductDescriptionId createId() {
        return ProductDescriptionId.of(DEFAULT_ID);
    }

    public static ProductDescriptionId createId(Long value) {
        return ProductDescriptionId.of(value);
    }

    public static ProductDescriptionId createNewId() {
        return ProductDescriptionId.forNew();
    }

    // ========== ImageUrl ==========

    public static ImageUrl createOriginUrl() {
        return ImageUrl.of(DEFAULT_ORIGIN_URL);
    }

    public static ImageUrl createOriginUrl(String url) {
        return ImageUrl.of(url);
    }

    public static ImageUrl createCdnUrl() {
        return ImageUrl.of(DEFAULT_CDN_URL);
    }

    public static ImageUrl createCdnUrl(String url) {
        return ImageUrl.of(url);
    }

    // ========== HtmlContent ==========

    public static HtmlContent createHtmlContent() {
        return HtmlContent.of(DEFAULT_HTML_CONTENT);
    }

    public static HtmlContent createHtmlContent(String content) {
        return HtmlContent.of(content);
    }

    public static HtmlContent createEmptyHtmlContent() {
        return HtmlContent.empty();
    }

    // ========== DescriptionImage ==========

    public static DescriptionImage createDescriptionImage() {
        return DescriptionImage.of(1, createOriginUrl(), createCdnUrl(), DEFAULT_NOW);
    }

    public static DescriptionImage createDescriptionImage(int displayOrder) {
        return DescriptionImage.of(
                displayOrder,
                ImageUrl.of("https://origin.example.com/image" + displayOrder + ".jpg"),
                ImageUrl.of("https://cdn.example.com/image" + displayOrder + ".jpg"),
                DEFAULT_NOW);
    }

    public static DescriptionImage createDescriptionImage(
            int displayOrder, String originUrl, String cdnUrl) {
        return DescriptionImage.of(
                displayOrder, ImageUrl.of(originUrl), ImageUrl.of(cdnUrl), DEFAULT_NOW);
    }

    public static List<DescriptionImage> createDescriptionImages(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(ProductDescriptionFixture::createDescriptionImage)
                .toList();
    }

    // ========== ProductDescription ==========

    public static ProductDescription create() {
        return ProductDescription.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                createHtmlContent(),
                createDescriptionImages(3),
                DEFAULT_NOW);
    }

    public static ProductDescription createWithoutImages() {
        return ProductDescription.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                createHtmlContent(),
                List.of(),
                DEFAULT_NOW);
    }

    public static ProductDescription createWithHtmlContent(String htmlContent) {
        return ProductDescription.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                HtmlContent.of(htmlContent),
                createDescriptionImages(3),
                DEFAULT_NOW);
    }

    public static ProductDescription reconstitute() {
        return ProductDescription.reconstitute(
                createId(),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                createHtmlContent(),
                createDescriptionImages(3),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    public static ProductDescription reconstitute(Long id) {
        return ProductDescription.reconstitute(
                ProductDescriptionId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                createHtmlContent(),
                createDescriptionImages(3),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    public static ProductDescription reconstitute(Long id, Long productGroupId) {
        return ProductDescription.reconstitute(
                ProductDescriptionId.of(id),
                ProductGroupId.of(productGroupId),
                createHtmlContent(),
                createDescriptionImages(3),
                DEFAULT_NOW,
                DEFAULT_NOW);
    }

    // ========== Builder Pattern ==========

    public static ProductDescriptionBuilder builder() {
        return new ProductDescriptionBuilder();
    }

    public static class ProductDescriptionBuilder {
        private ProductDescriptionId id = createNewId();
        private ProductGroupId productGroupId = ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID);
        private HtmlContent htmlContent = createHtmlContent();
        private List<DescriptionImage> images = createDescriptionImages(3);
        private Instant createdAt = DEFAULT_NOW;
        private Instant updatedAt = DEFAULT_NOW;

        public ProductDescriptionBuilder id(Long id) {
            this.id = ProductDescriptionId.of(id);
            return this;
        }

        public ProductDescriptionBuilder productGroupId(Long productGroupId) {
            this.productGroupId = ProductGroupId.of(productGroupId);
            return this;
        }

        public ProductDescriptionBuilder htmlContent(String htmlContent) {
            this.htmlContent = HtmlContent.of(htmlContent);
            return this;
        }

        public ProductDescriptionBuilder images(List<DescriptionImage> images) {
            this.images = images;
            return this;
        }

        public ProductDescriptionBuilder imageCount(int count) {
            this.images = createDescriptionImages(count);
            return this;
        }

        public ProductDescriptionBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductDescriptionBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProductDescription build() {
            return ProductDescription.reconstitute(
                    id, productGroupId, htmlContent, images, createdAt, updatedAt);
        }

        public ProductDescription buildNew() {
            return ProductDescription.create(productGroupId, htmlContent, images, createdAt);
        }
    }
}
