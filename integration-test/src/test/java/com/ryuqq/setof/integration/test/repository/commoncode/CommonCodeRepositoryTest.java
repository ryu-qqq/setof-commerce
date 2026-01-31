package com.ryuqq.setof.integration.test.repository.commoncode;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.commoncode.CommonCodeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncode.entity.CommonCodeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncode.repository.CommonCodeJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.CommonCodeTypeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.CommonCodeTypeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CommonCode JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통 코드 JPA Repository 테스트")
class CommonCodeRepositoryTest extends RepositoryTestBase {

    @Autowired private CommonCodeJpaRepository jpaRepository;
    @Autowired private CommonCodeTypeJpaRepository typeJpaRepository;

    private Long savedTypeId;

    @BeforeEach
    void setUp() {
        // 공통 코드가 참조할 타입 생성
        CommonCodeTypeJpaEntity savedType =
                typeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
        savedTypeId = savedType.getId();
        flushAndClear();
    }

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("활성 상태 공통 코드 저장 성공")
        void shouldSaveActiveEntity() {
            // given
            CommonCodeJpaEntity entity =
                    CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId);

            // when
            CommonCodeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<CommonCodeJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getCode()).isEqualTo(CommonCodeJpaEntityFixtures.DEFAULT_CODE);
            assertThat(found.get().isActive()).isTrue();
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            CommonCodeJpaEntity entity =
                    CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId);

            // when
            CommonCodeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getCommonCodeTypeId()).isEqualTo(savedTypeId);
            assertThat(found.getCode()).isEqualTo(CommonCodeJpaEntityFixtures.DEFAULT_CODE);
            assertThat(found.getDisplayName())
                    .isEqualTo(CommonCodeJpaEntityFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(found.getDisplayOrder())
                    .isEqualTo(CommonCodeJpaEntityFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<CommonCodeJpaEntity> entities =
                    List.of(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    savedTypeId, "CODE_1", "코드1"),
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    savedTypeId, "CODE_2", "코드2"),
                            CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                    savedTypeId, "CODE_3", "코드3"));

            // when
            List<CommonCodeJpaEntity> saved = jpaRepository.saveAll(entities);
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
        @DisplayName("ID로 공통 코드 조회 성공")
        void shouldFindById() {
            // given
            CommonCodeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
            flushAndClear();

            // when
            Optional<CommonCodeJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCode()).isEqualTo(CommonCodeJpaEntityFixtures.DEFAULT_CODE);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<CommonCodeJpaEntity> found = jpaRepository.findById(999999L);

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
            CommonCodeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
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
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_1", "코드1"));
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_2", "코드2"));
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_3", "코드3"));
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
            CommonCodeJpaEntity entity =
                    jpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
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
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_1", "코드1"));
            jpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_2", "코드2"));
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
        @DisplayName("비활성 상태 저장 및 조회")
        void shouldSaveAndRetrieveInactiveEntity() {
            // given
            CommonCodeJpaEntity entity = CommonCodeJpaEntityFixtures.newInactiveEntity();

            // when
            CommonCodeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.isActive()).isFalse();
        }

        @Test
        @DisplayName("다양한 표시 순서 저장 가능 확인")
        void shouldSaveVariousDisplayOrders() {
            // given
            CommonCodeJpaEntity entity1 = CommonCodeJpaEntityFixtures.newEntityWithDisplayOrder(1);
            CommonCodeJpaEntity entity2 =
                    CommonCodeJpaEntityFixtures.newEntityWithDisplayOrder(100);
            CommonCodeJpaEntity entity3 =
                    CommonCodeJpaEntityFixtures.newEntityWithDisplayOrder(999);

            // when
            jpaRepository.saveAll(List.of(entity1, entity2, entity3));
            flushAndClear();

            // then
            assertThat(jpaRepository.count()).isEqualTo(3);
        }

        @Test
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            CommonCodeJpaEntity entity =
                    CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId);

            // when
            CommonCodeJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            CommonCodeJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }
    }
}
