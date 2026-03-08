package com.ryuqq.setof.adapter.in.rest.v1.navigation.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response.NavigationMenuV1ApiResponse;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * NavigationV1ApiMapper - 네비게이션 V1 API Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Domain → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationV1ApiMapper {

    /**
     * NavigationMenu 목록 → NavigationMenuV1ApiResponse 목록 변환.
     *
     * @param menus NavigationMenu 목록
     * @return NavigationMenuV1ApiResponse 목록
     */
    public List<NavigationMenuV1ApiResponse> toListResponse(List<NavigationMenu> menus) {
        return menus.stream().map(this::toResponse).toList();
    }

    /**
     * NavigationMenu → NavigationMenuV1ApiResponse 변환.
     *
     * @param menu NavigationMenu
     * @return NavigationMenuV1ApiResponse
     */
    public NavigationMenuV1ApiResponse toResponse(NavigationMenu menu) {
        return new NavigationMenuV1ApiResponse(menu.idValue(), menu.title(), menu.linkUrl());
    }
}
