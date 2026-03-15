package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * BannerGroupCommandAdapterTest - 배너 그룹 Command Adapter 단위 테스트.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("BannerGroupCommandAdapter 단위 테스트")
class BannerGroupCommandAdapterTest {

    @Mock private BannerGroupJpaRepository bannerGroupJpaRepository;

    @Mock private BannerSlideJpaRepository bannerSlideJpaRepository;

    @Mock private BannerGroupJpaEntityMapper bannerGroupMapper;

    @Mock private BannerSlideJpaEntityMapper bannerSlideMapper;

    @InjectMocks private BannerGroupCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persist 테스트
    // ========================================================================

    @Nested
    @DisplayName("persist 메서드 테스트")
    class PersistTest {

        @Test
        @DisplayName("BannerGroup Domain을 Entity로 변환 후 저장하고 ID를 반환합니다")
        void persist_WithValidDomain_SavesAndReturnsId() {
            // given
            BannerGroup domain = BannerFixtures.newBannerGroup();
            BannerGroupJpaEntity entityToSave = BannerJpaEntityFixtures.activeGroupEntity(null);
            BannerGroupJpaEntity savedEntity = BannerJpaEntityFixtures.activeGroupEntity(100L);

            given(bannerGroupMapper.toEntity(domain)).willReturn(entityToSave);
            given(bannerGroupJpaRepository.save(entityToSave)).willReturn(savedEntity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(100L);
            then(bannerGroupMapper).should().toEntity(domain);
            then(bannerGroupJpaRepository).should().save(entityToSave);
        }

        @Test
        @DisplayName("활성 배너 그룹을 저장합니다")
        void persist_WithActiveBannerGroup_Saves() {
            // given
            BannerGroup domain = BannerFixtures.activeBannerGroup();
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity();

            given(bannerGroupMapper.toEntity(domain)).willReturn(entity);
            given(bannerGroupJpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("Mapper가 정확히 한 번 호출됩니다")
        void persist_CallsMapperOnce() {
            // given
            BannerGroup domain = BannerFixtures.newBannerGroup();
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity(null);

            given(bannerGroupMapper.toEntity(domain)).willReturn(entity);
            given(bannerGroupJpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(bannerGroupMapper).should(times(1)).toEntity(domain);
        }
    }

    // ========================================================================
    // 2. persistSlides 테스트
    // ========================================================================

    @Nested
    @DisplayName("persistSlides 메서드 테스트")
    class PersistSlidesTest {

        @Test
        @DisplayName("슬라이드 목록을 Entity로 변환 후 일괄 저장합니다")
        void persistSlides_WithMultipleSlides_SavesAll() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerSlide slide1 = BannerFixtures.activeBannerSlide(1L);
            BannerSlide slide2 = BannerFixtures.activeBannerSlide(2L);
            List<BannerSlide> slides = List.of(slide1, slide2);

            BannerSlideJpaEntity entity1 =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);
            BannerSlideJpaEntity entity2 =
                    BannerJpaEntityFixtures.activeSlideEntity(2L, bannerGroupId);

            given(bannerSlideMapper.toEntity(bannerGroupId, slide1)).willReturn(entity1);
            given(bannerSlideMapper.toEntity(bannerGroupId, slide2)).willReturn(entity2);

            // when
            commandAdapter.persistSlides(bannerGroupId, slides);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<BannerSlideJpaEntity>> captor = ArgumentCaptor.forClass(List.class);
            then(bannerSlideJpaRepository).should().saveAll(captor.capture());

            List<BannerSlideJpaEntity> savedEntities = captor.getValue();
            assertThat(savedEntities).hasSize(2);
            assertThat(savedEntities).contains(entity1, entity2);
        }

        @Test
        @DisplayName("단일 슬라이드를 저장합니다")
        void persistSlides_WithSingleSlide_SavesOne() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerSlide slide = BannerFixtures.activeBannerSlide();
            List<BannerSlide> slides = List.of(slide);
            BannerSlideJpaEntity entity =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);

            given(bannerSlideMapper.toEntity(bannerGroupId, slide)).willReturn(entity);

            // when
            commandAdapter.persistSlides(bannerGroupId, slides);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<BannerSlideJpaEntity>> captor = ArgumentCaptor.forClass(List.class);
            then(bannerSlideJpaRepository).should().saveAll(captor.capture());
            assertThat(captor.getValue()).hasSize(1);
        }

