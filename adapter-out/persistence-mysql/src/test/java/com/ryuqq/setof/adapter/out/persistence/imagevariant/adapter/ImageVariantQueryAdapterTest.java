package com.ryuqq.setof.adapter.out.persistence.imagevariant.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper.ImageVariantJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantQueryDslRepository;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ImageVariantQueryAdapterTest - 이미지 Variant Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ImageVariantQueryAdapter 단위 테스트")
class ImageVariantQueryAdapterTest {

    @Mock private ImageVariantQueryDslRepository queryDslRepository;

    @Mock private ImageVariantJpaEntityMapper mapper;

    @InjectMocks private ImageVariantQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findBySourceImageId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findBySourceImageId 메서드 테스트")
    class FindBySourceImageIdTest {

        @Test
        @DisplayName("단일 원본 이미지 ID로 Variant 목록을 반환합니다")
        void findBySourceImageId_WithValidId_ReturnsDomainList() {
            // given
            Long sourceImageId = 100L;
            ImageVariantJpaEntity entity1 = ImageVariantJpaEntityFixtures.activeEntity(1L);
            ImageVariantJpaEntity entity2 = ImageVariantJpaEntityFixtures.activeEntity(2L);
            ImageVariant domain1 = ImageVariantFixtures.activeImageVariant(1L);
            ImageVariant domain2 = ImageVariantFixtures.activeImageVariant(2L);

            given(queryDslRepository.findBySourceImageId(sourceImageId))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository).should().findBySourceImageId(sourceImageId);
        }

        @Test
        @DisplayName("Variant가 없으면 빈 리스트를 반환합니다")
        void findBySourceImageId_WithNoResults_ReturnsEmptyList() {
            // given
            Long sourceImageId = 999L;
            given(queryDslRepository.findBySourceImageId(sourceImageId)).willReturn(List.of());

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findBySourceImageId(sourceImageId);
        }

        @Test
        @DisplayName("단일 Variant가 있으면 단일 Domain을 반환합니다")
        void findBySourceImageId_WithSingleResult_ReturnsSingleDomain() {
            // given
            Long sourceImageId = 100L;
            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.activeEntity();
            ImageVariant domain = ImageVariantFixtures.activeImageVariant();

            given(queryDslRepository.findBySourceImageId(sourceImageId))
                    .willReturn(List.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(domain);
        }
    }

    // ========================================================================
    // 2. findBySourceImageIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findBySourceImageIds 메서드 테스트")
    class FindBySourceImageIdsTest {

        @Test
        @DisplayName("복수 원본 이미지 ID 목록으로 Variant 목록을 반환합니다")
        void findBySourceImageIds_WithValidIds_ReturnsDomainList() {
            // given
            List<Long> sourceImageIds = List.of(100L, 200L);
            ImageVariantJpaEntity entity1 = ImageVariantJpaEntityFixtures.activeEntity(1L);
            ImageVariantJpaEntity entity2 = ImageVariantJpaEntityFixtures.activeEntity(2L);
            ImageVariant domain1 = ImageVariantFixtures.activeImageVariant(1L);
            ImageVariant domain2 = ImageVariantFixtures.activeImageVariant(2L);

            given(queryDslRepository.findBySourceImageIds(sourceImageIds))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository).should().findBySourceImageIds(sourceImageIds);
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 리스트를 반환합니다")
        void findBySourceImageIds_WithEmptyList_ReturnsEmptyList() {
            // given
            List<Long> emptyList = List.of();
            given(queryDslRepository.findBySourceImageIds(emptyList)).willReturn(List.of());

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageIds(emptyList);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findBySourceImageIds(emptyList);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 리스트를 반환합니다")
        void findBySourceImageIds_WithNoResults_ReturnsEmptyList() {
            // given
            List<Long> sourceImageIds = List.of(999L, 998L);
            given(queryDslRepository.findBySourceImageIds(sourceImageIds)).willReturn(List.of());

            // when
            List<ImageVariant> result = queryAdapter.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).isEmpty();
        }
    }
}
