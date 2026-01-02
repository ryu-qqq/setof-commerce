package com.ryuqq.setof.application.content.port.in.query;

import com.ryuqq.setof.application.content.dto.response.ContentResponse;

/**
 * Content 단건 조회 UseCase (Query)
 *
 * <p>콘텐츠 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetContentUseCase {

    /**
     * 콘텐츠 단건 조회
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 응답
     */
    ContentResponse execute(Long contentId);
}
