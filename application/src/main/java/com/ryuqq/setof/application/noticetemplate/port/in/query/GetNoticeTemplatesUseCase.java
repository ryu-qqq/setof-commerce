package com.ryuqq.setof.application.noticetemplate.port.in.query;

import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateResponse;
import java.util.List;

/**
 * 상품고시 템플릿 목록 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetNoticeTemplatesUseCase {

    /**
     * 모든 상품고시 템플릿 조회
     *
     * @return 템플릿 Response 목록
     */
    List<NoticeTemplateResponse> getAll();
}
