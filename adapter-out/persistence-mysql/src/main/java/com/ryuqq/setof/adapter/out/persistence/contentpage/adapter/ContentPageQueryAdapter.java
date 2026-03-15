package com.ryuqq.setof.adapter.out.persistence.contentpage.adapter;

import com.ryuqq.setof.adapter.out.persistence.contentpage.mapper.ContentPageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageQueryDslRepository;
import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class ContentPageQueryAdapter implements ContentPageQueryPort {

    private final ContentPageQueryDslRepository queryDslRepository;
    private final ContentPageJpaEntityMapper mapper;

    public ContentPageQueryAdapter(
            ContentPageQueryDslRepository queryDslRepository, ContentPageJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Set<Long> fetchOnDisplayContentPageIds() {
        return new HashSet<>(queryDslRepository.fetchOnDisplayContentPageIds());
    }

    @Override
    public Optional<ContentPage> fetchContentPageMeta(long contentPageId) {
        return queryDslRepository.fetchById(contentPageId).map(mapper::toDomain);
    }

    @Override
    public Optional<ContentPage> fetchContentPage(ContentPageSearchCriteria criteria) {
        if (criteria.contentPageId() == null) {
            return Optional.empty();
        }
        return queryDslRepository
                .fetchByIdWithBypass(criteria.contentPageId(), criteria.bypass())
                .map(mapper::toDomain);
    }

    @Override
    public List<ContentPage> findByCriteria(ContentPageListSearchCriteria criteria) {
        return queryDslRepository.searchContentPages(criteria).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByCriteria(ContentPageListSearchCriteria criteria) {
        return queryDslRepository.countContentPages(criteria);
    }
}
