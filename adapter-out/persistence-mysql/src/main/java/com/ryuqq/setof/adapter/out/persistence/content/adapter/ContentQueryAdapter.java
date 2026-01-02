package com.ryuqq.setof.adapter.out.persistence.content.adapter;

import com.ryuqq.setof.adapter.out.persistence.content.mapper.ContentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.content.repository.ContentQueryDslRepository;
import com.ryuqq.setof.application.content.port.out.query.ContentQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ContentQueryAdapter - Content Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Content 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentQueryAdapter implements ContentQueryPort {

    private final ContentQueryDslRepository queryDslRepository;
    private final ContentJpaEntityMapper mapper;

    public ContentQueryAdapter(
            ContentQueryDslRepository queryDslRepository, ContentJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /** ID로 Content 조회 */
    @Override
    public Optional<Content> findById(ContentId contentId) {
        return queryDslRepository.findById(contentId.value()).map(mapper::toDomain);
    }

    /** 검색 조건으로 Content 목록 조회 */
    @Override
    public List<Content> findByCriteria(ContentSearchCriteria criteria) {
        return queryDslRepository.findByCriteria(criteria).stream().map(mapper::toDomain).toList();
    }

    /** 검색 조건에 맞는 총 개수 조회 */
    @Override
    public long countByCriteria(ContentSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    /** ID로 존재 여부 확인 */
    @Override
    public boolean existsById(ContentId contentId) {
        return queryDslRepository.existsById(contentId.value());
    }
}
