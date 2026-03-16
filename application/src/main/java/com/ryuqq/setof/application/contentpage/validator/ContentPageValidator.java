package com.ryuqq.setof.application.contentpage.validator;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import org.springframework.stereotype.Component;

/**
 * ContentPageValidator - 콘텐츠 페이지 검증 컴포넌트.
 *
 * <p>APP-VAL-001: Validator는 존재 여부 확인 및 비즈니스 제약 검증을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentPageValidator {

    private final ContentPageQueryManager contentPageQueryManager;

    public ContentPageValidator(ContentPageQueryManager contentPageQueryManager) {
        this.contentPageQueryManager = contentPageQueryManager;
    }

    /**
     * ID로 콘텐츠 페이지를 조회하고, 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param id 콘텐츠 페이지 ID VO
     * @return 조회된 ContentPage
     */
    public ContentPage findExistingOrThrow(ContentPageId id) {
        return contentPageQueryManager.findByIdOrThrow(id.value());
    }
}
