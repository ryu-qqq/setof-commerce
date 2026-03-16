package com.ryuqq.setof.adapter.out.persistence.contentpage.adapter;

import com.ryuqq.setof.adapter.out.persistence.contentpage.mapper.ContentPageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageJpaRepository;
import com.ryuqq.setof.application.contentpage.port.out.ContentPageCommandPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ContentPageCommandAdapter - 콘텐츠 페이지 저장 Adapter.
 *
 * <p>ContentPageCommandPort를 구현하여 콘텐츠 페이지를 영속합니다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class ContentPageCommandAdapter implements ContentPageCommandPort {

    private final ContentPageJpaRepository contentPageJpaRepository;
    private final ContentPageJpaEntityMapper mapper;

    public ContentPageCommandAdapter(
            ContentPageJpaRepository contentPageJpaRepository, ContentPageJpaEntityMapper mapper) {
        this.contentPageJpaRepository = contentPageJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(ContentPage contentPage) {
        return contentPageJpaRepository.save(mapper.toEntity(contentPage)).getId();
    }
}
