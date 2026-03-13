package com.ryuqq.setof.adapter.in.rest.admin.productgroupimage;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.RegisterProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.UpdateProductGroupImagesApiRequest;
import java.util.List;

/**
 * ProductGroupImage API 테스트 Fixtures.
 *
 * <p>상품 그룹 이미지 API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupImageApiFixtures {

    private ProductGroupImageApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final String DEFAULT_IMAGE_TYPE_THUMBNAIL = "THUMBNAIL";
    public static final String DEFAULT_IMAGE_TYPE_DETAIL = "DETAIL";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/image.jpg";
    public static final int DEFAULT_SORT_ORDER = 0;

    // ===== Register Request Fixtures =====

    public static RegisterProductGroupImagesApiRequest registerRequest() {
        return new RegisterProductGroupImagesApiRequest(
                List.of(
                        new RegisterProductGroupImagesApiRequest.ImageApiRequest(
                                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                                DEFAULT_IMAGE_URL,
                                DEFAULT_SORT_ORDER),
                        new RegisterProductGroupImagesApiRequest.ImageApiRequest(
                                DEFAULT_IMAGE_TYPE_DETAIL,
                                "https://example.com/detail-image.jpg",
                                1)));
    }

    public static RegisterProductGroupImagesApiRequest registerRequest(
            List<RegisterProductGroupImagesApiRequest.ImageApiRequest> images) {
        return new RegisterProductGroupImagesApiRequest(images);
    }

    public static RegisterProductGroupImagesApiRequest.ImageApiRequest registerImageRequest() {
        return new RegisterProductGroupImagesApiRequest.ImageApiRequest(
                DEFAULT_IMAGE_TYPE_THUMBNAIL, DEFAULT_IMAGE_URL, DEFAULT_SORT_ORDER);
    }

    public static RegisterProductGroupImagesApiRequest.ImageApiRequest registerImageRequest(
            String imageType, String imageUrl, int sortOrder) {
        return new RegisterProductGroupImagesApiRequest.ImageApiRequest(
                imageType, imageUrl, sortOrder);
    }

    // ===== Update Request Fixtures =====

    public static UpdateProductGroupImagesApiRequest updateRequest() {
        return new UpdateProductGroupImagesApiRequest(
                List.of(
                        new UpdateProductGroupImagesApiRequest.ImageApiRequest(
                                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                                "https://example.com/updated-thumbnail.jpg",
                                DEFAULT_SORT_ORDER),
                        new UpdateProductGroupImagesApiRequest.ImageApiRequest(
                                DEFAULT_IMAGE_TYPE_DETAIL,
                                "https://example.com/updated-detail.jpg",
                                1)));
    }

    public static UpdateProductGroupImagesApiRequest updateRequest(
            List<UpdateProductGroupImagesApiRequest.ImageApiRequest> images) {
        return new UpdateProductGroupImagesApiRequest(images);
    }

    public static UpdateProductGroupImagesApiRequest.ImageApiRequest updateImageRequest() {
        return new UpdateProductGroupImagesApiRequest.ImageApiRequest(
                DEFAULT_IMAGE_TYPE_THUMBNAIL,
                "https://example.com/updated-thumbnail.jpg",
                DEFAULT_SORT_ORDER);
    }

    public static UpdateProductGroupImagesApiRequest.ImageApiRequest updateImageRequest(
            String imageType, String imageUrl, int sortOrder) {
        return new UpdateProductGroupImagesApiRequest.ImageApiRequest(
                imageType, imageUrl, sortOrder);
    }
}
