package com.ryuqq.setof.integration.test.repository.commoncodetype;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.commoncodetype.CommonCodeTypeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.CommonCodeTypeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CommonCodeType JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통 코드 타입 JPA Repository 테스트")
class CommonCodeTypeRepositoryTest extends RepositoryTestBase {

    @Autowired private CommonCodeTypeJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("활성 상태 공통 코드 타입 저장 성공")
        void shouldSaveActiveEntity() {
            // given
            CommonCodeTypeJpaEntity entity = CommonCodeTypeJpaEntityFixtures.newEntity();

            // when
            CommonCodeTypeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<CommonCodeTypeJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getCode())
                    .isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_CODE);
            assertThat(found.get().isActive()).isTrue();
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            CommonCodeTypeJpaEntity entity = CommonCodeTypeJpaEntityFixtures.newEntity();

            // when
            CommonCodeTypeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeTypeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getCode()).isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_CODE);
            assertThat(found.getName()).isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_NAME);
            assertThat(found.getDescription())
                    .isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_DESCRIPTION);
            assertThat(found.getDisplayOrder())
                    .isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<CommonCodeTypeJpaEntity> entities =
                    List.of(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_1", "타입1"),
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_2", "타입2"),
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_3", "타입3"));

            // when
            List<CommonCodeTypeJpaEntity> saved = jpaRepository.saveAll(entities);
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(jpaRepository.count()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 공통 코드 타입 조회 성공")
        void shouldFindById() {
            // given
            CommonCodeTypeJpaEntity entity =
                    jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<CommonCodeTypeJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCode())
                    .isEqualTo(CommonCodeTypeJpaEntityFixtures.DEFAULT_CODE);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<CommonCodeTypeJpaEntity> found = jpaRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID는 true 반환")
        void shouldReturnTrueWhenExists() {
            // given
            CommonCodeTypeJpaEntity entity =
                    jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            boolean exists = jpaRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID는 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // when
            boolean exists = jpaRepository.existsById(999999L);

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("count 테스트")
    class CountTest {

        @Test
        @DisplayName("전체 개수 카운트")
        void shouldCountAll() {
            // given
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_1", "타입1"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_2", "타입2"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_3", "타입3"));
            flushAndClear();

            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("빈 테이블 카운트")
        void shouldReturnZeroWhenEmpty() {
            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("delete 테스트")
    class DeleteTest {

        @Test
        @DisplayName("엔티티 삭제 성공")
        void shouldDeleteEntity() {
            // given
            CommonCodeTypeJpaEntity entity =
                    jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            Long id = entity.getId();
            flushAndClear();

            // when
            jpaRepository.deleteById(id);
            flushAndClear();

            // then
            assertThat(jpaRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("전체 삭제 성공")
        void shouldDeleteAll() {
            // given
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_1", "타입1"));
            jpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_2", "타입2"));
            flushAndClear();

            // when
            jpaRepository.deleteAll();
            flushAndClear();

            // then
            assertThat(jpaRepository.count()).isZero();
        }
    }

    @Nested
    @DisplayName("엔티티 필드 검증 테스트")
    class EntityFieldValidationTest {

        @Test
        @DisplayName("설명 없이 저장 가능 확인")
        void shouldSaveWithoutDescription() {
            // given
            CommonCodeTypeJpaEntity entity =
                    CommonCodeTypeJpaEntityFixtures.newEntityWithoutDescription();

            // when
            CommonCodeTypeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeTypeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getDescription()).isNull();
        }

        @Test
        @DisplayName("비활성 상태 저장 및 조회")
        void shouldSaveAndRetrieveInactiveEntity() {
            // given
            CommonCodeTypeJpaEntity entity = CommonCodeTypeJpaEntityFixtures.newInactiveEntity();

            // when
            CommonCodeTypeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeTypeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.isActive()).isFalse();
        }

        @Test
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            CommonCodeTypeJpaEntity entity = CommonCodeTypeJpaEntityFixtures.newEntity();

            // when
            CommonCodeTypeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeTypeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }
    }
}
