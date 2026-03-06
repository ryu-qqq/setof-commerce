package com.ryuqq.setof.domain.productgroupimage.exception;

import java.util.Map;

/** THUMBNAIL 이미지가 없거나 중복될 때 발생하는 예외. */
public class ProductGroupImageNoThumbnailException extends ProductGroupImageException {

    public ProductGroupImageNoThumbnailException(Long productGroupId) {
        super(
                ProductGroupImageErrorCode.NO_THUMBNAIL,
                String.format("상품 그룹(%d)에 대표 이미지(THUMBNAIL)가 없습니다", productGroupId),
                Map.of("productGroupId", productGroupId));
    }

    public ProductGroupImageNoThumbnailException(long thumbnailCount) {
        super(
                ProductGroupImageErrorCode.NO_THUMBNAIL,
                String.format("THUMBNAIL 이미지가 정확히 1개 필요합니다 (현재 %d개)", thumbnailCount),
                Map.of("thumbnailCount", thumbnailCount));
    }
}
