package com.ryuqq.setof.adapter.in.rest.v2.orderevent.dto.response;

import com.ryuqq.setof.application.orderevent.dto.response.OrderTimelineResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * OrderTimelineV2ApiResponse - 주문 타임라인 API 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "주문 타임라인 응답")
public record OrderTimelineV2ApiResponse(
        @Schema(description = "주문 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                String orderId,
        @Schema(description = "타임라인 이벤트 목록 (시간순 정렬)") List<TimelineEventResponse> events) {

    /** 개별 타임라인 이벤트 */
    @Schema(description = "타임라인 이벤트")
    public record TimelineEventResponse(
            @Schema(description = "이벤트 유형", example = "ORDER_CREATED") String eventType,
            @Schema(description = "이벤트 제목", example = "주문 생성됨") String title,
            @Schema(description = "이벤트 상세 내용", example = "주문이 성공적으로 생성되었습니다.") String description,
            @Schema(description = "이벤트 소스", example = "ORDER") String source,
            @Schema(description = "관련 ID (클레임 ID 등)", example = "CLM-20241215-000001")
                    String referenceId,
            @Schema(description = "행위자 유형", example = "CUSTOMER") String actorType,
            @Schema(description = "행위자 ID", example = "12345") String actorId,
            @Schema(description = "이벤트 발생 시각") Instant occurredAt) {}

    /** Application Response → API Response 변환 */
    public static OrderTimelineV2ApiResponse from(OrderTimelineResponse response) {
        List<TimelineEventResponse> events =
                response.events().stream()
                        .map(
                                e ->
                                        new TimelineEventResponse(
                                                e.eventType(),
                                                e.eventType(),
                                                e.description(),
                                                e.eventSource(),
                                                e.sourceId(),
                                                e.actorType(),
                                                e.actorId(),
                                                e.createdAt()))
                        .toList();

        return new OrderTimelineV2ApiResponse(response.orderId(), events);
    }
}