        @Test
        @DisplayName("빈 슬라이드 목록으로 호출해도 saveAll이 호출됩니다")
        void persistSlides_WithEmptyList_CallsSaveAll() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            List<BannerSlide> emptySlides = List.of();

            // when
            commandAdapter.persistSlides(bannerGroupId, emptySlides);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<BannerSlideJpaEntity>> captor = ArgumentCaptor.forClass(List.class);
            then(bannerSlideJpaRepository).should().saveAll(captor.capture());
            assertThat(captor.getValue()).isEmpty();
        }

        @Test
        @DisplayName("각 슬라이드에 대해 Mapper가 호출됩니다")
        void persistSlides_CallsMapperForEachSlide() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerSlide slide1 = BannerFixtures.activeBannerSlide(1L);
            BannerSlide slide2 = BannerFixtures.activeBannerSlide(2L);
            BannerSlide slide3 = BannerFixtures.activeBannerSlide(3L);
            List<BannerSlide> slides = List.of(slide1, slide2, slide3);

            BannerSlideJpaEntity entity = BannerJpaEntityFixtures.activeSlideEntity();
            given(bannerSlideMapper.toEntity(bannerGroupId, slide1)).willReturn(entity);
            given(bannerSlideMapper.toEntity(bannerGroupId, slide2)).willReturn(entity);
            given(bannerSlideMapper.toEntity(bannerGroupId, slide3)).willReturn(entity);

            // when
            commandAdapter.persistSlides(bannerGroupId, slides);

            // then
            then(bannerSlideMapper)
                    .should(times(3))
                    .toEntity(
                            org.mockito.ArgumentMatchers.eq(bannerGroupId),
                            org.mockito.ArgumentMatchers.any(BannerSlide.class));
        }
    }

    // ========================================================================
    // 3. updateSlides 테스트
    // ========================================================================

    @Nested
    @DisplayName("updateSlides 메서드 테스트")
    class UpdateSlidesTest {

        @Test
        @DisplayName("슬라이드 목록을 Entity로 변환 후 일괄 저장합니다")
        void updateSlides_WithMultipleSlides_SavesAll() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerSlide slide1 = BannerFixtures.activeBannerSlide(1L);
            BannerSlide slide2 = BannerFixtures.activeBannerSlide(2L);
            List<BannerSlide> slides = List.of(slide1, slide2);

            BannerSlideJpaEntity entity1 =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);
            BannerSlideJpaEntity entity2 =
                    BannerJpaEntityFixtures.activeSlideEntity(2L, bannerGroupId);

            given(bannerSlideMapper.toEntity(bannerGroupId, slide1)).willReturn(entity1);
            given(bannerSlideMapper.toEntity(bannerGroupId, slide2)).willReturn(entity2);

            // when
            commandAdapter.updateSlides(bannerGroupId, slides);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<BannerSlideJpaEntity>> captor = ArgumentCaptor.forClass(List.class);
            then(bannerSlideJpaRepository).should().saveAll(captor.capture());

            List<BannerSlideJpaEntity> savedEntities = captor.getValue();
            assertThat(savedEntities).hasSize(2);
            assertThat(savedEntities).contains(entity1, entity2);
        }

        @Test
        @DisplayName("빈 슬라이드 목록으로 업데이트해도 saveAll이 호출됩니다")
        void updateSlides_WithEmptyList_CallsSaveAll() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            List<BannerSlide> emptySlides = List.of();

            // when
            commandAdapter.updateSlides(bannerGroupId, emptySlides);

            // then
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<BannerSlideJpaEntity>> captor = ArgumentCaptor.forClass(List.class);
            then(bannerSlideJpaRepository).should().saveAll(captor.capture());
            assertThat(captor.getValue()).isEmpty();
        }

        @Test
        @DisplayName("각 슬라이드에 대해 Mapper가 올바른 bannerGroupId와 함께 호출됩니다")
        void updateSlides_CallsMapperWithCorrectBannerGroupId() {
            // given
            long bannerGroupId = 42L;
            BannerSlide slide = BannerFixtures.activeBannerSlide();
            List<BannerSlide> slides = List.of(slide);
            BannerSlideJpaEntity entity =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);

            given(bannerSlideMapper.toEntity(bannerGroupId, slide)).willReturn(entity);

            // when
            commandAdapter.updateSlides(bannerGroupId, slides);

            // then
            then(bannerSlideMapper).should().toEntity(bannerGroupId, slide);
        }
    }
}
