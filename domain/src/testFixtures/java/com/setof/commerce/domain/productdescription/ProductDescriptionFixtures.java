package com.setof.commerce.domain.productdescription;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.id.DescriptionImageId;
import com.ryuqq.setof.domain.productdescription.id.ProductGroupDescriptionId;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;

/**
 * ProductDescription 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroupDescription 관련 객체들을 생성합니다.
 */
public final class ProductDescriptionFixtures {

    private ProductDescriptionFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_DESCRIPTION_HTML = "<p>상품 상세설명입니다</p>";
    public static final String DEFAULT_IMAGE_URL = "https://cdn.example.com/desc/image1.jpg";
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_CDN_PATH = "https://cdn.example.com/descriptions/100";

    // ===== ProductGroupDescription Aggregate Fixtures =====

    /** 신규 상세설명 생성 (기본 ProductGroupId 사용). */
    public static ProductGroupDescription newDescription() {
        return ProductGroupDescription.forNew(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                defaultDescriptionHtml(),
                DEFAULT_CDN_PATH,
                CommonVoFixtures.now());
    }

    /** 신규 상세설명 생성 (특정 ProductGroupId 사용). */
    public static ProductGroupDescription newDescription(ProductGroupId productGroupId) {
        return ProductGroupDescription.forNew(
                productGroupId, defaultDescriptionHtml(), DEFAULT_CDN_PATH, CommonVoFixtures.now());
    }

    /** 활성 상태의 상세설명 복원 (ID와 컨텐츠 포함). */
    public static ProductGroupDescription activeDescription() {
        return ProductGroupDescription.reconstitute(
                ProductGroupDescriptionId.of(1L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                defaultDescriptionHtml(),
                DEFAULT_CDN_PATH,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 활성 상태의 상세설명 복원 (특정 ID 사용). */
    public static ProductGroupDescription activeDescription(Long id) {
        return ProductGroupDescription.reconstitute(
                ProductGroupDescriptionId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                defaultDescriptionHtml(),
                DEFAULT_CDN_PATH,
                defaultImages(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 빈 상세설명 복원 (null 컨텐츠, 이미지 없음). */
    public static ProductGroupDescription emptyDescription() {
        return ProductGroupDescription.reconstitute(
                ProductGroupDescriptionId.of(2L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DescriptionHtml.empty(),
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== VO Fixtures =====

    /** 기본 DescriptionHtml VO 생성. */
    public static DescriptionHtml defaultDescriptionHtml() {
        return DescriptionHtml.of(DEFAULT_DESCRIPTION_HTML);
    }

    // ===== DescriptionUpdateData Fixtures =====

    /** 기본 DescriptionUpdateData VO 생성. */
    public static DescriptionUpdateData defaultDescriptionUpdateData() {
        return DescriptionUpdateData.of(
                defaultDescriptionHtml(),
                DEFAULT_CDN_PATH,
                defaultImages(),
                CommonVoFixtures.now());
    }

    /** 이미지 없는 DescriptionUpdateData VO 생성. */
    public static DescriptionUpdateData descriptionUpdateDataWithoutImages() {
        return DescriptionUpdateData.of(
                defaultDescriptionHtml(), DEFAULT_CDN_PATH, List.of(), CommonVoFixtures.now());
    }

    // ===== DescriptionImage Fixtures =====

    /** 기본 DescriptionImage 생성 (sortOrder=0). */
    public static DescriptionImage descriptionImage() {
        return DescriptionImage.forNew(DEFAULT_IMAGE_URL, 0);
    }

    /** 특정 sortOrder의 DescriptionImage 생성. */
    public static DescriptionImage descriptionImage(int sortOrder) {
        return DescriptionImage.forNew(DEFAULT_IMAGE_URL, sortOrder);
    }

    /** 기본 이미지 목록 생성 (3개). */
    public static List<DescriptionImage> defaultImages() {
        return List.of(
                DescriptionImage.reconstitute(
                        DescriptionImageId.of(1L), "https://cdn.example.com/desc/image1.jpg", 0),
                DescriptionImage.reconstitute(
                        DescriptionImageId.of(2L), "https://cdn.example.com/desc/image2.jpg", 1),
                DescriptionImage.reconstitute(
                        DescriptionImageId.of(3L), "https://cdn.example.com/desc/image3.jpg", 2));
    }
}
