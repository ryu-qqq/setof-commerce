package com.ryuqq.setof.storage.legacy.composite.content.adapter;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentCompositeQueryAdapter - 콘텐츠 페이지 메타 조회 Adapter.
 *
 * <p>ContentPageQueryPort를 구현하여 콘텐츠 메타데이터만 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
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
}
