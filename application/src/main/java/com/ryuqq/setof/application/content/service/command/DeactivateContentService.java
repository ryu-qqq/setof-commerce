package com.ryuqq.setof.application.content.service.command;

import com.ryuqq.setof.application.content.dto.command.DeactivateContentCommand;
import com.ryuqq.setof.application.content.manager.command.ContentPersistenceManager;
import com.ryuqq.setof.application.content.manager.query.ContentReadManager;
import com.ryuqq.setof.application.content.port.in.command.DeactivateContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Content 비활성화 Service
 *
 * <p>콘텐츠 비활성화를 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeactivateContentService implements DeactivateContentUseCase {

    private final ContentReadManager contentReadManager;
    private final ContentPersistenceManager contentPersistenceManager;

    public DeactivateContentService(
            ContentReadManager contentReadManager,
            ContentPersistenceManager contentPersistenceManager) {
        this.contentReadManager = contentReadManager;
        this.contentPersistenceManager = contentPersistenceManager;
    }

    @Override
    @Transactional
    public void execute(DeactivateContentCommand command) {
        // 1. 기존 Content 조회
        Content content = contentReadManager.findById(command.contentId());

        // 2. Domain 비활성화 처리
        content.deactivate();

        // 3. 영속화 (Manager)
        contentPersistenceManager.persist(content);
    }
}
