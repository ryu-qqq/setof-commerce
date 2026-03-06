package com.ryuqq.setof.application.productgroupimage;

import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand.ImageCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import java.util.List;

/**
 * ProductGroupImage Application Command 테스트 Fixtures.
 *
 * <p>ProductGroupImage 관련 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupImageCommandFixtures {

    private ProductGroupImageCommandFixtures() {}

    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_THUMBNAIL_URL = "https://example.com/thumbnail.png";
    public static final String DEFAULT_DETAIL_URL = "https://example.com/detail.png";
    public static final String UPDATED_THUMBNAIL_URL = "https://example.com/new-thumbnail.png";
    public static final String UPDATED_DETAIL_URL = "https://example.com/new-detail.png";

    // ===== RegisterProductGroupImagesCommand Fixtures =====

    /** 기본 이미지 등록 커맨드 (썸네일 + 상세) */
    public static RegisterProductGroupImagesCommand registerCommand() {
        return new RegisterProductGroupImagesCommand(
                DEFAULT_PRODUCT_GROUP_ID, List.of(thumbnailImageCommand(), detailImageCommand()));
    }

    /** 상품그룹 ID를 지정한 이미지 등록 커맨드 */
    public static RegisterProductGroupImagesCommand registerCommand(long productGroupId) {
        return new RegisterProductGroupImagesCommand(
                productGroupId, List.of(thumbnailImageCommand(), detailImageCommand()));
    }

    /** 썸네일만 있는 등록 커맨드 */
    public static RegisterProductGroupImagesCommand registerThumbnailOnlyCommand() {
        return new RegisterProductGroupImagesCommand(
                DEFAULT_PRODUCT_GROUP_ID, List.of(thumbnailImageCommand()));
    }

    /** 여러 상세 이미지를 포함한 등록 커맨드 */
    public static RegisterProductGroupImagesCommand registerCommandWithMultipleDetails() {
        return new RegisterProductGroupImagesCommand(
                DEFAULT_PRODUCT_GROUP_ID,
                List.of(
                        thumbnailImageCommand(),
                        detailImageCommand(),
                        detailImageCommand("https://example.com/detail2.png", 2)));
    }

    // ===== RegisterProductGroupImagesCommand.ImageCommand Fixtures =====

    /** 기본 썸네일 이미지 커맨드 */
    public static ImageCommand thumbnailImageCommand() {
        return new ImageCommand("THUMBNAIL", DEFAULT_THUMBNAIL_URL, 0);
    }

    /** 기본 상세 이미지 커맨드 */
    public static ImageCommand detailImageCommand() {
        return new ImageCommand("DETAIL", DEFAULT_DETAIL_URL, 1);
    }

    /** URL과 sortOrder를 지정한 상세 이미지 커맨드 */
    public static ImageCommand detailImageCommand(String imageUrl, int sortOrder) {
        return new ImageCommand("DETAIL", imageUrl, sortOrder);
    }

    // ===== UpdateProductGroupImagesCommand Fixtures =====

    /** 기본 이미지 수정 커맨드 (썸네일 + 상세) */
    public static UpdateProductGroupImagesCommand updateCommand() {
        return new UpdateProductGroupImagesCommand(
                DEFAULT_PRODUCT_GROUP_ID,
                List.of(updatedThumbnailImageCommand(), updatedDetailImageCommand()));
    }

    /** 상품그룹 ID를 지정한 이미지 수정 커맨드 */
    public static UpdateProductGroupImagesCommand updateCommand(long productGroupId) {
        return new UpdateProductGroupImagesCommand(
                productGroupId,
                List.of(updatedThumbnailImageCommand(), updatedDetailImageCommand()));
    }

    /** 썸네일만 있는 수정 커맨드 */
    public static UpdateProductGroupImagesCommand updateThumbnailOnlyCommand() {
        return new UpdateProductGroupImagesCommand(
                DEFAULT_PRODUCT_GROUP_ID, List.of(updatedThumbnailImageCommand()));
    }

    // ===== UpdateProductGroupImagesCommand.ImageCommand Fixtures =====

    /** 수정된 썸네일 이미지 커맨드 */
    public static UpdateProductGroupImagesCommand.ImageCommand updatedThumbnailImageCommand() {
        return new UpdateProductGroupImagesCommand.ImageCommand(
                "THUMBNAIL", UPDATED_THUMBNAIL_URL, 0);
    }

    /** 수정된 상세 이미지 커맨드 */
    public static UpdateProductGroupImagesCommand.ImageCommand updatedDetailImageCommand() {
        return new UpdateProductGroupImagesCommand.ImageCommand("DETAIL", UPDATED_DETAIL_URL, 1);
    }
}
