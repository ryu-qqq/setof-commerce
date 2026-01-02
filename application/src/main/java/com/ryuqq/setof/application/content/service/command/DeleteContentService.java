package com.ryuqq.setof.application.content.service.command;

import com.ryuqq.setof.application.content.dto.command.DeleteContentCommand;
import com.ryuqq.setof.application.content.manager.command.ContentPersistenceManager;
import com.ryuqq.setof.application.content.manager.query.ContentReadManager;
import com.ryuqq.setof.application.content.port.in.command.DeleteContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Content 삭제 Service
 *
 * <p>콘텐츠 소프트 삭제를 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteContentService implements DeleteContentUseCase {

    private final ContentReadManager contentReadManager;
    private final ContentPersistenceManager contentPersistenceManager;

    public DeleteContentService(
            ContentReadManager contentReadManager,
            ContentPersistenceManager contentPersistenceManager) {
        this.contentReadManager = contentReadManager;
        this.contentPersistenceManager = contentPersistenceManager;
    }

    @Override
    @Transactional
    public void execute(DeleteContentCommand command) {
        // 1. 기존 Content 조회
        Content content = contentReadManager.findById(command.contentId());

        // 2. Domain 삭제 처리
        content.delete();

        // 3. 영속화 (Manager)
        contentPersistenceManager.persist(content);
    }
}
