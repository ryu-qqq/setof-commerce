package com.ryuqq.setof.application.contentpage.facade;

import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageCommandManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPageUpdateData;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.DisplayComponentDiff;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ContentPageCommandFacade - 콘텐츠 페이지 Command 퍼사드.
 *
 * <p>ContentPage + DisplayComponent를 하나의 트랜잭션으로 묶어 처리합니다. Manager 간의 조합을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageCommandFacade {

    private final ContentPageCommandFactory contentPageCommandFactory;
    private final ContentPageCommandManager contentPageCommandManager;

    public ContentPageCommandFacade(
            ContentPageCommandFactory contentPageCommandFactory,
            ContentPageCommandManager contentPageCommandManager) {
        this.contentPageCommandFactory = contentPageCommandFactory;
        this.contentPageCommandManager = contentPageCommandManager;
    }

    /**
     * 콘텐츠 페이지와 컴포넌트를 함께 등록합니다.
     *
     * @param bundle 등록 번들 (ContentPage + 컴포넌트 Command 목록)
     * @return 생성된 콘텐츠 페이지 ID
     */
    @Transactional
    public Long register(ContentPageRegistrationBundle bundle) {
        Long contentPageId = contentPageCommandManager.persist(bundle.contentPage());

        if (bundle.hasComponents()) {
            List<DisplayComponent> components =
                    contentPageCommandFactory.createComponents(
                            contentPageId, bundle.componentCommands());
            contentPageCommandManager.persistComponents(components);
        }

        return contentPageId;
    }

    /**
     * 콘텐츠 페이지 메타 수정과 컴포넌트 diff를 함께 처리합니다.
     *
     * <p>처리 흐름:
     *
     * <ol>
     *   <li>ContentPage 도메인 update() 적용
     *   <li>ContentPage 저장
     *   <li>incoming 컴포넌트 생성 → 기존 컴포넌트와 diff 계산
     *   <li>컴포넌트 diff 저장
     * </ol>
     *
     * @param bundle 수정 번들 (기존 ContentPage + 기존 Components + Command + 시각)
     * @param command 수정 Command (메타 + 컴포넌트 정보)
     */
    @Transactional
    public void update(ContentPageUpdateBundle bundle, UpdateContentPageCommand command) {
        ContentPage contentPage = bundle.contentPage();
        ContentPageUpdateData updateData =
                contentPageCommandFactory.createUpdateData(command, bundle.updatedAt());
        contentPage.update(updateData);
        contentPageCommandManager.persist(contentPage);

        if (bundle.incomingComponentCommands() != null
                && !bundle.incomingComponentCommands().isEmpty()) {
            List<DisplayComponent> incoming =
                    contentPageCommandFactory.createComponentsForUpdate(
                            command.contentPageId(),
                            bundle.incomingComponentCommands(),
                            bundle.updatedAt());

            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            bundle.existingComponents(), incoming, bundle.updatedAt());

            if (!diff.hasNoChanges()) {
                contentPageCommandManager.persistComponentDiff(diff);
            }
        }
    }
}
