package com.ryuqq.setof.application.noticetemplate.port.out.command;

import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;

/**
 * 상품고시 템플릿 영속성 Port Out
 *
 * <p>상품고시 템플릿 저장/수정을 위한 Port입니다.
 *
 * <p>규칙:
 *
 * <ul>
 *   <li>persist(): 저장/수정 통합 (JPA merge 활용 - ID 유무로 INSERT/UPDATE 분기)
 *   <li>purge(): Hard Delete (Soft Delete 권장하나 필요 시 사용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface NoticeTemplatePersistencePort {

    /**
     * 상품고시 템플릿 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param noticeTemplate 저장/수정할 템플릿
     * @return 저장된 템플릿 ID (Value Object)
     */
    NoticeTemplateId persist(NoticeTemplate noticeTemplate);

    /**
     * 상품고시 템플릿 물리 삭제 (Hard Delete)
     *
     * <p>Soft Delete 권장하나, 템플릿 완전 삭제가 필요한 경우 사용
     *
     * @param templateId 삭제할 템플릿 ID
     */
    void purge(NoticeTemplateId templateId);
}
