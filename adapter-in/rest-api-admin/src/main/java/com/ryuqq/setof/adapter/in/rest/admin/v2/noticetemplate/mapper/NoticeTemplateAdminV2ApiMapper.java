package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command.NoticeTemplateFieldV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command.RegisterNoticeTemplateV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command.UpdateNoticeTemplateV2ApiRequest;
import com.ryuqq.setof.application.noticetemplate.dto.command.CreateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.dto.command.NoticeTemplateFieldDto;
import com.ryuqq.setof.application.noticetemplate.dto.command.UpdateNoticeTemplateCommand;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * NoticeTemplate Admin V2 API Mapper
 *
 * <p>상품고시 템플릿 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class NoticeTemplateAdminV2ApiMapper {

    /**
     * 등록 요청 → 등록 커맨드 변환
     *
     * @param request API 요청 DTO
     * @return 등록 Command
     */
    public CreateNoticeTemplateCommand toCreateCommand(RegisterNoticeTemplateV2ApiRequest request) {
        List<NoticeTemplateFieldDto> fieldDtos = toFieldDtos(request.fields());
        return new CreateNoticeTemplateCommand(
                CategoryId.of(request.categoryId()), request.templateName(), fieldDtos);
    }

    /**
     * 수정 요청 → 수정 커맨드 변환
     *
     * @param templateId 템플릿 ID
     * @param request API 요청 DTO
     * @return 수정 Command
     */
    public UpdateNoticeTemplateCommand toUpdateCommand(
            Long templateId, UpdateNoticeTemplateV2ApiRequest request) {
        List<NoticeTemplateFieldDto> fieldDtos =
                request.fields() != null ? toFieldDtos(request.fields()) : null;
        return new UpdateNoticeTemplateCommand(
                NoticeTemplateId.of(templateId), request.templateName(), fieldDtos);
    }

    /**
     * 필드 요청 목록 → 필드 DTO 목록 변환
     *
     * @param fields API 필드 요청 목록
     * @return 필드 DTO 목록
     */
    private List<NoticeTemplateFieldDto> toFieldDtos(List<NoticeTemplateFieldV2ApiRequest> fields) {
        return fields.stream()
                .map(
                        field ->
                                new NoticeTemplateFieldDto(
                                        field.key(),
                                        field.description(),
                                        field.required(),
                                        field.displayOrder()))
                .toList();
    }
}
