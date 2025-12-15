package com.ryuqq.setof.application.productimage.port.out.command;

import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;

/**
 * ProductImage Persistence Port (Command)
 *
 * <p>ProductImage Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductImagePersistencePort {

    /**
     * 상품이미지 저장
     *
     * @param productImage 저장할 ProductImage
     * @return 저장된 상품이미지 ID
     */
    Long save(ProductImage productImage);

    /**
     * 상품이미지 수정
     *
     * @param productImage 수정할 ProductImage
     */
    void update(ProductImage productImage);

    /**
     * 상품이미지 삭제
     *
     * @param productImageId 삭제할 상품이미지 ID
     */
    void delete(Long productImageId);

    /**
     * 상품그룹의 모든 이미지 삭제
     *
     * @param productGroupId 상품그룹 ID
     */
    void deleteByProductGroupId(Long productGroupId);
}
