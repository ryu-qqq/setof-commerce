package com.ryuqq.setof.integration.test.repository.imagevariant;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantQueryDslRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ImageVariant QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>단일/복수 원본 이미지 ID 기반 조회
 * </ul>
 */
@Tag(TestTags.IMAGE_VARIANT)
@DisplayName("이미지 Variant QueryDSL Repository 테스트")
class ImageVariantQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private ImageVariantJpaRepository jpaRepository;
    @Autowired private ImageVariantQueryDslRepository queryDslRepository;

    // ========================================================================
    // 1. findBySourceImageId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findBySourceImageId 테스트")
    class FindBySourceImageIdTest {

        @Test
        @DisplayName("단일 원본 이미지 ID로 활성 Variant 목록 조회 성공")
        void shouldFindActiveVariantsBySourceImageId() {
            // given
            Long sourceImageId = 100L;
            jpaRepository.save(
                    ImageVariantJpaEntityFixtures.newEntityWith(sourceImageId, "SMALL_WEBP"));
            jpaRepository.save(
                    ImageVariantJpaEntityFixtures.newEntityWith(sourceImageId, "MEDIUM_WEBP"));
            flushAndClear();

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(e -> e.getSourceImageId().equals(sourceImageId));
        }

        @Test
        @DisplayName("삭제된 Variant는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedVariant() {
            // given
            Long sourceImageId = ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_IMAGE_ID;
            jpaRepository.save(ImageVariantJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("활성 Variant와 삭제된 Variant가 혼재할 때 활성 Variant만 조회")
        void shouldReturnOnlyActiveVariants() {
            // given
            Long sourceImageId = 100L;
            jpaRepository.save(
                    ImageVariantJpaEntityFixtures.newEntityWith(sourceImageId, "SMALL_WEBP"));
            jpaRepository.save(ImageVariantJpaEntityFixtures.newDeletedEntity());
            flushAndClear();

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("존재하지 않는 원본 이미지 ID로 조회시 빈 목록 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            List<ImageVariantJpaEntity> result = queryDslRepository.findBySourceImageId(999999L);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("다른 원본 이미지 ID의 Variant는 조회되지 않음")
        void shouldNotReturnVariantsOfDifferentSourceImageId() {
            // given
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(100L, "SMALL_WEBP"));
            jpaRepository.save(ImageVariantJpaEntityFixtures.newEntityWith(200L, "MEDIUM_WEBP"));
            flushAndClear();

            // when
            List<ImageVariantJpaEntity> result = queryDslRepository.findBySourceImageId(100L);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSourceImageId()).isEqualTo(100L);
        }
    }

    // ========================================================================
    // 2. findBySourceImageIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findBySourceImageIds 테스트")
    class FindBySourceImageIdsTest {

        private ImageVariantJpaEntity variant100Small;
        private ImageVariantJpaEntity variant100Medium;
        private ImageVariantJpaEntity variant200Small;
        private ImageVariantJpaEntity deletedVariant;

        @BeforeEach
        void setUp() {
            variant100Small =
                    jpaRepository.save(
                            ImageVariantJpaEntityFixtures.newEntityWith(100L, "SMALL_WEBP"));
            variant100Medium =
                    jpaRepository.save(
                            ImageVariantJpaEntityFixtures.newEntityWith(100L, "MEDIUM_WEBP"));
            variant200Small =
                    jpaRepository.save(
                            ImageVariantJpaEntityFixtures.newEntityWith(200L, "SMALL_WEBP"));
            deletedVariant = jpaRepository.save(ImageVariantJpaEntityFixtures.newDeletedEntity());
            flushAndClear();
        }

        @Test
        @DisplayName("복수 원본 이미지 ID 목록으로 활성 Variant 목록 조회 성공")
        void shouldFindActiveVariantsBySourceImageIds() {
            // given
            List<Long> sourceImageIds = List.of(100L, 200L);

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(ImageVariantJpaEntity::getSourceImageId)
                    .containsExactlyInAnyOrder(100L, 100L, 200L);
        }

        @Test
        @DisplayName("삭제된 Variant는 복수 ID 조회에서 제외됨")
        void shouldExcludeDeletedVariants() {
            // given
            Long sourceImageId = ImageVariantJpaEntityFixtures.DEFAULT_SOURCE_IMAGE_ID;
            List<Long> sourceImageIds = List.of(sourceImageId);

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).noneMatch(e -> e.getId().equals(deletedVariant.getId()));
        }

        @Test
        @DisplayName("단일 원본 이미지 ID 목록으로 조회")
        void shouldFindVariantsForSingleSourceImageId() {
            // given
            List<Long> sourceImageIds = List.of(100L);

            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(e -> e.getSourceImageId().equals(100L));
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<ImageVariantJpaEntity> result = queryDslRepository.findBySourceImageIds(List.of());

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 원본 이미지 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenNotFound() {
            // when
            List<ImageVariantJpaEntity> result =
                    queryDslRepository.findBySourceImageIds(List.of(999999L, 888888L));

            // then
            assertThat(result).isEmpty();
        }
    }
}
