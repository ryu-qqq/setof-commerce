package com.ryuqq.setof.storage.legacy.composite.content.adapter;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentCompositeQueryAdapter - 콘텐츠 페이지 메타 조회 Adapter.
 *
 * <p>ContentPageQueryPort를 구현하여 콘텐츠 메타데이터만 조회한다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "true")
public class LegacyWebContentCompositeQueryAdapter implements ContentPageQueryPort {

    private final LegacyWebContentCompositeQueryDslRepository repository;
    private final LegacyWebContentMapper mapper;

    public LegacyWebContentCompositeQueryAdapter(
            LegacyWebContentCompositeQueryDslRepository repository, LegacyWebContentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Set<Long> fetchOnDisplayContentPageIds() {
        List<Long> contentIds = repository.fetchOnDisplayContentIds();
        return new HashSet<>(contentIds);
    }

    @Override
    public Optional<ContentPage> fetchContentPageMeta(long contentPageId) {
        return repository.fetchContentById(contentPageId).map(mapper::toContentPage);
    }

    @Override
    public Optional<ContentPage> fetchContentPage(ContentPageSearchCriteria criteria) {
        return repository.fetchContent(criteria.contentPageId()).map(mapper::toContentPage);
    }

    @Override
    public List<ContentPage> findByCriteria(ContentPageListSearchCriteria criteria) {
        throw new UnsupportedOperationException(
                "레거시 어댑터에서는 목록 검색을 지원하지 않습니다. persistence.legacy.content.enabled=false로 전환하세요.");
    }

    @Override
    public long countByCriteria(ContentPageListSearchCriteria criteria) {
        throw new UnsupportedOperationException(
                "레거시 어댑터에서는 목록 카운트를 지원하지 않습니다. persistence.legacy.content.enabled=false로 전환하세요.");
    }
}
