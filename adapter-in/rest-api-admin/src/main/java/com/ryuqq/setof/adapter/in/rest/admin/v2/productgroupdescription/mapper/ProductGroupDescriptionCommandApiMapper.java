package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.RegisterProductGroupDescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.UpdateProductGroupDescriptionApiRequest;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupDescriptionCommandApiMapper - 상품 그룹 상세 설명 Command API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupDescriptionCommandApiMapper {

    /**
     * RegisterProductGroupDescriptionApiRequest -> RegisterProductGroupDescriptionCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterProductGroupDescriptionCommand toRegisterCommand(
            Long productGroupId, RegisterProductGroupDescriptionApiRequest request) {
        return new RegisterProductGroupDescriptionCommand(
                productGroupId,
                request.content(),
                toDescriptionImageCommands(request.descriptionImages()));
    }

    /**
     * UpdateProductGroupDescriptionApiRequest -> UpdateProductGroupDescriptionCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductGroupDescriptionCommand toUpdateCommand(
            Long productGroupId, UpdateProductGroupDescriptionApiRequest request) {
        return new UpdateProductGroupDescriptionCommand(
                productGroupId,
                request.content(),
                toUpdateDescriptionImageCommands(request.descriptionImages()));
    }

    private List<RegisterProductGroupDescriptionCommand.DescriptionImageCommand>
            toDescriptionImageCommands(
                    List<RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest>
                            images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(
                        img ->
                                new RegisterProductGroupDescriptionCommand.DescriptionImageCommand(
                                        img.imageUrl(), img.sortOrder()))
                .toList();
    }

    private List<UpdateProductGroupDescriptionCommand.DescriptionImageCommand>
            toUpdateDescriptionImageCommands(
                    List<UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest>
                            images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(
                        img ->
                                new UpdateProductGroupDescriptionCommand.DescriptionImageCommand(
                                        img.imageUrl(), img.sortOrder()))
                .toList();
    }
}
