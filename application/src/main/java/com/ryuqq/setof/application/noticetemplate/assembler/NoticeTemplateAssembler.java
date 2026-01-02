package com.ryuqq.setof.application.noticetemplate.assembler;

import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateFieldResponse;
import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateResponse;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품고시 템플릿 Assembler
 *
 * <p>Domain ↔ Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class NoticeTemplateAssembler {

    /**
     * Domain → Response 변환
     *
     * @param domain 템플릿 도메인
     * @return Response DTO
     */
    public NoticeTemplateResponse toResponse(NoticeTemplate domain) {
        List<NoticeTemplateFieldResponse> requiredFieldResponses =
                domain.getRequiredFields().stream().map(this::toFieldResponse).toList();

        List<NoticeTemplateFieldResponse> optionalFieldResponses =
                domain.getOptionalFields().stream().map(this::toFieldResponse).toList();

        return new NoticeTemplateResponse(
                domain.getIdValue(),
                domain.getCategoryId(),
                domain.getTemplateName(),
                requiredFieldResponses,
                optionalFieldResponses,
                domain.getTotalFieldCount(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * 필드 Domain → Response 변환
     *
     * @param field 필드 VO
     * @return 필드 Response DTO
     */
    private NoticeTemplateFieldResponse toFieldResponse(NoticeField field) {
        return new NoticeTemplateFieldResponse(
                field.key(), field.description(), field.required(), field.displayOrder());
    }
}
