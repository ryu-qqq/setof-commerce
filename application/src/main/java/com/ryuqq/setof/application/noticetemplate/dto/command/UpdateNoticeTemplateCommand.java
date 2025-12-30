package com.ryuqq.setof.application.noticetemplate.dto.command;

import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.util.List;

/**
 * 상품고시 템플릿 수정 Command
 *
 * @param templateId 템플릿 ID
 * @param templateName 템플릿명 (null이면 변경 안함)
 * @param fields 필드 목록 (null이면 변경 안함, 빈 리스트면 모두 삭제)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateNoticeTemplateCommand(
        NoticeTemplateId templateId, String templateName, List<NoticeTemplateFieldDto> fields) {}
