package com.ryuqq.setof.application.productimage.port.in.command;

/**
 * 상품이미지 삭제 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteProductImageUseCase {

    /**
     * 상품이미지 삭제
     *
     * @param productImageId 상품이미지 ID
     */
    void delete(Long productImageId);

    /**
     * 상품그룹의 모든 이미지 삭제
     *
     * @param productGroupId 상품그룹 ID
     */
    void deleteByProductGroupId(Long productGroupId);
}
