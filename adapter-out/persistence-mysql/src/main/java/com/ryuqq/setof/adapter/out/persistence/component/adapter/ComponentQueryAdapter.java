package com.ryuqq.setof.adapter.out.persistence.component.adapter;

import com.ryuqq.setof.adapter.out.persistence.component.mapper.ComponentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.component.repository.ComponentQueryDslRepository;
import com.ryuqq.setof.application.component.port.out.query.ComponentQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * ComponentQueryAdapter - Component Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Component 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ComponentQueryAdapter implements ComponentQueryPort {

    private final ComponentQueryDslRepository queryDslRepository;
    private final ComponentJpaEntityMapper mapper;

    public ComponentQueryAdapter(
            ComponentQueryDslRepository queryDslRepository, ComponentJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /** ID로 컴포넌트 조회 */
    @Override
    public Optional<Component> findById(ComponentId componentId) {
        return queryDslRepository.findById(componentId.value()).map(mapper::toDomain);
    }

    /** Content ID로 컴포넌트 목록 조회 */
    @Override
    public List<Component> findByContentId(ContentId contentId) {
        return queryDslRepository.findByContentId(contentId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 컴포넌트 존재 여부 확인 */
    @Override
    public boolean existsById(ComponentId componentId) {
        return queryDslRepository.existsById(componentId.value());
    }
}
