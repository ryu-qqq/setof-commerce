package com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command.CreateProductImageV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command.UpdateProductDescriptionV1ApiRequest;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Product Image Mapper
 *
 * <p>V1 Legacy API Request를 Application Command로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageAdminV1ApiMapper {

    /**
     * V1 상세 설명 수정 요청을 Application Command로 변환
     *
     * @param productDescriptionId 상품설명 ID
     * @param request V1 상세 설명 수정 요청
     * @return UpdateProductDescriptionCommand
     */
    public UpdateProductDescriptionCommand toUpdateDescriptionCommand(
            Long productDescriptionId, UpdateProductDescriptionV1ApiRequest request) {

        return new UpdateProductDescriptionCommand(
                productDescriptionId, request.detailDescription(), Collections.emptyList());
    }

    /**
     * V1 이미지 생성 요청 목록을 Application Command 목록으로 변환
     *
     * @param existingImageIds 기존 이미지 ID 목록
     * @param requests V1 이미지 생성 요청 목록
     * @return UpdateProductImageCommand 목록
     */
    public List<UpdateProductImageCommand> toUpdateImageCommands(
            List<Long> existingImageIds, List<CreateProductImageV1ApiRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }

        int minSize = Math.min(existingImageIds.size(), requests.size());
        return java.util.stream.IntStream.range(0, minSize)
                .mapToObj(
                        i -> toUpdateImageCommand(existingImageIds.get(i), requests.get(i), i + 1))
                .toList();
    }

    private UpdateProductImageCommand toUpdateImageCommand(
            Long imageId, CreateProductImageV1ApiRequest request, int displayOrder) {

        return new UpdateProductImageCommand(
                imageId, request.productImageType(), request.imageUrl(), displayOrder);
    }
}
