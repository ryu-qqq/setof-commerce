package com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.dto.request.RecordOrderEventV2ApiRequest;
import com.ryuqq.setof.application.orderevent.dto.command.RecordOrderEventCommand;
import org.springframework.stereotype.Component;

/**
 * OrderEventAdminV2ApiMapper - OrderEvent Admin API DTO 변환 Mapper
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class OrderEventAdminV2ApiMapper {

    /**
     * RecordOrderEventV2ApiRequest → RecordOrderEventCommand 변환
     *
     * <p>Domain VO 의존성 제거를 위해 String 기반 팩토리 메서드 사용
     *
     * @param request API 요청 DTO
     * @return Application Command
     */
    public RecordOrderEventCommand toCommand(RecordOrderEventV2ApiRequest request) {
        return RecordOrderEventCommand.ofStrings(
                request.orderId(),
                request.eventType(),
                request.source(),
                request.referenceId(),
                request.actorType(),
                request.actorId(),
                buildDescription(request.title(), request.description()));
    }

    private String buildDescription(String title, String description) {
        if (title == null && description == null) {
            return null;
        }
        if (title == null) {
            return description;
        }
        if (description == null) {
            return title;
        }
        return title + " - " + description;
    }
}
