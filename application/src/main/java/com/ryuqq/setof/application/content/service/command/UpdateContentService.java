package com.ryuqq.setof.application.content.service.command;

import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;
import com.ryuqq.setof.application.content.factory.command.ContentCommandFactory;
import com.ryuqq.setof.application.content.manager.command.ContentPersistenceManager;
import com.ryuqq.setof.application.content.manager.query.ContentReadManager;
import com.ryuqq.setof.application.content.port.in.command.UpdateContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentMemo;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Content 수정 Service
 *
 * <p>콘텐츠 수정을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateContentService implements UpdateContentUseCase {

    private final ContentReadManager contentReadManager;
    private final ContentCommandFactory contentCommandFactory;
    private final ContentPersistenceManager contentPersistenceManager;

    public UpdateContentService(
            ContentReadManager contentReadManager,
            ContentCommandFactory contentCommandFactory,
            ContentPersistenceManager contentPersistenceManager) {
        this.contentReadManager = contentReadManager;
        this.contentCommandFactory = contentCommandFactory;
        this.contentPersistenceManager = contentPersistenceManager;
    }

    @Override
    @Transactional
    public void execute(UpdateContentCommand command) {
        // 1. 기존 Content 조회
        Content content = contentReadManager.findById(command.contentId());

        // 2. Command → VOs (Factory)
        ContentTitle title = contentCommandFactory.createTitle(command);
        ContentMemo memo = contentCommandFactory.createMemo(command);
        ImageUrl imageUrl = contentCommandFactory.createImageUrl(command);
        DisplayPeriod displayPeriod = contentCommandFactory.createDisplayPeriod(command);

        // 3. Domain 업데이트
        content.update(title, memo, imageUrl, displayPeriod);

        // 4. 영속화 (Manager)
        contentPersistenceManager.persist(content);
    }
}
