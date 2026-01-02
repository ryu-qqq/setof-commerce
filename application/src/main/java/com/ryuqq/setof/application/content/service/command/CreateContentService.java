package com.ryuqq.setof.application.content.service.command;

import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.factory.command.ContentCommandFactory;
import com.ryuqq.setof.application.content.manager.command.ContentPersistenceManager;
import com.ryuqq.setof.application.content.port.in.command.CreateContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import org.springframework.stereotype.Service;

/**
 * Content 생성 Service
 *
 * <p>콘텐츠 생성을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateContentService implements CreateContentUseCase {

    private final ContentCommandFactory contentCommandFactory;
    private final ContentPersistenceManager contentPersistenceManager;

    public CreateContentService(
            ContentCommandFactory contentCommandFactory,
            ContentPersistenceManager contentPersistenceManager) {
        this.contentCommandFactory = contentCommandFactory;
        this.contentPersistenceManager = contentPersistenceManager;
    }

    @Override
    public Long execute(CreateContentCommand command) {
        // 1. Command → Domain (Factory)
        Content content = contentCommandFactory.create(command);

        // 2. 영속화 (Manager)
        ContentId savedId = contentPersistenceManager.persist(content);

        // 3. ID 반환
        return savedId.value();
    }
}
