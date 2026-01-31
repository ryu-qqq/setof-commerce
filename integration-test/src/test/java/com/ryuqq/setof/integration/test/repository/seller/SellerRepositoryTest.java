package com.ryuqq.setof.integration.test.repository.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
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
 * Seller JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 JPA Repository 테스트")
class SellerRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("활성 상태 셀러 저장 성공")
        void shouldSaveActiveEntity() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newEntity();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<SellerJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_SELLER_NAME);
            assertThat(found.get().isActive()).isTrue();
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newEntity();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getSellerName())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_SELLER_NAME);
            assertThat(found.getDisplayName())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(found.getLogoUrl()).isEqualTo(SellerJpaEntityFixtures.DEFAULT_LOGO_URL);
            assertThat(found.getDescription())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_DESCRIPTION);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<SellerJpaEntity> entities =
                    List.of(
                            SellerJpaEntityFixtures.activeEntityWithName("셀러1", "스토어1"),
                            SellerJpaEntityFixtures.activeEntityWithName("셀러2", "스토어2"),
                            SellerJpaEntityFixtures.activeEntityWithName("셀러3", "스토어3"));

            // when
            List<SellerJpaEntity> saved = jpaRepository.saveAll(entities);
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
        @DisplayName("ID로 셀러 조회 성공")
        void shouldFindById() {
            // given
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<SellerJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName())
                    .isEqualTo(SellerJpaEntityFixtures.DEFAULT_SELLER_NAME);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerJpaEntity> found = jpaRepository.findById(999999L);

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
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newEntity());
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
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러1", "스토어1"));
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러2", "스토어2"));
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러3", "스토어3"));
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
            SellerJpaEntity entity = jpaRepository.save(SellerJpaEntityFixtures.newEntity());
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
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러1", "스토어1"));
            jpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("셀러2", "스토어2"));
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
        @DisplayName("로고 URL 없이 저장 가능 확인")
        void shouldSaveWithoutLogoUrl() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newEntityWithoutLogoUrl();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getLogoUrl()).isNull();
        }

        @Test
        @DisplayName("설명 없이 저장 가능 확인")
        void shouldSaveWithoutDescription() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newEntityWithoutDescription();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getDescription()).isNull();
        }

        @Test
        @DisplayName("비활성 상태 저장 및 조회")
        void shouldSaveAndRetrieveInactiveEntity() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newInactiveEntity();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.isActive()).isFalse();
        }

        @Test
        @DisplayName("삭제 상태 저장 및 조회")
        void shouldSaveAndRetrieveDeletedEntity() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newDeletedEntity();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.isActive()).isFalse();
            assertThat(found.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            SellerJpaEntity entity = SellerJpaEntityFixtures.newEntity();

            // when
            SellerJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }
    }
}
