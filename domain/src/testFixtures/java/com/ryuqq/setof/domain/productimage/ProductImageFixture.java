package com.ryuqq.setof.domain.productimage;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import com.ryuqq.setof.domain.productimage.vo.ProductImageId;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

/**
 * ProductImage 도메인 테스트 Fixture
 *
 * <p>테스트에서 사용되는 ProductImage 관련 객체 생성 유틸리티
 */
public final class ProductImageFixture {

    private ProductImageFixture() {
        // Utility class
    }

    // ========== Default Values ==========
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_ORIGIN_URL = "https://origin.example.com/image1.jpg";
    public static final String DEFAULT_CDN_URL = "https://cdn.example.com/image1.jpg";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final Instant DEFAULT_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ========== ProductImageId ==========

    public static ProductImageId createId() {
        return ProductImageId.of(DEFAULT_ID);
    }

    public static ProductImageId createId(Long value) {
        return ProductImageId.of(value);
    }

    public static ProductImageId createNewId() {
        return ProductImageId.forNew();
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

    // ========== ProductImage ==========

    public static ProductImage createMain() {
        return ProductImage.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ImageType.MAIN,
                createOriginUrl(),
                createCdnUrl(),
                1,
                DEFAULT_NOW);
    }

    public static ProductImage createSub(int displayOrder) {
        return ProductImage.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ImageType.SUB,
                ImageUrl.of("https://origin.example.com/sub" + displayOrder + ".jpg"),
                ImageUrl.of("https://cdn.example.com/sub" + displayOrder + ".jpg"),
                displayOrder,
                DEFAULT_NOW);
    }

    public static ProductImage createDetail(int displayOrder) {
        return ProductImage.create(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ImageType.DETAIL,
                ImageUrl.of("https://origin.example.com/detail" + displayOrder + ".jpg"),
                ImageUrl.of("https://cdn.example.com/detail" + displayOrder + ".jpg"),
                displayOrder,
                DEFAULT_NOW);
    }

    public static ProductImage reconstitute() {
        return ProductImage.reconstitute(
                createId(),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ImageType.MAIN,
                createOriginUrl(),
                createCdnUrl(),
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_NOW);
    }

    public static ProductImage reconstitute(Long id) {
        return ProductImage.reconstitute(
                ProductImageId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ImageType.MAIN,
                createOriginUrl(),
                createCdnUrl(),
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_NOW);
    }

    public static ProductImage reconstitute(Long id, ImageType imageType) {
        return ProductImage.reconstitute(
                ProductImageId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                imageType,
                createOriginUrl(),
                createCdnUrl(),
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_NOW);
    }

    /**
     * 상품그룹의 이미지 세트 생성 (메인 1개 + 서브 N개)
     *
     * @param subCount 서브 이미지 개수
     * @return ProductImage 리스트
     */
    public static List<ProductImage> createImageSet(int subCount) {
        List<ProductImage> images = new java.util.ArrayList<>();
        images.add(createMain());
        IntStream.rangeClosed(2, subCount + 1).forEach(i -> images.add(createSub(i)));
        return images;
    }

    // ========== Builder Pattern ==========

    public static ProductImageBuilder builder() {
        return new ProductImageBuilder();
    }

    public static class ProductImageBuilder {
        private ProductImageId id = createNewId();
        private ProductGroupId productGroupId = ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID);
        private ImageType imageType = ImageType.MAIN;
        private ImageUrl originUrl = createOriginUrl();
        private ImageUrl cdnUrl = createCdnUrl();
        private int displayOrder = DEFAULT_DISPLAY_ORDER;
        private Instant createdAt = DEFAULT_NOW;

        public ProductImageBuilder id(Long id) {
            this.id = ProductImageId.of(id);
            return this;
        }

        public ProductImageBuilder productGroupId(Long productGroupId) {
            this.productGroupId = ProductGroupId.of(productGroupId);
            return this;
        }

        public ProductImageBuilder imageType(ImageType imageType) {
            this.imageType = imageType;
            return this;
        }

        public ProductImageBuilder originUrl(String originUrl) {
            this.originUrl = ImageUrl.of(originUrl);
            return this;
        }

        public ProductImageBuilder cdnUrl(String cdnUrl) {
            this.cdnUrl = ImageUrl.of(cdnUrl);
            return this;
        }

        public ProductImageBuilder displayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public ProductImageBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductImage build() {
            return ProductImage.reconstitute(
                    id, productGroupId, imageType, originUrl, cdnUrl, displayOrder, createdAt);
        }

        public ProductImage buildNew() {
            return ProductImage.create(
                    productGroupId, imageType, originUrl, cdnUrl, displayOrder, createdAt);
        }
    }
}
