package com.ryuqq.setof.application.productgroupimage.port.out.query;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import java.util.Map;

/**
 * ProductGroupImageQueryPort - 상품그룹 이미지 Query Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupImageQueryPort {

    /**
     * 단일 상품그룹 ID로 이미지 목록을 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 활성 상품그룹 이미지 목록
     */
    List<ProductGroupImage> findByProductGroupId(ProductGroupId productGroupId);

    /**
     * 복수 상품그룹 ID로 이미지 목록을 조회합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 활성 상품그룹 이미지 목록
     */
    List<ProductGroupImage> findByProductGroupIds(List<ProductGroupId> productGroupIds);

    /**
     * 복수 상품그룹 ID로 대표(THUMBNAIL) 이미지 ID를 조회합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return productGroupId → thumbnailImageId 맵
     */
    Map<Long, Long> findThumbnailImageIdsByProductGroupIds(List<ProductGroupId> productGroupIds);
}
