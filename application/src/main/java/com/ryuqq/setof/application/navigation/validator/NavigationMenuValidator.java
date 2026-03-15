package com.ryuqq.setof.application.navigation.validator;

import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.exception.NavigationMenuNotFoundException;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuValidator - 네비게이션 메뉴 검증기.
 *
 * <p>APP-VAL-001: 검증 성공 시 Domain 객체를 반환합니다.
 *
 * <p>APP-VAL-002: 도메인 전용 예외를 발생시킵니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuValidator {

    private final NavigationMenuQueryPort queryPort;

    public NavigationMenuValidator(NavigationMenuQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 네비게이션 메뉴 존재 여부를 검증하고 Domain 객체를 반환합니다.
     *
     * <p>APP-VAL-001: 검증 성공 시 조회한 Domain 객체를 반환합니다.
     *
     * <p>APP-VAL-002: 존재하지 않는 경우 도메인 전용 예외를 발생시킵니다.
     *
     * @param id 네비게이션 메뉴 ID
     * @return NavigationMenu 도메인 객체
     * @throws NavigationMenuNotFoundException 존재하지 않는 경우
     */
    public NavigationMenu findExistingOrThrow(NavigationMenuId id) {
        return queryPort.findById(id.value()).orElseThrow(NavigationMenuNotFoundException::new);
    }
}
