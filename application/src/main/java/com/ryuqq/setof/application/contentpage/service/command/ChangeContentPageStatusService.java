package com.ryuqq.setof.application.contentpage.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageCommandManager;
import com.ryuqq.setof.application.contentpage.port.in.command.ChangeContentPageStatusUseCase;
import com.ryuqq.setof.application.contentpage.validator.ContentPageValidator;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import org.springframework.stereotype.Service;

/**
 * ChangeContentPageStatusService - 콘텐츠 페이지 노출 상태 변경 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 StatusChangeContext 생성
 *   <li>Validator를 통해 콘텐츠 페이지 존재 확인
 *   <li>도메인 changeDisplayStatus() 호출
 *   <li>Manager를 통해 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ChangeContentPageStatusService implements ChangeContentPageStatusUseCase {

    private final ContentPageCommandFactory contentPageCommandFactory;
    private final ContentPageValidator contentPageValidator;
    private final ContentPageCommandManager contentPageCommandManager;

    public ChangeContentPageStatusService(
            ContentPageCommandFactory contentPageCommandFactory,
            ContentPageValidator contentPageValidator,
            ContentPageCommandManager contentPageCommandManager) {
        this.contentPageCommandFactory = contentPageCommandFactory;
        this.contentPageValidator = contentPageValidator;
        this.contentPageCommandManager = contentPageCommandManager;
    }

    @Override
    public void execute(ChangeContentPageStatusCommand command) {
        StatusChangeContext<ContentPageId> context =
                contentPageCommandFactory.createStatusChangeContext(command);

        ContentPage contentPage = contentPageValidator.findExistingOrThrow(context.id());
        contentPage.changeDisplayStatus(command.active(), context.changedAt());

        contentPageCommandManager.persist(contentPage);
    }
}
