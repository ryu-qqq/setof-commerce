package com.ryuqq.setof.application.noticetemplate.port.in.command;

import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;

/**
 * 상품고시 템플릿 삭제 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteNoticeTemplateUseCase {

    /**
     * 상품고시 템플릿 삭제
     *
     * @param templateId 삭제할 템플릿 ID
     */
    void delete(NoticeTemplateId templateId);
}
