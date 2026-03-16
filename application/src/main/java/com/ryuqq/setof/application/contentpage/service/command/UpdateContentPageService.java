package com.ryuqq.setof.application.contentpage.service.command;

import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.facade.ContentPageCommandFacade;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.manager.DisplayComponentReadManager;
import com.ryuqq.setof.application.contentpage.port.in.command.UpdateContentPageUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * UpdateContentPageService - 콘텐츠 페이지 수정 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>ReadManager로 기존 ContentPage + Components 조회
 *   <li>Factory로 수정 번들 생성
 *   <li>Facade로 도메인 update + 컴포넌트 diff + 일괄 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateContentPageService implements UpdateContentPageUseCase {

    private final ContentPageQueryManager contentPageQueryManager;
    private final DisplayComponentReadManager displayComponentReadManager;
    private final ContentPageCommandFactory contentPageCommandFactory;
    private final ContentPageCommandFacade contentPageCommandFacade;

    public UpdateContentPageService(
            ContentPageQueryManager contentPageQueryManager,
            DisplayComponentReadManager displayComponentReadManager,
            ContentPageCommandFactory contentPageCommandFactory,
            ContentPageCommandFacade contentPageCommandFacade) {
        this.contentPageQueryManager = contentPageQueryManager;
        this.displayComponentReadManager = displayComponentReadManager;
        this.contentPageCommandFactory = contentPageCommandFactory;
        this.contentPageCommandFacade = contentPageCommandFacade;
    }

    @Override
    public void execute(UpdateContentPageCommand command) {
        long contentPageId = command.contentPageId();
        ContentPage contentPage = contentPageQueryManager.findByIdOrThrow(contentPageId);

        ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(contentPageId, true);
        List<DisplayComponent> existingComponents =
                displayComponentReadManager.findByContentPage(criteria);

        ContentPageUpdateBundle bundle =
                contentPageCommandFactory.createUpdateBundle(
                        contentPage, existingComponents, command);
        contentPageCommandFacade.update(bundle, command);
    }
}
