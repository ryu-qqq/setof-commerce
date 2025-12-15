package com.ryuqq.setof.application.productimage.service.command;

import com.ryuqq.setof.application.productimage.manager.command.ProductImageWriteManager;
import com.ryuqq.setof.application.productimage.port.in.command.DeleteProductImageUseCase;
import org.springframework.stereotype.Service;

/**
 * 상품이미지 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteProductImageService implements DeleteProductImageUseCase {

    private final ProductImageWriteManager writeManager;

    public DeleteProductImageService(ProductImageWriteManager writeManager) {
        this.writeManager = writeManager;
    }

    @Override
    public void delete(Long productImageId) {
        writeManager.delete(productImageId);
    }

    @Override
    public void deleteByProductGroupId(Long productGroupId) {
        writeManager.deleteByProductGroupId(productGroupId);
    }
}
