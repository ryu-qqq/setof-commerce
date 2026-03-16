package com.ryuqq.setof.adapter.out.persistence.displaycomponent.adapter;

import com.ryuqq.setof.adapter.out.persistence.displaycomponent.mapper.DisplayComponentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository.DisplayComponentJpaRepository;
import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentCommandPort;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * DisplayComponentCommandAdapter - 디스플레이 컴포넌트 저장 Adapter.
 *
 * <p>DisplayComponentCommandPort를 구현하여 컴포넌트를 영속합니다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class DisplayComponentCommandAdapter implements DisplayComponentCommandPort {

    private final DisplayComponentJpaRepository displayComponentJpaRepository;
    private final DisplayComponentJpaEntityMapper mapper;

    public DisplayComponentCommandAdapter(
            DisplayComponentJpaRepository displayComponentJpaRepository,
            DisplayComponentJpaEntityMapper mapper) {
        this.displayComponentJpaRepository = displayComponentJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void persistAll(List<DisplayComponent> components) {
        var entities = components.stream().map(mapper::toEntity).toList();
        displayComponentJpaRepository.saveAll(entities);
    }

    @Override
    public void updateAll(List<DisplayComponent> components) {
        var entities = components.stream().map(mapper::toEntity).toList();
        displayComponentJpaRepository.saveAll(entities);
    }
}
