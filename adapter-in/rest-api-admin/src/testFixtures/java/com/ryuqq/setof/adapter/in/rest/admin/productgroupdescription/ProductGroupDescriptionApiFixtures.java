package com.ryuqq.setof.adapter.in.rest.admin.productgroupdescription;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.RegisterProductGroupDescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.UpdateProductGroupDescriptionApiRequest;
import java.util.List;

/**
 * ProductGroupDescription API 테스트 Fixtures.
 *
 * <p>상품 그룹 상세 설명 API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupDescriptionApiFixtures {

    private ProductGroupDescriptionApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final String DEFAULT_CONTENT = "<p>상품 상세 설명입니다.</p>";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/desc-image.jpg";
    public static final int DEFAULT_SORT_ORDER = 0;

    // ===== Register Request Fixtures =====

    public static RegisterProductGroupDescriptionApiRequest registerRequest() {
        return new RegisterProductGroupDescriptionApiRequest(
                DEFAULT_CONTENT,
                List.of(
                        new RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest(
                                DEFAULT_IMAGE_URL, DEFAULT_SORT_ORDER)));
    }

    public static RegisterProductGroupDescriptionApiRequest registerRequestWithoutImages() {
        return new RegisterProductGroupDescriptionApiRequest(DEFAULT_CONTENT, null);
    }

    public static RegisterProductGroupDescriptionApiRequest registerRequest(
            String content,
            List<RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest> images) {
        return new RegisterProductGroupDescriptionApiRequest(content, images);
    }

    public static RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest
            registerDescriptionImageRequest() {
        return new RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest(
                DEFAULT_IMAGE_URL, DEFAULT_SORT_ORDER);
    }

    public static RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest
            registerDescriptionImageRequest(String imageUrl, int sortOrder) {
        return new RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest(
                imageUrl, sortOrder);
    }

    // ===== Update Request Fixtures =====

    public static UpdateProductGroupDescriptionApiRequest updateRequest() {
        return new UpdateProductGroupDescriptionApiRequest(
                "<p>수정된 상품 상세 설명입니다.</p>",
                List.of(
                        new UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest(
                                "https://example.com/updated-desc-image.jpg", DEFAULT_SORT_ORDER)));
    }

    public static UpdateProductGroupDescriptionApiRequest updateRequestWithoutImages() {
        return new UpdateProductGroupDescriptionApiRequest("<p>수정된 상품 상세 설명입니다.</p>", null);
    }

    public static UpdateProductGroupDescriptionApiRequest updateRequest(
            String content,
            List<UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest> images) {
        return new UpdateProductGroupDescriptionApiRequest(content, images);
    }

    public static UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest
            updateDescriptionImageRequest() {
        return new UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest(
                "https://example.com/updated-desc-image.jpg", DEFAULT_SORT_ORDER);
    }
}
