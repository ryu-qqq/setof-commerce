package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command.UpdateProductImageV2ApiRequest;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import org.springframework.stereotype.Component;

/**
 * ProductImage Admin V2 API Mapper
 *
 * <p>API Request DTO를 Application Command로 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductImageAdminV2ApiMapper {

    /**
     * 수정 요청을 Command로 변환
     *
     * @param imageId 이미지 ID
     * @param request API 요청
     * @return Application Command
     */
    public UpdateProductImageCommand toUpdateCommand(
            Long imageId, UpdateProductImageV2ApiRequest request) {
        return new UpdateProductImageCommand(
                imageId, request.imageType(), request.cdnUrl(), request.displayOrder());
    }
}
