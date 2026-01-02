package com.ryuqq.setof.application.noticetemplate.service.command;

import com.ryuqq.setof.application.noticetemplate.manager.command.NoticeTemplatePersistenceManager;
import com.ryuqq.setof.application.noticetemplate.manager.query.NoticeTemplateReadManager;
import com.ryuqq.setof.application.noticetemplate.port.in.command.DeleteNoticeTemplateUseCase;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import org.springframework.stereotype.Service;

/**
 * 상품고시 템플릿 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteNoticeTemplateService implements DeleteNoticeTemplateUseCase {

    private final NoticeTemplatePersistenceManager persistenceManager;
    private final NoticeTemplateReadManager readManager;

    public DeleteNoticeTemplateService(
            NoticeTemplatePersistenceManager persistenceManager,
            NoticeTemplateReadManager readManager) {
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
    }

    /**
     * 상품고시 템플릿 삭제
     *
     * @param templateId 삭제할 템플릿 ID
     */
    @Override
    public void delete(NoticeTemplateId templateId) {
        // 존재 여부 확인
        readManager.findById(templateId);

        // 물리 삭제 (Hard Delete)
        persistenceManager.purge(templateId);
    }
}
