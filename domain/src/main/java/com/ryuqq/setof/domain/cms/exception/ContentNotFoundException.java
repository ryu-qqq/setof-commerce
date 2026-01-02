package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ContentNotFoundException - Content를 찾을 수 없을 때 발생
 *
 * <p>HTTP 응답: 404 NOT FOUND
 *
 * @author development-team
 * @since 1.0.0
 */
public class ContentNotFoundException extends DomainException {

    /**
     * ID로 Content를 찾을 수 없을 때 생성
     *
     * @param contentId Content ID
     */
    public ContentNotFoundException(Long contentId) {
        super(
                CmsErrorCode.CONTENT_NOT_FOUND,
                String.format("Content not found: %d", contentId),
                Map.of("contentId", contentId));
    }
}
