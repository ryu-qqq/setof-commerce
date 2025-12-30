package com.ryuqq.setof.application.noticetemplate.service.command;

import com.ryuqq.setof.application.noticetemplate.dto.command.UpdateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.factory.command.NoticeTemplateCommandFactory;
import com.ryuqq.setof.application.noticetemplate.manager.command.NoticeTemplatePersistenceManager;
import com.ryuqq.setof.application.noticetemplate.manager.query.NoticeTemplateReadManager;
import com.ryuqq.setof.application.noticetemplate.port.in.command.UpdateNoticeTemplateUseCase;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import org.springframework.stereotype.Service;

/**
 * 상품고시 템플릿 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateNoticeTemplateService implements UpdateNoticeTemplateUseCase {

    private final NoticeTemplateCommandFactory commandFactory;
    private final NoticeTemplatePersistenceManager persistenceManager;
    private final NoticeTemplateReadManager readManager;

    public UpdateNoticeTemplateService(
            NoticeTemplateCommandFactory commandFactory,
            NoticeTemplatePersistenceManager persistenceManager,
            NoticeTemplateReadManager readManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
    }

    /**
     * 상품고시 템플릿 수정
     *
     * @param command 수정 Command
     */
    @Override
    public void update(UpdateNoticeTemplateCommand command) {
        // 기존 템플릿 조회
        NoticeTemplate existing = readManager.findById(command.templateId());

        // Domain 수정
        NoticeTemplate updated = commandFactory.applyUpdate(command, existing);

        // 저장 (persist는 ID 유무로 INSERT/UPDATE 분기)
        persistenceManager.persist(updated);
    }
}
