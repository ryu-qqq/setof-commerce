package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.RegisterProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.UpdateProductGroupImagesApiRequest;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import org.springframework.stereotype.Component;

/**
 * ProductGroupImageCommandApiMapper - 상품 그룹 이미지 Command API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupImageCommandApiMapper {

    /**
     * RegisterProductGroupImagesApiRequest -> RegisterProductGroupImagesCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterProductGroupImagesCommand toRegisterCommand(
            Long productGroupId, RegisterProductGroupImagesApiRequest request) {
        return new RegisterProductGroupImagesCommand(
                productGroupId,
                request.images().stream()
                        .map(
                                img ->
                                        new RegisterProductGroupImagesCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList());
    }

    /**
     * UpdateProductGroupImagesApiRequest -> UpdateProductGroupImagesCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductGroupImagesCommand toUpdateCommand(
            Long productGroupId, UpdateProductGroupImagesApiRequest request) {
        return new UpdateProductGroupImagesCommand(
                productGroupId,
                request.images().stream()
                        .map(
                                img ->
                                        new UpdateProductGroupImagesCommand.ImageCommand(
                                                img.imageType(), img.imageUrl(), img.sortOrder()))
                        .toList());
    }
}
