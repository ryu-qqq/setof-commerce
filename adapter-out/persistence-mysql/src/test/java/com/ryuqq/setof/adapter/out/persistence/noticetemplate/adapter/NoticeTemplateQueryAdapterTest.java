package com.ryuqq.setof.adapter.out.persistence.noticetemplate.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateJpaEntity;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NoticeTemplateQueryAdapter 통합 테스트
 *
 * <p>NoticeTemplateQueryPort 구현체의 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("NoticeTemplateQueryAdapter 통합 테스트")
class NoticeTemplateQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private NoticeTemplateQueryAdapter queryAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_CATEGORY_ID = 1L;

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        @Test
        @DisplayName("성공 - ID로 템플릿을 조회한다")
        void findById_existingTemplate_returnsTemplate() {
            // Given
            NoticeTemplateJpaEntity entity = createAndPersistTemplate("의류", TEST_CATEGORY_ID);
            createAndPersistFields(entity.getId());
            flushAndClear();

            // When
            Optional<NoticeTemplate> result =
                    queryAdapter.findById(NoticeTemplateId.of(entity.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getTemplateName()).isEqualTo("의류");
            assertThat(result.get().getCategoryId()).isEqualTo(TEST_CATEGORY_ID);
        }

        @Test
        @DisplayName("성공 - 필드가 포함된 템플릿을 조회한다")
        void findById_withFields_returnsTemplateWithFields() {
            // Given
            NoticeTemplateJpaEntity entity = createAndPersistTemplate("의류", TEST_CATEGORY_ID);
            createAndPersistFields(entity.getId());
            flushAndClear();

            // When
            Optional<NoticeTemplate> result =
                    queryAdapter.findById(NoticeTemplateId.of(entity.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getRequiredFields()).hasSize(2);
            assertThat(result.get().getOptionalFields()).hasSize(1);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 Optional.empty 반환")
        void findById_nonExistingId_returnsEmptyOptional() {
            // Given
            Long nonExistingId = 9999L;

            // When
            Optional<NoticeTemplate> result =
                    queryAdapter.findById(NoticeTemplateId.of(nonExistingId));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCategoryId 메서드")
    class FindByCategoryId {

        @Test
        @DisplayName("성공 - 카테고리 ID로 템플릿을 조회한다")
        void findByCategoryId_existingCategory_returnsTemplate() {
            // Given
            NoticeTemplateJpaEntity entity = createAndPersistTemplate("의류", TEST_CATEGORY_ID);
            flushAndClear();

            // When
            Optional<NoticeTemplate> result =
                    queryAdapter.findByCategoryId(CategoryId.of(TEST_CATEGORY_ID));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getCategoryId()).isEqualTo(TEST_CATEGORY_ID);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 카테고리로 조회 시 Optional.empty 반환")
        void findByCategoryId_nonExistingCategory_returnsEmptyOptional() {
            // Given
            Long nonExistingCategoryId = 9999L;

            // When
            Optional<NoticeTemplate> result =
                    queryAdapter.findByCategoryId(CategoryId.of(nonExistingCategoryId));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllTemplates 메서드")
    class FindAllTemplates {

        @Test
        @DisplayName("성공 - 모든 템플릿을 조회한다")
        void findAllTemplates_multipleTemplates_returnsAll() {
            // Given
            createAndPersistTemplate("의류", 1L);
            createAndPersistTemplate("신발", 2L);
            createAndPersistTemplate("가방", 3L);
            flushAndClear();

            // When
            List<NoticeTemplate> result = queryAdapter.findAllTemplates();

            // Then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("성공 - 템플릿이 없으면 빈 리스트 반환")
        void findAllTemplates_noTemplates_returnsEmptyList() {
            // When
            List<NoticeTemplate> result = queryAdapter.findAllTemplates();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByCategoryId 메서드")
    class ExistsByCategoryId {

        @Test
        @DisplayName("성공 - 존재하는 카테고리면 true 반환")
        void existsByCategoryId_existingCategory_returnsTrue() {
            // Given
            createAndPersistTemplate("의류", TEST_CATEGORY_ID);
            flushAndClear();

            // When
            boolean exists = queryAdapter.existsByCategoryId(CategoryId.of(TEST_CATEGORY_ID));

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 카테고리면 false 반환")
        void existsByCategoryId_nonExistingCategory_returnsFalse() {
            // Given
            Long nonExistingCategoryId = 9999L;

            // When
            boolean exists = queryAdapter.existsByCategoryId(CategoryId.of(nonExistingCategoryId));

            // Then
            assertThat(exists).isFalse();
        }
    }

    // ========== Helper Methods ==========

    private NoticeTemplateJpaEntity createAndPersistTemplate(String name, Long categoryId) {
        NoticeTemplateJpaEntity entity =
                NoticeTemplateJpaEntity.of(null, categoryId, name, NOW, NOW);
        return persistAndFlush(entity);
    }

    private void createAndPersistFields(Long templateId) {
        // 필수 필드 2개
        persistAndFlush(
                NoticeTemplateFieldJpaEntity.of(null, templateId, "material", "소재", true, 1));
        persistAndFlush(NoticeTemplateFieldJpaEntity.of(null, templateId, "color", "색상", true, 2));
        // 선택 필드 1개
        persistAndFlush(
                NoticeTemplateFieldJpaEntity.of(
                        null, templateId, "washingMethod", "세탁방법", false, 3));
    }
}
