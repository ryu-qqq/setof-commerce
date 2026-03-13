package com.ryuqq.setof.application.banner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerQueryFixtures;
import com.ryuqq.setof.application.banner.manager.BannerSlideQueryManager;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("BannerSlideQueryService 단위 테스트")
class BannerSlideQueryServiceTest {

    @InjectMocks private BannerSlideQueryService sut;

    @Mock private BannerSlideQueryManager queryManager;

    @Nested
    @DisplayName("fetchDisplayBannerSlides() - 배너 타입별 전시 슬라이드 조회")
    class FetchDisplayBannerSlidesTest {

        @Test
        @DisplayName("유효한 배너 타입으로 전시 중인 슬라이드 목록을 반환한다")
        void fetchDisplayBannerSlides_ValidBannerType_ReturnsSlideList() {
            // given
            BannerType bannerType = BannerQueryFixtures.defaultBannerType();
            List<BannerSlide> expected = BannerQueryFixtures.activeBannerSlides();

            given(queryManager.fetchDisplayBannerSlides(bannerType)).willReturn(expected);

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryManager).should().fetchDisplayBannerSlides(bannerType);
        }

        @Test
        @DisplayName("전시 중인 슬라이드가 없으면 빈 목록을 반환한다")
        void fetchDisplayBannerSlides_NoDisplaySlides_ReturnsEmptyList() {
            // given
            BannerType bannerType = BannerQueryFixtures.defaultBannerType();

            given(queryManager.fetchDisplayBannerSlides(bannerType)).willReturn(List.of());

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isEmpty();
            then(queryManager).should().fetchDisplayBannerSlides(bannerType);
        }

        @Test
        @DisplayName("Manager에 조회를 위임하고 결과를 그대로 반환한다")
        void fetchDisplayBannerSlides_DelegatesTo_QueryManager() {
            // given
            BannerType bannerType = BannerType.CART;
            List<BannerSlide> expected = BannerQueryFixtures.activeBannerSlides();

            given(queryManager.fetchDisplayBannerSlides(bannerType)).willReturn(expected);

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryManager).should().fetchDisplayBannerSlides(bannerType);
            then(queryManager).shouldHaveNoMoreInteractions();
        }
    }
}
