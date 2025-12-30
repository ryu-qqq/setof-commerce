package com.ryuqq.setof.adapter.out.persistence.noticetemplate.adapter;

import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.mapper.NoticeTemplateJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository.NoticeTemplateFieldJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository.NoticeTemplateFieldQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository.NoticeTemplateJpaRepository;
import com.ryuqq.setof.application.noticetemplate.port.out.command.NoticeTemplatePersistencePort;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * NoticeTemplatePersistenceAdapter - NoticeTemplate Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, NoticeTemplate 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class NoticeTemplatePersistenceAdapter implements NoticeTemplatePersistencePort {

    private final NoticeTemplateJpaRepository jpaRepository;
    private final NoticeTemplateFieldJpaRepository fieldJpaRepository;
    private final NoticeTemplateFieldQueryDslRepository fieldQueryDslRepository;
    private final NoticeTemplateJpaEntityMapper mapper;

    public NoticeTemplatePersistenceAdapter(
            NoticeTemplateJpaRepository jpaRepository,
            NoticeTemplateFieldJpaRepository fieldJpaRepository,
            NoticeTemplateFieldQueryDslRepository fieldQueryDslRepository,
            NoticeTemplateJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.fieldJpaRepository = fieldJpaRepository;
        this.fieldQueryDslRepository = fieldQueryDslRepository;
        this.mapper = mapper;
    }

    /**
     * NoticeTemplate 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param noticeTemplate 저장/수정할 템플릿
     * @return 저장된 템플릿 ID (Value Object)
     */
    @Override
    public NoticeTemplateId persist(NoticeTemplate noticeTemplate) {
        // 1. 메인 Entity 저장 (JPA merge: ID 유무로 INSERT/UPDATE 분기)
        NoticeTemplateJpaEntity entity = mapper.toEntity(noticeTemplate);
        NoticeTemplateJpaEntity savedEntity = jpaRepository.save(entity);
        Long templateId = savedEntity.getId();

        // 2. 수정 시 기존 필드 삭제
        if (noticeTemplate.getId() != null) {
            List<NoticeTemplateFieldJpaEntity> existingFields =
                    fieldQueryDslRepository.findByTemplateId(templateId);
            if (!existingFields.isEmpty()) {
                fieldJpaRepository.deleteAll(existingFields);
            }
        }

        // 3. 필드 Entity 저장
        saveFields(noticeTemplate, templateId);

        return NoticeTemplateId.of(templateId);
    }

    /**
     * NoticeTemplate 물리 삭제 (Hard Delete)
     *
     * @param templateId 삭제할 템플릿 ID
     */
    @Override
    public void purge(NoticeTemplateId templateId) {
        Long idValue = templateId.value();
        // 1. 필드 먼저 삭제
        List<NoticeTemplateFieldJpaEntity> existingFields =
                fieldQueryDslRepository.findByTemplateId(idValue);
        if (!existingFields.isEmpty()) {
            fieldJpaRepository.deleteAll(existingFields);
        }

        // 2. 메인 Entity 삭제
        jpaRepository.deleteById(idValue);
    }

    /**
     * 필드 저장 (필수 + 선택 필드 통합)
     *
     * @param noticeTemplate 템플릿
     * @param templateId 템플릿 ID
     */
    private void saveFields(NoticeTemplate noticeTemplate, Long templateId) {
        List<NoticeField> allFields = new ArrayList<>();
        allFields.addAll(noticeTemplate.getRequiredFields());
        allFields.addAll(noticeTemplate.getOptionalFields());

        List<NoticeTemplateFieldJpaEntity> fieldEntities =
                mapper.toFieldEntities(allFields, templateId);
        if (!fieldEntities.isEmpty()) {
            fieldJpaRepository.saveAll(fieldEntities);
        }
    }
}
