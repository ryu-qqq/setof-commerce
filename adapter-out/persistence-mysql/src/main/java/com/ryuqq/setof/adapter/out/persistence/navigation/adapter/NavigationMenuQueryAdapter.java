package com.ryuqq.setof.adapter.out.persistence.navigation.adapter;

import com.ryuqq.setof.adapter.out.persistence.navigation.mapper.NavigationMenuJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuQueryDslRepository;
import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuQueryAdapter - 네비게이션 메뉴 Query 어댑터.
 *
 * <p>NavigationMenuQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.legacy.navigation.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.navigation.enabled", havingValue = "false")
public class NavigationMenuQueryAdapter implements NavigationMenuQueryPort {

    private final NavigationMenuQueryDslRepository queryDslRepository;
    private final NavigationMenuJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: Mapper + QueryDslRepository 의존.
     *
     * @param queryDslRepository QueryDSL 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public NavigationMenuQueryAdapter(
            NavigationMenuQueryDslRepository queryDslRepository,
            NavigationMenuJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 전시 중인 네비게이션 메뉴 목록 조회.
     *
     * @return NavigationMenu 목록
     */
    @Override
    public Optional<NavigationMenu> findById(long navigationMenuId) {
        return queryDslRepository.findById(navigationMenuId).map(mapper::toDomain);
    }

    @Override
    public List<NavigationMenu> fetchNavigationMenus() {
        return queryDslRepository.fetchDisplayMenus().stream().map(mapper::toDomain).toList();
    }
}
