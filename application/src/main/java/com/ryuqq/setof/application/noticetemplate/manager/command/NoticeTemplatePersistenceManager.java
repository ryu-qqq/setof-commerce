package com.ryuqq.setof.application.noticetemplate.manager.command;

import com.ryuqq.setof.application.noticetemplate.port.out.command.NoticeTemplatePersistencePort;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품고시 템플릿 Persistence Manager
 *
 * <p>트랜잭션 경계를 관리하고 Persistence Port에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional
public class NoticeTemplatePersistenceManager {

    private final NoticeTemplatePersistencePort persistencePort;

    public NoticeTemplatePersistenceManager(NoticeTemplatePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    /**
     * 상품고시 템플릿 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param noticeTemplate 저장/수정할 템플릿
     * @return 저장된 템플릿 ID (Value Object)
     */
    public NoticeTemplateId persist(NoticeTemplate noticeTemplate) {
        return persistencePort.persist(noticeTemplate);
    }

    /**
     * 상품고시 템플릿 물리 삭제 (Hard Delete)
     *
     * @param templateId 삭제할 템플릿 ID
     */
    public void purge(NoticeTemplateId templateId) {
        persistencePort.purge(templateId);
    }
}
