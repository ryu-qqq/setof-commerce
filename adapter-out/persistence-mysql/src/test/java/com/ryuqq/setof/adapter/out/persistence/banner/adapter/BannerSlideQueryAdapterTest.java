package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideQueryDslRepository;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.setof.commerce.domain.banner.BannerFixtures;
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
 * BannerSlideQueryAdapterTest - 배너 슬라이드 Query Adapter 단위 테스트.
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
@DisplayName("BannerSlideQueryAdapter 단위 테스트")
class BannerSlideQueryAdapterTest {

    @Mock private BannerSlideQueryDslRepository queryDslRepository;

    @Mock private BannerSlideJpaEntityMapper mapper;

    @InjectMocks private BannerSlideQueryAdapter queryAdapter;

    // ========================================================================
    // 1. fetchDisplayBannerSlides 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchDisplayBannerSlides 메서드 테스트")
    class FetchDisplayBannerSlidesTest {

        @Test
        @DisplayName("배너 타입으로 전시 중인 슬라이드 목록을 반환합니다")
        void fetchDisplayBannerSlides_WithBannerType_ReturnsSlideList() {
            // given
            BannerType bannerType = BannerType.RECOMMEND;
            BannerSlideJpaEntity entity1 = BannerJpaEntityFixtures.activeSlideEntity(1L, 1L);
            BannerSlideJpaEntity entity2 = BannerJpaEntityFixtures.activeSlideEntity(2L, 1L);
            BannerSlide domain1 = BannerFixtures.activeBannerSlide(1L);
            BannerSlide domain2 = BannerFixtures.activeBannerSlide(2L);

            given(queryDslRepository.fetchDisplaySlides("RECOMMEND"))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<BannerSlide> result = queryAdapter.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository).should().fetchDisplaySlides("RECOMMEND");
        }

        @Test
        @DisplayName("전시 중인 슬라이드가 없으면 빈 리스트를 반환합니다")
        void fetchDisplayBannerSlides_WithNoResults_ReturnsEmptyList() {
            // given
            BannerType bannerType = BannerType.RECOMMEND;
            given(queryDslRepository.fetchDisplaySlides("RECOMMEND")).willReturn(List.of());

            // when
            List<BannerSlide> result = queryAdapter.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("단일 슬라이드만 있을 때 단건 목록을 반환합니다")
        void fetchDisplayBannerSlides_WithSingleSlide_ReturnsSingleElementList() {
            // given
            BannerType bannerType = BannerType.RECOMMEND;
            BannerSlideJpaEntity entity = BannerJpaEntityFixtures.activeSlideEntity();
            BannerSlide domain = BannerFixtures.activeBannerSlide();

            given(queryDslRepository.fetchDisplaySlides("RECOMMEND")).willReturn(List.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            List<BannerSlide> result = queryAdapter.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(domain);
        }
    }
}
