package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;

/**
 * ComponentAutoProductQueryPort - AUTO 상품 조회 출력 포트.
 *
 * <p>category/brand 기반으로 동적 상품을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ComponentAutoProductQueryPort {

    /**
     * AUTO 상품 조회.
     *
     * <p>categoryId, brandIds 조건에 따라 product_group 테이블에서 직접 조회한다.
     *
     * @param criteria AUTO 상품 조회 조건
     * @return 상품 목록
     */
    List<ProductThumbnailSnapshot> fetchAutoProducts(AutoProductCriteria criteria);
}
