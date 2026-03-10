package com.ryuqq.setof.application.productgroupimage.dto.command;

import java.util.List;

/**
 * 상품그룹 이미지 수정 커맨드 (전체 교체).
 *
 * @param productGroupId 상품그룹 ID
 * @param images 이미지 목록
 */
public record UpdateProductGroupImagesCommand(long productGroupId, List<ImageCommand> images) {

    /**
     * 이미지 커맨드.
     *
     * @param imageType 이미지 유형 (THUMBNAIL, DETAIL)
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    public record ImageCommand(String imageType, String imageUrl, int sortOrder) {}
}
