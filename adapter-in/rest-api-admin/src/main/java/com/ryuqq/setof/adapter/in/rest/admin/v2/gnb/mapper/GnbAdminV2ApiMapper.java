package com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.CreateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.UpdateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.response.GnbV2ApiResponse;
import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * GNB Admin API Mapper
 *
 * <p>REST API DTO ↔ Application Command/Response 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class GnbAdminV2ApiMapper {

    /**
     * 생성 요청 → Command 변환
     *
     * @param request 생성 요청
     * @return CreateGnbCommand
     */
    public CreateGnbCommand toCreateCommand(CreateGnbV2ApiRequest request) {
        return new CreateGnbCommand(
                request.title(),
                request.linkUrl(),
                request.displayOrder(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * 수정 요청 → Command 변환
     *
     * @param gnbId GNB ID
     * @param request 수정 요청
     * @return UpdateGnbCommand
     */
    public UpdateGnbCommand toUpdateCommand(Long gnbId, UpdateGnbV2ApiRequest request) {
        return new UpdateGnbCommand(
                GnbId.of(gnbId),
                request.title(),
                request.linkUrl(),
                request.displayOrder(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * Response → API Response 변환
     *
     * @param response Application Response
     * @return GnbV2ApiResponse
     */
    public GnbV2ApiResponse toApiResponse(GnbResponse response) {
        return new GnbV2ApiResponse(
                response.gnbId(),
                response.title(),
                response.linkUrl(),
                response.displayOrder(),
                response.status(),
                response.displayStartDate(),
                response.displayEndDate(),
                response.createdAt(),
                response.updatedAt());
    }

    /**
     * Response 목록 → API Response 목록 변환
     *
     * @param responses Application Response 목록
     * @return GnbV2ApiResponse 목록
     */
    public List<GnbV2ApiResponse> toApiResponses(List<GnbResponse> responses) {
        return responses.stream().map(this::toApiResponse).toList();
    }
}
