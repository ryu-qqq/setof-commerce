package com.ryuqq.setof.application.content.port.out.command;

import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentId;

/**
 * Content Persistence Port (Command)
 *
 * <p>Content Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ContentPersistencePort {

    /**
     * Content 저장 (신규 생성 또는 수정)
     *
     * @param content 저장할 Content (Domain Aggregate)
     * @return 저장된 Content의 ID
     */
    ContentId persist(Content content);
}
