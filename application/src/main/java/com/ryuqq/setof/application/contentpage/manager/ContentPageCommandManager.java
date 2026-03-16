package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageCommandPort;
import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentCommandPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.DisplayComponentDiff;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ContentPageCommandManager - 콘텐츠 페이지 Command 매니저.
 *
 * <p>APP-MGR-001: Manager는 @Transactional을 선언하고 Port-Out을 호출합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageCommandManager {

    private final ContentPageCommandPort contentPageCommandPort;
    private final DisplayComponentCommandPort displayComponentCommandPort;

    public ContentPageCommandManager(
            ContentPageCommandPort contentPageCommandPort,
            DisplayComponentCommandPort displayComponentCommandPort) {
        this.contentPageCommandPort = contentPageCommandPort;
        this.displayComponentCommandPort = displayComponentCommandPort;
    }

    /**
     * 콘텐츠 페이지를 저장합니다.
     *
     * @param contentPage 저장할 콘텐츠 페이지
     * @return 생성된 콘텐츠 페이지 ID
     */
    @Transactional
    public Long persist(ContentPage contentPage) {
        return contentPageCommandPort.persist(contentPage);
    }

    /**
     * 컴포넌트 목록을 신규 저장합니다.
     *
     * @param components 저장할 컴포넌트 목록
     */
    @Transactional
    public void persistComponents(List<DisplayComponent> components) {
        if (!components.isEmpty()) {
            displayComponentCommandPort.persistAll(components);
        }
    }

    /**
     * 컴포넌트 Diff 결과를 영속합니다.
     *
     * <p>added: 신규 컴포넌트 저장. allDirtyComponents (retained + removed): 기존 컴포넌트 상태 갱신.
     *
     * @param diff 컴포넌트 변경 비교 결과
     */
    @Transactional
    public void persistComponentDiff(DisplayComponentDiff diff) {
        if (!diff.added().isEmpty()) {
            displayComponentCommandPort.persistAll(diff.added());
        }
        if (!diff.allDirtyComponents().isEmpty()) {
            displayComponentCommandPort.updateAll(diff.allDirtyComponents());
        }
    }
}
