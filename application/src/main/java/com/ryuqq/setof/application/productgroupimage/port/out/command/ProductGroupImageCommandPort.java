package com.ryuqq.setof.application.productgroupimage.port.out.command;

import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;

/**
 * ProductGroupImageCommandPort - 상품그룹 이미지 Command Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupImageCommandPort {

    /**
     * 상품그룹 이미지를 저장합니다.
     *
     * @param image 저장할 상품그룹 이미지 도메인 객체
     * @param productGroupId 상품그룹 ID
     */
    void persist(ProductGroupImage image, Long productGroupId);

    /**
     * 상품그룹 이미지를 일괄 저장합니다.
     *
     * @param images 저장할 상품그룹 이미지 도메인 객체 목록
     * @param productGroupId 상품그룹 ID
     */
    void persistAll(List<ProductGroupImage> images, Long productGroupId);
}
