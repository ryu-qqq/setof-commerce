package com.ryuqq.setof.adapter.out.persistence.content.adapter;

import com.ryuqq.setof.adapter.out.persistence.content.entity.ContentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.content.mapper.ContentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.content.repository.ContentJpaRepository;
import com.ryuqq.setof.application.content.port.out.command.ContentPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import org.springframework.stereotype.Component;

/**
 * ContentPersistenceAdapter - Content Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Content 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentPersistenceAdapter implements ContentPersistencePort {

    private final ContentJpaRepository jpaRepository;
    private final ContentJpaEntityMapper mapper;

    public ContentPersistenceAdapter(
            ContentJpaRepository jpaRepository, ContentJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /** Content 저장 (생성/수정) */
    @Override
    public ContentId persist(Content content) {
        ContentJpaEntity entity = mapper.toEntity(content);
        ContentJpaEntity savedEntity = jpaRepository.save(entity);
        return ContentId.of(savedEntity.getId());
    }
}
