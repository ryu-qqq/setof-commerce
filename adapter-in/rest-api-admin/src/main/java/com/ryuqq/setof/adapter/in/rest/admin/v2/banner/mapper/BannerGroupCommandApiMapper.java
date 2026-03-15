package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.ChangeBannerGroupStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlidesApiRequest;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import org.springframework.stereotype.Component;

/**
 * BannerGroupCommandApiMapper - 배너 그룹 Command API 변환 매퍼.
 *
 * <p>API Request와 Application Command 간 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>CQRS 분리: Command 전용 Mapper (QueryApiMapper와 분리).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupCommandApiMapper {

    /**
     * RegisterBannerGroupApiRequest -> RegisterBannerGroupCommand 변환.
     *
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterBannerGroupCommand toCommand(RegisterBannerGroupApiRequest request) {
        return new RegisterBannerGroupCommand(
                request.title(),
                request.bannerType(),
                request.displayStartAt(),
                request.displayEndAt(),
                request.active(),
                request.slides().stream()
                        .map(
                                s ->
                                        new RegisterBannerSlideCommand(
                                                s.title(),
                                                s.imageUrl(),
                                                s.linkUrl(),
                                                s.displayOrder(),
                                                s.displayStartAt(),
                                                s.displayEndAt(),
                                                s.active()))
                        .toList());
    }

    /**
     * UpdateBannerGroupApiRequest + PathVariable ID -> UpdateBannerGroupCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 -> PathVariable에서 전달.
     *
     * @param bannerGroupId 배너 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateBannerGroupCommand toCommand(
            long bannerGroupId, UpdateBannerGroupApiRequest request) {
        return new UpdateBannerGroupCommand(
                bannerGroupId,
                request.title(),
                request.bannerType(),
                request.displayStartAt(),
                request.displayEndAt(),
                request.active());
    }

    /**
     * UpdateBannerSlidesApiRequest + PathVariable ID -> UpdateBannerSlidesCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 -> PathVariable에서 전달.
     *
     * @param bannerGroupId 배너 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateBannerSlidesCommand toSlidesCommand(
            long bannerGroupId, UpdateBannerSlidesApiRequest request) {
        return new UpdateBannerSlidesCommand(
                bannerGroupId,
                request.slides().stream()
                        .map(
                                s ->
                                        new UpdateBannerSlideCommand(
                                                s.slideId(),
                                                s.title(),
                                                s.imageUrl(),
                                                s.linkUrl(),
                                                s.displayOrder(),
                                                s.displayStartAt(),
                                                s.displayEndAt(),
                                                s.active()))
                        .toList());
    }

    /**
     * PathVariable ID + ChangeBannerGroupStatusApiRequest -> ChangeBannerGroupStatusCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 -> PathVariable에서 전달.
     *
     * @param bannerGroupId 배너 그룹 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public ChangeBannerGroupStatusCommand toCommand(
            long bannerGroupId, ChangeBannerGroupStatusApiRequest request) {
        return new ChangeBannerGroupStatusCommand(bannerGroupId, request.active());
    }

    /**
     * PathVariable ID -> RemoveBannerGroupCommand 변환.
     *
     * @param bannerGroupId 삭제 대상 배너 그룹 ID (PathVariable)
     * @return Application Command DTO
     */
    public RemoveBannerGroupCommand toCommand(long bannerGroupId) {
        return new RemoveBannerGroupCommand(bannerGroupId);
    }
}
