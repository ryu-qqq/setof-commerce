package com.ryuqq.setof.application.content.manager.command;

import com.ryuqq.setof.application.content.port.out.command.ContentPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Content Persistence Manager
 *
 * <p>Content 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentPersistenceManager {

    private final ContentPersistencePort contentPersistencePort;

    public ContentPersistenceManager(ContentPersistencePort contentPersistencePort) {
        this.contentPersistencePort = contentPersistencePort;
    }

    /**
     * Content 저장
     *
     * @param content 저장할 Content
     * @return 저장된 Content의 ID
     */
    @Transactional
    public ContentId persist(Content content) {
        return contentPersistencePort.persist(content);
    }
}
