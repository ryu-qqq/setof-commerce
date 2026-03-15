package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.GnbDetailsV1ApiRequest;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * GnbCommandV1ApiMapper - v1 GNB Command API 변환 매퍼.
 *
 * <p>레거시 v1 요청 DTO(displayYn Y/N, LocalDateTime)를 Application Command(boolean, Instant)로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>displayYn "Y" → active true, "N" → active false
 *   <li>LocalDateTime (KST 기준) → Instant
 *   <li>gnbId → navigation menu ID
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class GnbCommandV1ApiMapper {

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
     * CreateGnbV1ApiRequest → RegisterNavigationMenuCommand 변환.
     *
     * <p>gnbId == null인 경우에만 호출합니다.
     *
     * @param request v1 GNB 등록 요청 DTO
     * @return Application 네비게이션 메뉴 등록 Command
     */
    public RegisterNavigationMenuCommand toRegisterCommand(CreateGnbV1ApiRequest request) {
        GnbDetailsV1ApiRequest d = request.gnbDetails();
        return new RegisterNavigationMenuCommand(
                d.title(),
                d.linkUrl(),
                d.displayOrder(),
                toInstant(d.displayPeriod().displayStartDate()),
                toInstant(d.displayPeriod().displayEndDate()),
                toActive(d.displayYn()));
    }

    /**
     * CreateGnbV1ApiRequest → UpdateNavigationMenuCommand 변환.
     *
     * <p>gnbId != null인 경우에만 호출합니다.
     *
     * @param request v1 GNB 수정 요청 DTO
     * @return Application 네비게이션 메뉴 수정 Command
     */
    public UpdateNavigationMenuCommand toUpdateCommand(CreateGnbV1ApiRequest request) {
        GnbDetailsV1ApiRequest d = request.gnbDetails();
        return new UpdateNavigationMenuCommand(
                request.gnbId(),
                d.title(),
                d.linkUrl(),
                d.displayOrder(),
                toInstant(d.displayPeriod().displayStartDate()),
                toInstant(d.displayPeriod().displayEndDate()),
                toActive(d.displayYn()));
    }

    /**
     * gnbId → RemoveNavigationMenuCommand 변환.
     *
     * @param gnbId 삭제 대상 GNB ID
     * @return Application 네비게이션 메뉴 삭제 Command
     */
    public RemoveNavigationMenuCommand toRemoveCommand(long gnbId) {
        return new RemoveNavigationMenuCommand(gnbId);
    }
}
