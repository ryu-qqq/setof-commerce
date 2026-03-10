package com.ryuqq.setof.application.productgroupimage.manager;

import com.ryuqq.setof.application.productgroupimage.port.out.query.ProductGroupImageQueryPort;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupImageReadManager - 상품그룹 이미지 조회 Manager.
 *
 * <p>QueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ProductGroupImageReadManager {

    private final ProductGroupImageQueryPort queryPort;

    public ProductGroupImageReadManager(ProductGroupImageQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 상품그룹 ID로 이미지 목록을 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 이미지 목록
     */
    public List<ProductGroupImage> getByProductGroupId(ProductGroupId productGroupId) {
        return queryPort.findByProductGroupId(productGroupId);
    }

    /**
     * 복수 상품그룹 ID로 이미지 목록을 조회합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 상품그룹 이미지 목록
     */
    public List<ProductGroupImage> getByProductGroupIds(List<ProductGroupId> productGroupIds) {
        return queryPort.findByProductGroupIds(productGroupIds);
    }
}
