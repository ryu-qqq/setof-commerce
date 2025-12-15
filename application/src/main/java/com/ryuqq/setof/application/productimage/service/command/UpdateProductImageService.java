package com.ryuqq.setof.application.productimage.service.command;

import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.manager.command.ProductImageWriteManager;
import com.ryuqq.setof.application.productimage.manager.query.ProductImageReadManager;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import org.springframework.stereotype.Service;

/**
 * 상품이미지 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductImageService implements UpdateProductImageUseCase {

    private final ProductImageReadManager readManager;
    private final ProductImageWriteManager writeManager;

    public UpdateProductImageService(
            ProductImageReadManager readManager, ProductImageWriteManager writeManager) {
        this.readManager = readManager;
        this.writeManager = writeManager;
    }

    @Override
    public void update(UpdateProductImageCommand command) {
        ProductImage existingImage = readManager.findById(command.id());

        ProductImage updatedImage =
                existingImage
                        .changeImageType(ImageType.valueOf(command.imageType()))
                        .updateCdnUrl(ImageUrl.of(command.cdnUrl()))
                        .changeDisplayOrder(command.displayOrder());

        writeManager.update(updatedImage);
    }
}
