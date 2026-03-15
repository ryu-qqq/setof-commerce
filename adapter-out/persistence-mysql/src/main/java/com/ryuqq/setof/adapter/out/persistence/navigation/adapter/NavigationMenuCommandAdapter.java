package com.ryuqq.setof.adapter.out.persistence.navigation.adapter;

import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.mapper.NavigationMenuJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.application.navigation.port.out.command.NavigationMenuCommandPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuCommandAdapter - 네비게이션 메뉴 Command 어댑터.
 *
 * <p>NavigationMenuCommandPort를 구현하여 New DB 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-005: Domain → Entity 변환은 Mapper 사용.
 *
 * <p>Command는 항상 New DB(persistence-mysql)에 저장하므로 ConditionalOnProperty 미적용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuCommandAdapter implements NavigationMenuCommandPort {

    private final NavigationMenuJpaRepository jpaRepository;
    private final NavigationMenuJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-001: JpaRepository + Mapper 의존.
     *
     * @param jpaRepository JPA 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public NavigationMenuCommandAdapter(
            NavigationMenuJpaRepository jpaRepository, NavigationMenuJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 네비게이션 메뉴를 저장합니다 (신규 등록 및 수정 공통).
     *
     * <p>PER-ADP-001: JpaRepository.save()만 사용.
     *
     * <p>PER-ADP-005: Domain → Entity 변환 후 저장.
     *
     * @param navigationMenu 저장할 NavigationMenu 도메인 객체
     * @return 저장된 네비게이션 메뉴 ID
     */
    @Override
    public Long persist(NavigationMenu navigationMenu) {
        NavigationMenuJpaEntity entity = mapper.toEntity(navigationMenu);
        return jpaRepository.save(entity).getId();
    }
}
