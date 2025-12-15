package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command.UpdateProductDescriptionV2ApiRequest;
import com.ryuqq.setof.application.productdescription.dto.command.DescriptionImageDto;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductDescription Admin V2 API Mapper
 *
 * <p>API Request DTO를 Application Command로 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductDescriptionAdminV2ApiMapper {

    /**
     * 수정 요청을 Command로 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public UpdateProductDescriptionCommand toUpdateCommand(
            UpdateProductDescriptionV2ApiRequest request) {
        List<DescriptionImageDto> images =
                request.images() != null
                        ? request.images().stream()
                                .map(
                                        i ->
                                                new DescriptionImageDto(
                                                        i.displayOrder(),
                                                        i.originUrl(),
                                                        i.cdnUrl()))
                                .toList()
                        : List.of();

        return new UpdateProductDescriptionCommand(
                request.productDescriptionId(), request.htmlContent(), images);
    }
}
