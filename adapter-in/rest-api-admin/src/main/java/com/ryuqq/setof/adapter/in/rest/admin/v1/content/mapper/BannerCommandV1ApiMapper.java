package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateBannerItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateBannerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateBannerDisplayYnV1ApiRequest;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerCommandV1ApiMapper - v1 배너 Command API 변환 매퍼.
 *
 * <p>레거시 v1 요청 DTO(displayYn Y/N, LocalDateTime)를 Application Command(boolean, Instant)로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>displayYn "Y" → active true, "N" → active false
 *   <li>LocalDateTime (KST 기준) → Instant
 *   <li>bannerItemId → slideId
 *   <li>bannerId (CreateBannerItemV1ApiRequest 안의) → bannerGroupId
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerCommandV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * displayYn("Y"/"N") → boolean active 변환.
     *
     * @param displayYn 레거시 전시 여부 문자열
     * @return true이면 전시, false이면 비전시
     */
    private boolean toActive(String displayYn) {
        return "Y".equalsIgnoreCase(displayYn);
    }

    /**
     * LocalDateTime (KST 기준) → Instant 변환.
     *
     * @param ldt KST 기준 LocalDateTime
     * @return UTC Instant
     */
    private Instant toInstant(LocalDateTime ldt) {
        return ldt.atZone(KST).toInstant();
    }

    /**
     * CreateBannerV1ApiRequest → RegisterBannerGroupCommand 변환.
     *
     * <p>슬라이드 없이 그룹만 등록합니다. (슬라이드는 별도 items API로 처리)
     *
     * @param request v1 배너 그룹 등록 요청 DTO
     * @return Application 배너 그룹 등록 Command
     */
    public RegisterBannerGroupCommand toRegisterCommand(CreateBannerV1ApiRequest request) {
        return new RegisterBannerGroupCommand(
                request.title(),
                request.bannerType(),
                toInstant(request.displayPeriod().displayStartDate()),
                toInstant(request.displayPeriod().displayEndDate()),
                toActive(request.displayYn()),
                List.of());
    }

    /**
     * bannerId + CreateBannerV1ApiRequest → UpdateBannerGroupCommand 변환.
     *
     * <p>슬라이드 수정은 포함하지 않습니다. (UpdateBannerSlidesUseCase로 별도 처리)
     *
     * @param bannerId PathVariable 배너 그룹 ID
     * @param request v1 배너 그룹 수정 요청 DTO
     * @return Application 배너 그룹 수정 Command
     */
    public UpdateBannerGroupCommand toUpdateCommand(
            long bannerId, CreateBannerV1ApiRequest request) {
        return new UpdateBannerGroupCommand(
                bannerId,
                request.title(),
                request.bannerType(),
                toInstant(request.displayPeriod().displayStartDate()),
                toInstant(request.displayPeriod().displayEndDate()),
                toActive(request.displayYn()));
    }

    /**
     * List&lt;CreateBannerItemV1ApiRequest&gt; → UpdateBannerSlidesCommand 변환.
     *
     * <p>레거시 규칙: 첫 번째 항목의 bannerId를 배너 그룹 ID로 사용합니다.
     *
     * @param requests v1 배너 슬라이드 일괄 요청 DTO 목록
     * @return Application 배너 슬라이드 일괄 수정 Command
     */
    public UpdateBannerSlidesCommand toUpdateSlidesCommand(
            List<CreateBannerItemV1ApiRequest> requests) {
        long bannerGroupId = requests.get(0).bannerId();
        List<UpdateBannerSlideCommand> slides =
                requests.stream()
                        .map(
                                item ->
                                        new UpdateBannerSlideCommand(
                                                item.bannerItemId(),
                                                item.title(),
                                                item.imageUrl(),
                                                item.linkUrl(),
                                                item.displayOrder(),
                                                toInstant(item.displayPeriod().displayStartDate()),
                                                toInstant(item.displayPeriod().displayEndDate()),
                                                toActive(item.displayYn())))
                        .toList();
        return new UpdateBannerSlidesCommand(bannerGroupId, slides);
    }

    /**
     * bannerId + UpdateBannerDisplayYnV1ApiRequest → ChangeBannerGroupStatusCommand 변환.
     *
     * @param bannerId PathVariable 배너 그룹 ID
     * @param request v1 노출 상태 변경 요청 DTO
     * @return Application 배너 그룹 노출 상태 변경 Command
     */
    public ChangeBannerGroupStatusCommand toChangeStatusCommand(
            long bannerId, UpdateBannerDisplayYnV1ApiRequest request) {
        return new ChangeBannerGroupStatusCommand(bannerId, toActive(request.displayYn()));
    }
}
