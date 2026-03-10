package com.ryuqq.setof.application.productgroupimage.factory;

import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand.ImageCommand;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupImageFactory - 상품그룹 이미지 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupImageFactory {

    /**
     * 이미지 커맨드 목록으로부터 ProductGroupImage 도메인 객체 목록을 생성합니다.
     *
     * @param imageCommands 이미지 커맨드 목록
     * @return ProductGroupImage 도메인 객체 목록
     */
    public List<ProductGroupImage> createNewImages(List<ImageCommand> imageCommands) {
        if (imageCommands == null || imageCommands.isEmpty()) {
            return List.of();
        }
        return imageCommands.stream().map(this::createNewImage).toList();
    }

    /**
     * 수정용 이미지 커맨드 목록으로부터 ProductGroupImage 도메인 객체 목록을 생성합니다.
     *
     * @param imageCommands 이미지 커맨드 목록
     * @return ProductGroupImage 도메인 객체 목록
     */
    public List<ProductGroupImage> createNewImagesFromUpdate(
            List<
                            com.ryuqq.setof.application.productgroupimage.dto.command
                                    .UpdateProductGroupImagesCommand.ImageCommand>
                    imageCommands) {
        if (imageCommands == null || imageCommands.isEmpty()) {
            return List.of();
        }
        return imageCommands.stream()
                .map(
                        cmd ->
                                ProductGroupImage.forNew(
                                        ImageType.valueOf(cmd.imageType()),
                                        ImageUrl.of(cmd.imageUrl()),
                                        cmd.sortOrder()))
                .toList();
    }

    private ProductGroupImage createNewImage(ImageCommand command) {
        return ProductGroupImage.forNew(
                ImageType.valueOf(command.imageType()),
                ImageUrl.of(command.imageUrl()),
                command.sortOrder());
    }
}
