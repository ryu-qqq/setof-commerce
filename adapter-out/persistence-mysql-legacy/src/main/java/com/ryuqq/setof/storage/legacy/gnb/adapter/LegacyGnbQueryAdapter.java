package com.ryuqq.setof.storage.legacy.gnb.adapter;

import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.storage.legacy.gnb.entity.LegacyGnbEntity;
import com.ryuqq.setof.storage.legacy.gnb.mapper.LegacyGnbMapper;
import com.ryuqq.setof.storage.legacy.gnb.repository.LegacyGnbQueryDslRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyGnbQueryAdapter - 레거시 GNB 조회 Adapter.
 *
 * <p>레거시 DB를 조회하여 NavigationMenu 도메인 객체로 변환 후 반환한다. Application 레이어는 레거시 존재를 모른다.
 *
 * <p>활성화 조건: persistence.legacy.navigation.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.navigation.enabled", havingValue = "true")
public class LegacyGnbQueryAdapter implements NavigationMenuQueryPort {

    private final LegacyGnbQueryDslRepository repository;
    private final LegacyGnbMapper mapper;

    public LegacyGnbQueryAdapter(LegacyGnbQueryDslRepository repository, LegacyGnbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<NavigationMenu> fetchNavigationMenus() {
        List<LegacyGnbEntity> entities = repository.fetchGnbs();
        return mapper.toDomains(entities);
    }
}
