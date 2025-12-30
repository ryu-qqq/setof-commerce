package com.ryuqq.setof.adapter.out.persistence.noticetemplate.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateJpaEntity;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NoticeTemplatePersistenceAdapter 통합 테스트
 *
 * <p>NoticeTemplatePersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("NoticeTemplatePersistenceAdapter 통합 테스트")
class NoticeTemplatePersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private NoticeTemplatePersistenceAdapter persistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_CATEGORY_ID = 1L;

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 상품고시 템플릿을 저장하고 ID를 반환한다")
        void persist_newNoticeTemplate_savesAndReturnsId() {
            // Given
            NoticeTemplate newTemplate = createNewNoticeTemplate();

            // When
            NoticeTemplateId savedId = persistenceAdapter.persist(newTemplate);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            NoticeTemplateJpaEntity found = find(NoticeTemplateJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();
            assertThat(found.getCategoryId()).isEqualTo(TEST_CATEGORY_ID);
            assertThat(found.getTemplateName()).isEqualTo("의류");
        }

        @Test
        @DisplayName("성공 - 필드가 포함된 템플릿을 저장한다")
        void persist_withFields_savesFields() {
            // Given
            NoticeTemplate template = createNoticeTemplateWithFields();

            // When
            NoticeTemplateId savedId = persistenceAdapter.persist(template);
            flushAndClear();

            // Then
            NoticeTemplateJpaEntity found = find(NoticeTemplateJpaEntity.class, savedId.value());
            assertThat(found).isNotNull();

            List<NoticeTemplateFieldJpaEntity> fields = findAllFields();
            assertThat(fields).hasSize(3);
        }

        @Test
        @DisplayName("성공 - 필수 필드와 선택 필드를 구분하여 저장한다")
        void persist_withRequiredAndOptionalFields_savesCorrectly() {
            // Given
            NoticeTemplate template = createNoticeTemplateWithMixedFields();

            // When
            NoticeTemplateId savedId = persistenceAdapter.persist(template);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();

            List<NoticeTemplateFieldJpaEntity> fields = findAllFields();
            assertThat(fields).hasSize(4);

            long requiredCount =
                    fields.stream().filter(NoticeTemplateFieldJpaEntity::isRequired).count();
            long optionalCount = fields.stream().filter(f -> !f.isRequired()).count();

            assertThat(requiredCount).isEqualTo(2);
            assertThat(optionalCount).isEqualTo(2);
        }
    }

    // ========== Helper Methods ==========

    private List<NoticeTemplateFieldJpaEntity> findAllFields() {
        return query(
                "SELECT f FROM NoticeTemplateFieldJpaEntity f", NoticeTemplateFieldJpaEntity.class);
    }

    private NoticeTemplate createNewNoticeTemplate() {
        return NoticeTemplate.forNewWithRequiredFieldsOnly(TEST_CATEGORY_ID, "의류", List.of(), NOW);
    }

    private NoticeTemplate createNoticeTemplateWithFields() {
        List<NoticeField> requiredFields =
                List.of(
                        NoticeField.required("material", "소재", 1),
                        NoticeField.required("color", "색상", 2),
                        NoticeField.required("size", "치수", 3));

        return NoticeTemplate.forNewWithRequiredFieldsOnly(
                TEST_CATEGORY_ID, "의류", requiredFields, NOW);
    }

    private NoticeTemplate createNoticeTemplateWithMixedFields() {
        List<NoticeField> requiredFields =
                List.of(
                        NoticeField.required("material", "소재", 1),
                        NoticeField.required("origin", "원산지", 2));

        List<NoticeField> optionalFields =
                List.of(
                        NoticeField.optional("washingMethod", "세탁방법", 3),
                        NoticeField.optional("asPhone", "A/S 전화번호", 4));

        return NoticeTemplate.forNew(TEST_CATEGORY_ID, "의류", requiredFields, optionalFields, NOW);
    }
}
