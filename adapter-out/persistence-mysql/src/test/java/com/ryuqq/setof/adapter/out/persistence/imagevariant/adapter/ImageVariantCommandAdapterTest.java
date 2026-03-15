package com.ryuqq.setof.adapter.out.persistence.imagevariant.adapter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper.ImageVariantJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantJpaRepository;
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
 * ImageVariantCommandAdapterTest - 이미지 Variant Command Adapter 단위 테스트.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ImageVariantCommandAdapter 단위 테스트")
class ImageVariantCommandAdapterTest {

    @Mock private ImageVariantJpaRepository repository;

    @Mock private ImageVariantJpaEntityMapper mapper;

    @InjectMocks private ImageVariantCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persistAll 테스트
    // ========================================================================

    @Nested
    @DisplayName("persistAll 메서드 테스트")
    class PersistAllTest {

        @Test
        @DisplayName("Variant 목록을 일괄 저장합니다")
        void persistAll_WithValidVariants_SavesAll() {
            // given
            ImageVariant domain1 = ImageVariantFixtures.activeImageVariant(1L);
            ImageVariant domain2 = ImageVariantFixtures.activeImageVariant(2L);
            List<ImageVariant> variants = List.of(domain1, domain2);

            ImageVariantJpaEntity entity1 = ImageVariantJpaEntityFixtures.activeEntity(1L);
            ImageVariantJpaEntity entity2 = ImageVariantJpaEntityFixtures.activeEntity(2L);

            given(mapper.toEntity(domain1)).willReturn(entity1);
            given(mapper.toEntity(domain2)).willReturn(entity2);
            given(repository.saveAll(List.of(entity1, entity2)))
                    .willReturn(List.of(entity1, entity2));

            // when
            commandAdapter.persistAll(variants);

            // then
            then(mapper).should(times(1)).toEntity(domain1);
            then(mapper).should(times(1)).toEntity(domain2);
            then(repository).should().saveAll(List.of(entity1, entity2));
        }

        @Test
        @DisplayName("단일 Variant를 저장합니다")
        void persistAll_WithSingleVariant_SavesOne() {
            // given
            ImageVariant domain = ImageVariantFixtures.newImageVariant();
            List<ImageVariant> variants = List.of(domain);

            ImageVariantJpaEntity entity = ImageVariantJpaEntityFixtures.newEntity();
            given(mapper.toEntity(domain)).willReturn(entity);
            given(repository.saveAll(List.of(entity))).willReturn(List.of(entity));

            // when
            commandAdapter.persistAll(variants);

            // then
            then(mapper).should().toEntity(domain);
            then(repository).should().saveAll(List.of(entity));
        }

        @Test
        @DisplayName("빈 목록으로 저장 시 saveAll을 빈 목록으로 호출합니다")
        void persistAll_WithEmptyList_CallsSaveAllWithEmptyList() {
            // given
            List<ImageVariant> emptyList = List.of();
            given(repository.saveAll(List.of())).willReturn(List.of());

            // when
            commandAdapter.persistAll(emptyList);

            // then
            then(mapper).shouldHaveNoInteractions();
            then(repository).should().saveAll(List.of());
        }
    }

    // ========================================================================
    // 2. softDeleteBySourceImageId 테스트
    // ========================================================================

    @Nested
    @DisplayName("softDeleteBySourceImageId 메서드 테스트")
    class SoftDeleteBySourceImageIdTest {

        @Test
        @DisplayName("원본 이미지 ID로 Variant를 소프트 삭제합니다")
        void softDeleteBySourceImageId_WithValidId_CallsRepository() {
            // given
            Long sourceImageId = 100L;

            // when
            commandAdapter.softDeleteBySourceImageId(sourceImageId);

            // then
            then(repository).should().softDeleteBySourceImageId(sourceImageId);
        }

        @Test
        @DisplayName("다른 원본 이미지 ID로 소프트 삭제를 호출합니다")
        void softDeleteBySourceImageId_WithDifferentId_CallsRepositoryWithCorrectId() {
            // given
            Long sourceImageId = 200L;

            // when
            commandAdapter.softDeleteBySourceImageId(sourceImageId);

            // then
            then(repository).should().softDeleteBySourceImageId(200L);
        }
    }
}
