package com.ryuqq.setof.application.contentpage.facade;

import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.manager.ComponentProductReadFacade;
import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.manager.DisplayComponentReadManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ContentPageDetailReadFacade - 콘텐츠 페이지 상세 조회 ReadFacade.
 *
 * <p>ContentPageQueryManager, DisplayComponentReadManager, ComponentProductReadFacade를 조합하여 콘텐츠 메타
 * + 컴포넌트 상세 + 상품 썸네일을 하나의 결과로 반환한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageDetailReadFacade {

    private final ContentPageQueryManager contentPageQueryManager;
    private final DisplayComponentReadManager displayComponentReadManager;
    private final ComponentProductReadFacade componentProductReadFacade;

    public ContentPageDetailReadFacade(
            ContentPageQueryManager contentPageQueryManager,
            DisplayComponentReadManager displayComponentReadManager,
            ComponentProductReadFacade componentProductReadFacade) {
        this.contentPageQueryManager = contentPageQueryManager;
        this.displayComponentReadManager = displayComponentReadManager;
        this.componentProductReadFacade = componentProductReadFacade;
    }

    /**
     * 콘텐츠 페이지 상세 조회.
     *
     * @param criteria 검색 조건
     * @return ContentPageDetailResult (메타 + 컴포넌트 + 상품 번들)
     */
    public ContentPageDetailResult getContentPageDetail(ContentPageSearchCriteria criteria) {
        ContentPage page = contentPageQueryManager.fetchContentPage(criteria);
        List<DisplayComponent> components =
                displayComponentReadManager.fetchDisplayComponents(criteria);
        ComponentProductBundle productBundle =
                componentProductReadFacade.fetchComponentProducts(components);
        return new ContentPageDetailResult(page, components, productBundle);
    }
}
