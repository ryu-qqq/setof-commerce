package com.ryuqq.setof.application.productimage.manager.command;

import com.ryuqq.setof.application.productimage.port.out.command.ProductImagePersistencePort;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import org.springframework.stereotype.Component;

/**
 * ProductImage Write Manager
 *
 * <p>ProductImage 쓰기 작업 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageWriteManager {

    private final ProductImagePersistencePort persistencePort;

    public ProductImageWriteManager(ProductImagePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    /**
     * 상품이미지 저장
     *
     * @param productImage ProductImage 도메인
     * @return 저장된 상품이미지 ID
     */
    public Long save(ProductImage productImage) {
        return persistencePort.save(productImage);
    }

    /**
     * 상품이미지 수정
     *
     * @param productImage ProductImage 도메인
     */
    public void update(ProductImage productImage) {
        persistencePort.update(productImage);
    }

    /**
     * 상품이미지 삭제
     *
     * @param productImageId 상품이미지 ID
     */
    public void delete(Long productImageId) {
        persistencePort.delete(productImageId);
    }

    /**
     * 상품그룹의 모든 이미지 삭제
     *
     * @param productGroupId 상품그룹 ID
     */
    public void deleteByProductGroupId(Long productGroupId) {
        persistencePort.deleteByProductGroupId(productGroupId);
    }
}
