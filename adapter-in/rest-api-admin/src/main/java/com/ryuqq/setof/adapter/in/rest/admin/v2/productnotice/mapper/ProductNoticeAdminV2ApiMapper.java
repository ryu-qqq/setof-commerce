package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeV2ApiRequest;
import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNotice Admin V2 API Mapper
 *
 * <p>API Request DTO를 Application Command로 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductNoticeAdminV2ApiMapper {

    /**
     * 수정 요청을 Command로 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public UpdateProductNoticeCommand toUpdateCommand(UpdateProductNoticeV2ApiRequest request) {
        List<NoticeItemDto> items =
                request.items() != null
                        ? request.items().stream()
                                .map(
                                        i ->
                                                new NoticeItemDto(
                                                        i.fieldKey(),
                                                        i.fieldValue(),
                                                        i.displayOrder()))
                                .toList()
                        : List.of();

        return new UpdateProductNoticeCommand(request.productNoticeId(), items);
    }
}
