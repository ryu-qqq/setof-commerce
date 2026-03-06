package com.setof.commerce.domain.productgroupimage;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImageDiff;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImageUpdateData;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImages;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroupImage 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ProductGroupImage 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupImageFixtures {

    private ProductGroupImageFixtures() {}

    // ===== 상수 =====
    public static final long DEFAULT_IMAGE_ID = 1L;
    public static final long DEFAULT_DETAIL_IMAGE_ID = 2L;
    public static final String DEFAULT_THUMBNAIL_URL = "https://example.com/thumbnail.png";
    public static final String DEFAULT_DETAIL_URL = "https://example.com/detail.png";

    // ===== ID Fixtures =====

    /** 기본 썸네일 이미지 ID */
    public static ProductGroupImageId defaultThumbnailImageId() {
        return ProductGroupImageId.of(DEFAULT_IMAGE_ID);
    }

    /** 기본 상세 이미지 ID */
    public static ProductGroupImageId defaultDetailImageId() {
        return ProductGroupImageId.of(DEFAULT_DETAIL_IMAGE_ID);
    }

    /** 신규 이미지 ID (값 없음) */
    public static ProductGroupImageId newImageId() {
        return ProductGroupImageId.forNew();
    }

    // ===== ProductGroupImage Fixtures =====

    /** 신규 썸네일 이미지 생성 (ID 없음) */
    public static ProductGroupImage newThumbnailImage() {
        return ProductGroupImage.forNew(ImageType.THUMBNAIL, ImageUrl.of(DEFAULT_THUMBNAIL_URL), 0);
    }

    /** 신규 상세 이미지 생성 (ID 없음) */
    public static ProductGroupImage newDetailImage() {
        return ProductGroupImage.forNew(ImageType.DETAIL, ImageUrl.of(DEFAULT_DETAIL_URL), 1);
    }

    /** 신규 상세 이미지 생성 - 사용자 정의 URL */
    public static ProductGroupImage newDetailImage(String url, int sortOrder) {
        return ProductGroupImage.forNew(ImageType.DETAIL, ImageUrl.of(url), sortOrder);
    }

    /** 영속성에서 복원된 썸네일 이미지 */
    public static ProductGroupImage persistedThumbnailImage() {
        return ProductGroupImage.reconstitute(
                defaultThumbnailImageId(),
                ImageType.THUMBNAIL,
                ImageUrl.of(DEFAULT_THUMBNAIL_URL),
                0);
    }

    /** 영속성에서 복원된 상세 이미지 */
    public static ProductGroupImage persistedDetailImage() {
        return ProductGroupImage.reconstitute(
                defaultDetailImageId(), ImageType.DETAIL, ImageUrl.of(DEFAULT_DETAIL_URL), 1);
    }

    /** 소프트 삭제된 이미지 */
    public static ProductGroupImage deletedThumbnailImage() {
        return ProductGroupImage.reconstitute(
                defaultThumbnailImageId(),
                ImageType.THUMBNAIL,
                ImageUrl.of(DEFAULT_THUMBNAIL_URL),
                0,
                CommonVoFixtures.yesterday());
    }

    // ===== ProductGroupImages Fixtures =====

    /** 기본 이미지 컬렉션 (썸네일 + 상세) */
    public static ProductGroupImages defaultImages() {
        return ProductGroupImages.of(List.of(newThumbnailImage(), newDetailImage()));
    }

    /** 썸네일만 있는 이미지 컬렉션 */
    public static ProductGroupImages thumbnailOnlyImages() {
        return ProductGroupImages.of(List.of(newThumbnailImage()));
    }

    /** 영속성에서 복원된 이미지 컬렉션 */
    public static ProductGroupImages persistedImages() {
        return ProductGroupImages.reconstitute(
                List.of(persistedThumbnailImage(), persistedDetailImage()));
    }

    /** 비어있는 이미지 컬렉션 (reconstitute 사용) */
    public static ProductGroupImages emptyImages() {
        return ProductGroupImages.reconstitute(List.of());
    }

    // ===== ProductGroupImageUpdateData Fixtures =====

    /** 기본 이미지 수정 데이터 */
    public static ProductGroupImageUpdateData defaultUpdateData() {
        return ProductGroupImageUpdateData.of(defaultImages(), CommonVoFixtures.now());
    }

    /** 이미지 컬렉션과 수정 시각으로 수정 데이터 생성 */
    public static ProductGroupImageUpdateData updateData(
            ProductGroupImages newImages, Instant updatedAt) {
        return ProductGroupImageUpdateData.of(newImages, updatedAt);
    }

    // ===== ProductGroupImageDiff Fixtures =====

    /** 변경 없는 Diff (빈 추가/삭제, 전체 유지) */
    public static ProductGroupImageDiff noChangeDiff() {
        List<ProductGroupImage> retained = List.of(persistedThumbnailImage());
        return ProductGroupImageDiff.of(List.of(), List.of(), retained, CommonVoFixtures.now());
    }

    /** 추가가 있는 Diff */
    public static ProductGroupImageDiff addedDiff() {
        List<ProductGroupImage> added = List.of(newDetailImage());
        List<ProductGroupImage> retained = List.of(persistedThumbnailImage());
        return ProductGroupImageDiff.of(added, List.of(), retained, CommonVoFixtures.now());
    }

    /** 삭제가 있는 Diff */
    public static ProductGroupImageDiff removedDiff() {
        List<ProductGroupImage> removed = List.of(persistedDetailImage());
        List<ProductGroupImage> retained = List.of(persistedThumbnailImage());
        return ProductGroupImageDiff.of(List.of(), removed, retained, CommonVoFixtures.now());
    }
}
