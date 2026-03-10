package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.RegisterProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeApiRequest;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeCommandApiMapper - 상품 그룹 고시정보 Command API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeCommandApiMapper {

    /**
     * RegisterProductNoticeApiRequest -> RegisterProductNoticeCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterProductNoticeCommand toRegisterCommand(
            Long productGroupId, RegisterProductNoticeApiRequest request) {
        return new RegisterProductNoticeCommand(
                productGroupId,
                request.entries().stream()
                        .map(
                                entry ->
                                        new RegisterProductNoticeCommand.NoticeEntryCommand(
                                                entry.noticeFieldId(),
                                                entry.fieldName(),
                                                entry.fieldValue()))
                        .toList());
    }

    /**
     * UpdateProductNoticeApiRequest -> UpdateProductNoticeCommand 변환.
     *
     * @param productGroupId 상품 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateProductNoticeCommand toUpdateCommand(
            Long productGroupId, UpdateProductNoticeApiRequest request) {
        return new UpdateProductNoticeCommand(
                productGroupId,
                request.entries().stream()
                        .map(
                                entry ->
                                        new UpdateProductNoticeCommand.NoticeEntryCommand(
                                                entry.noticeFieldId(),
                                                entry.fieldName(),
                                                entry.fieldValue()))
                        .toList());
    }
}
