package com.ryuqq.setof.application.noticetemplate.dto.command;

import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.util.List;

/**
 * 상품고시 템플릿 생성 Command
 *
 * @param categoryId 카테고리 ID
 * @param templateName 템플릿명
 * @param fields 필드 목록
 * @author development-team
 * @since 1.0.0
 */
public record CreateNoticeTemplateCommand(
        CategoryId categoryId, String templateName, List<NoticeTemplateFieldDto> fields) {}
