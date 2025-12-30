package com.ryuqq.setof.adapter.out.persistence.noticetemplate.mapper;

import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateJpaEntity;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * NoticeTemplateJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 Mapper로서 Entity와 Domain 객체 간 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class NoticeTemplateJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (메인 Entity만)
     *
     * @param domain NoticeTemplate 도메인
     * @return NoticeTemplateJpaEntity
     */
    public NoticeTemplateJpaEntity toEntity(NoticeTemplate domain) {
        return NoticeTemplateJpaEntity.of(
                domain.getIdValue(),
                domain.getCategoryId(),
                domain.getTemplateName(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Domain 필드 -> Entity 필드 변환
     *
     * @param field NoticeField 도메인
     * @param templateId 템플릿 ID
     * @return NoticeTemplateFieldJpaEntity
     */
    public NoticeTemplateFieldJpaEntity toFieldEntity(NoticeField field, Long templateId) {
        return NoticeTemplateFieldJpaEntity.of(
                null, // 신규 생성 시 ID는 null
                templateId,
                field.key(),
                field.description(),
                field.required(),
                field.displayOrder());
    }

    /**
     * Domain 필드 목록 -> Entity 필드 목록 변환
     *
     * @param fields NoticeField 도메인 목록
     * @param templateId 템플릿 ID
     * @return NoticeTemplateFieldJpaEntity 목록
     */
    public List<NoticeTemplateFieldJpaEntity> toFieldEntities(
            List<NoticeField> fields, Long templateId) {
        if (fields == null || fields.isEmpty()) {
            return List.of();
        }
        return fields.stream().map(f -> toFieldEntity(f, templateId)).toList();
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity NoticeTemplateJpaEntity
     * @param fieldEntities 필드 Entity 목록
     * @return NoticeTemplate 도메인
     */
    public NoticeTemplate toDomain(
            NoticeTemplateJpaEntity entity, List<NoticeTemplateFieldJpaEntity> fieldEntities) {
        List<NoticeField> requiredFields = new ArrayList<>();
        List<NoticeField> optionalFields = new ArrayList<>();

        if (fieldEntities != null) {
            for (NoticeTemplateFieldJpaEntity fieldEntity : fieldEntities) {
                NoticeField field = toFieldDomain(fieldEntity);
                if (fieldEntity.isRequired()) {
                    requiredFields.add(field);
                } else {
                    optionalFields.add(field);
                }
            }
        }

        return NoticeTemplate.reconstitute(
                NoticeTemplateId.of(entity.getId()),
                entity.getCategoryId(),
                entity.getTemplateName(),
                requiredFields,
                optionalFields,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Entity 필드 -> Domain 필드 변환
     *
     * @param entity 필드 Entity
     * @return NoticeField 도메인
     */
    private NoticeField toFieldDomain(NoticeTemplateFieldJpaEntity entity) {
        return NoticeField.of(
                entity.getFieldKey(),
                entity.getDescription(),
                entity.isRequired(),
                entity.getDisplayOrder());
    }
}
