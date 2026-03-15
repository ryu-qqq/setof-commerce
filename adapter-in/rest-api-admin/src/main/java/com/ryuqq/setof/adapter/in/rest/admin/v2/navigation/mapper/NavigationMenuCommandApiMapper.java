package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.RegisterNavigationMenuApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.UpdateNavigationMenuApiRequest;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuCommandApiMapper - 네비게이션 메뉴 Command API 변환 매퍼.
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
public class NavigationMenuCommandApiMapper {

    /**
     * RegisterNavigationMenuApiRequest -> RegisterNavigationMenuCommand 변환.
     *
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterNavigationMenuCommand toCommand(RegisterNavigationMenuApiRequest request) {
        return new RegisterNavigationMenuCommand(
                request.title(),
                request.linkUrl(),
                request.displayOrder(),
                request.displayStartAt(),
                request.displayEndAt(),
                request.active());
    }

    /**
     * UpdateNavigationMenuApiRequest + PathVariable ID -> UpdateNavigationMenuCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 -> PathVariable에서 전달.
     *
     * @param navigationMenuId 네비게이션 메뉴 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateNavigationMenuCommand toCommand(
            long navigationMenuId, UpdateNavigationMenuApiRequest request) {
        return new UpdateNavigationMenuCommand(
                navigationMenuId,
                request.title(),
                request.linkUrl(),
                request.displayOrder(),
                request.displayStartAt(),
                request.displayEndAt(),
                request.active());
    }

    /**
     * PathVariable ID -> RemoveNavigationMenuCommand 변환.
     *
     * @param navigationMenuId 삭제 대상 네비게이션 메뉴 ID (PathVariable)
     * @return Application Command DTO
     */
    public RemoveNavigationMenuCommand toCommand(long navigationMenuId) {
        return new RemoveNavigationMenuCommand(navigationMenuId);
    }
}
