package com.ryuqq.setof.application.noticetemplate.factory.command;

import com.ryuqq.setof.application.noticetemplate.dto.command.CreateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.dto.command.NoticeTemplateFieldDto;
import com.ryuqq.setof.application.noticetemplate.dto.command.UpdateNoticeTemplateCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품고시 템플릿 Command Factory
 *
 * <p>Command DTO → Domain 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class NoticeTemplateCommandFactory {

    private final ClockHolder clockHolder;

    public NoticeTemplateCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateCommand → Domain 변환
     *
     * @param command 생성 Command
     * @return NoticeTemplate 도메인
     */
    public NoticeTemplate create(CreateNoticeTemplateCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        List<NoticeField> requiredFields = new ArrayList<>();
        List<NoticeField> optionalFields = new ArrayList<>();

        if (command.fields() != null) {
            for (NoticeTemplateFieldDto fieldDto : command.fields()) {
                NoticeField field = toNoticeField(fieldDto);
                if (fieldDto.required()) {
                    requiredFields.add(field);
                } else {
                    optionalFields.add(field);
                }
            }
        }

        return NoticeTemplate.forNew(
                command.categoryId().value(),
                command.templateName(),
                requiredFields,
                optionalFields,
                now);
    }

    /**
     * UpdateCommand + 기존 Domain → 수정된 Domain 변환
     *
     * @param command 수정 Command
     * @param existing 기존 템플릿 도메인
     * @return 수정된 NoticeTemplate 도메인
     */
    public NoticeTemplate applyUpdate(
            UpdateNoticeTemplateCommand command, NoticeTemplate existing) {
        Instant now = Instant.now(clockHolder.getClock());
        NoticeTemplate result = existing;

        // 템플릿명 수정
        if (command.templateName() != null && !command.templateName().isBlank()) {
            result = result.rename(command.templateName(), now);
        }

        // 필드 수정
        if (command.fields() != null) {
            List<NoticeField> requiredFields = new ArrayList<>();
            List<NoticeField> optionalFields = new ArrayList<>();

            for (NoticeTemplateFieldDto fieldDto : command.fields()) {
                NoticeField field = toNoticeField(fieldDto);
                if (fieldDto.required()) {
                    requiredFields.add(field);
                } else {
                    optionalFields.add(field);
                }
            }

            result = result.replaceAllFields(requiredFields, optionalFields, now);
        }

        return result;
    }

    /**
     * DTO → NoticeField 변환
     *
     * @param dto 필드 DTO
     * @return NoticeField VO
     */
    private NoticeField toNoticeField(NoticeTemplateFieldDto dto) {
        return NoticeField.of(dto.key(), dto.description(), dto.required(), dto.displayOrder());
    }
}
