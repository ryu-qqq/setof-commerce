package com.ryuqq.setof.storage.legacy.composite.web.content.adapter;

import com.ryuqq.setof.application.legacy.content.dto.response.LegacyComponentResult;
import com.ryuqq.setof.application.legacy.content.dto.response.LegacyContentResult;
import com.ryuqq.setof.domain.legacy.content.dto.query.LegacyContentSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebContentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebViewExtensionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.web.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentCompositeQueryAdapter - 레거시 웹 콘텐츠 Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyContentCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebContentCompositeQueryAdapter {

    private final LegacyWebContentCompositeQueryDslRepository repository;
    private final LegacyWebContentMapper mapper;

    public LegacyWebContentCompositeQueryAdapter(
            LegacyWebContentCompositeQueryDslRepository repository, LegacyWebContentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 전시 중인 콘텐츠 ID 목록 조회.
     *
     * @return 전시 중인 콘텐츠 ID Set
     */
    public Set<Long> fetchOnDisplayContentIds() {
        List<Long> contentIds = repository.fetchOnDisplayContentIds();
        return new HashSet<>(contentIds);
    }

    /**
     * 콘텐츠 메타데이터 조회 (컴포넌트 없이).
     *
     * @param contentId 콘텐츠 ID
     * @return LegacyContentResult Optional
     */
    public Optional<LegacyContentResult> fetchContentMetaData(Long contentId) {
        return repository.fetchContentById(contentId).map(mapper::toMetaDataResult);
    }

    /**
     * 콘텐츠 상세 조회 (컴포넌트 포함).
     *
     * @param condition 검색 조건
     * @return LegacyContentResult Optional
     */
    public Optional<LegacyContentResult> fetchContent(LegacyContentSearchCondition condition) {
        // 1. 콘텐츠 조회
        Optional<LegacyWebContentQueryDto> contentOpt =
                repository.fetchContent(condition.contentId());
        if (contentOpt.isEmpty()) {
            return Optional.empty();
        }

        LegacyWebContentQueryDto content = contentOpt.get();

        // 2. 컴포넌트 조회
        List<LegacyWebComponentQueryDto> components =
                repository.fetchComponentsByContentId(condition);

        // 3. 뷰 확장 ID 추출 및 조회
        List<Long> viewExtensionIds =
                components.stream()
                        .map(LegacyWebComponentQueryDto::viewExtensionId)
                        .filter(Objects::nonNull)
                        .toList();

        List<LegacyWebViewExtensionQueryDto> viewExtensions =
                repository.fetchViewExtensionsByIds(viewExtensionIds);
        Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap =
                mapper.toViewExtensionMap(viewExtensions);

        // 4. 매핑 및 반환
        List<LegacyComponentResult> componentResults =
                mapper.toComponentResults(components, viewExtensionMap);
        return Optional.of(mapper.toResult(content, componentResults));
    }

    /**
     * 컴포넌트 목록 조회.
     *
     * @param condition 검색 조건
     * @return LegacyComponentResult 목록
     */
    public List<LegacyComponentResult> fetchComponents(LegacyContentSearchCondition condition) {
        List<LegacyWebComponentQueryDto> components =
                repository.fetchComponentsByContentId(condition);

        List<Long> viewExtensionIds =
                components.stream()
                        .map(LegacyWebComponentQueryDto::viewExtensionId)
                        .filter(Objects::nonNull)
                        .toList();

        List<LegacyWebViewExtensionQueryDto> viewExtensions =
                repository.fetchViewExtensionsByIds(viewExtensionIds);
        Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap =
                mapper.toViewExtensionMap(viewExtensions);

        return mapper.toComponentResults(components, viewExtensionMap);
    }
}
