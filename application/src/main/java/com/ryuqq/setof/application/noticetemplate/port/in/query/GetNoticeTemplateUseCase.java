package com.ryuqq.setof.application.noticetemplate.port.in.query;

import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateResponse;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.util.Optional;

/**
 * 상품고시 템플릿 단건 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetNoticeTemplateUseCase {

    /**
     * ID로 상품고시 템플릿 조회
     *
     * @param templateId 템플릿 ID
     * @return 템플릿 Response
     */
    NoticeTemplateResponse getById(NoticeTemplateId templateId);

    /**
     * 카테고리 ID로 상품고시 템플릿 조회
     *
     * @param categoryId 카테고리 ID
     * @return 템플릿 Response (없으면 Optional.empty)
     */
    Optional<NoticeTemplateResponse> getByCategoryId(CategoryId categoryId);
}
