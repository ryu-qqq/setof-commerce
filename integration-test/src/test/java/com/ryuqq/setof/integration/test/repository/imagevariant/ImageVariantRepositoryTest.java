package com.ryuqq.setof.integration.test.repository.imagevariant;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantJpaRepository;
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
 * ImageVariant JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.IMAGE_VARIANT)
@DisplayName("이미지 Variant JPA Repository 테스트")
class ImageVariantRepositoryTest extends RepositoryTestBase {

    @Autowired private ImageVariantJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("활성 상태 Variant 저장 성공")
        void shouldSaveActiveEntity() {
            // given
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.newEntity();

            // when
            ImageVariantJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<ImageVariantJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getSourceImageId())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_IMAGE_ID);
            assertThat(found.get().getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.newEntity();

            // when
            ImageVariantJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            ImageVariantJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getSourceImageId())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_IMAGE_ID);
            assertThat(found.getSourceType())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_TYPE);
            assertThat(found.getVariantType())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_VARIANT_TYPE);
            assertThat(found.getResultAssetId())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_RESULT_ASSET_ID);
            assertThat(found.getVariantUrl())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_VARIANT_URL);
            assertThat(found.getWidth()).isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_WIDTH);
            assertThat(found.getHeight()).isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_HEIGHT);
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("여러 Variant 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<ImageVariantJpaEntity> entities =
                    List.of(
                            ImageVariantJpaEntityFixtures.newEntityWith(100L, "SMALL_WEBP"),
                            ImageVariantJpaEntityFixtures.newEntityWith(100L, "MEDIUM_WEBP"),
                            ImageVariantJpaEntityFixtures.newEntityWith(100L, "LARGE_WEBP"));

            // when
            List<ImageVariantJpaEntity> saved = jpaRepository.saveAll(entities);
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(jpaRepository.count()).isEqualTo(3);
        }

        @Test
        @DisplayName("width/height가 null인 Variant 저장 성공")
        void shouldSaveEntityWithNullDimension() {
            // given
            ImageVariantJpaEntity entity =
                    ImageVariantJpaEntityFixtures.newEntityWithNullDimension();

            // when
            ImageVariantJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            ImageVariantJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getWidth()).isNull();
            assertThat(found.getHeight()).isNull();
        }
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 Variant 조회 성공")
        void shouldFindById() {
            // given
            ImageVariantJpaEntity entity =
                    jpaRepository.save(ImageVariantJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<ImageVariantJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSourceImageId())
                    .isEqualTo(ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_IMAGE_ID);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<ImageVariantJpaEntity> found = jpaRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("softDeleteBySourceImageId 테스트")
    class SoftDeleteBySourceImageIdTest {

        @Test
        @DisplayName("원본 이미지 ID로 소프트 삭제 성공")
        void shouldSoftDeleteBySourceImageId() {
            // given
            Long sourceImageId = 100L;
            ImageVariantJpaEntity entity1 =
                    jpaRepository.save(
                            ImageVariantJpaEntityFixtures.activeEntityWithSourceImageId(
                                    sourceImageId));
            ImageVariantJpaEntity entity2 =
                    jpaRepository.save(
                            ImageVariantJpaEntityFixtures.activeEntityWithVariantType(
                                    "SMALL_WEBP", sourceImageId));
            flushAndClear();

            // when
            jpaRepository.softDeleteBySourceImageId(sourceImageId);
            flushAndClear();

            // then
            ImageVariantJpaEntity found1 = jpaRepository.findById(entity1.getId()).orElseThrow();
            ImageVariantJpaEntity found2 = jpaRepository.findById(entity2.getId()).orElseThrow();
            assertThat(found1.getDeletedAt()).isNotNull();
            assertThat(found2.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("이미 삭제된 Variant는 재삭제되지 않음")
        void shouldNotAffectAlreadyDeletedEntities() {
            // given
            Long sourceImageId = 200L;
            ImageVariantJpaEntity deletedEntity =
                    jpaRepository.save(ImageVariantJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            jpaRepository.softDeleteBySourceImageId(sourceImageId);
            flushAndClear();

            // then (다른 sourceImageId라 영향 없음)
            ImageVariantJpaEntity found =
                    jpaRepository.findById(deletedEntity.getId()).orElseThrow();
            assertThat(found.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("count 테스트")
    class CountTest {

        @Test
        @DisplayName("전체 개수 카운트")
        void shouldCountAll() {
            // given
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "SMALL_WEBP"));
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "MEDIUM_WEBP"));
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "LARGE_WEBP"));
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
            ImageVariantJpaEntity entity =
                    jpaRepository.save(ImageVariantJpaEntityFixtures.newEntity());
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
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "SMALL_WEBP"));
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "MEDIUM_WEBP"));
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
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.newEntity();

            // when
            ImageVariantJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            ImageVariantJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("삭제 상태 저장 및 조회")
        void shouldSaveAndRetrieveDeletedEntity() {
            // given
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.newDeletedEntity();

            // when
            ImageVariantJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            ImageVariantJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getDeletedAt()).isNotNull();
        }
    }
}
