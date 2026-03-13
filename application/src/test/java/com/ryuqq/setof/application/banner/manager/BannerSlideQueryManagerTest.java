package com.ryuqq.setof.application.banner.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerQueryFixtures;
import com.ryuqq.setof.application.banner.port.out.BannerSlideQueryPort;
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
@DisplayName("BannerSlideQueryManager 단위 테스트")
class BannerSlideQueryManagerTest {

    @InjectMocks private BannerSlideQueryManager sut;

    @Mock private BannerSlideQueryPort queryPort;

    @Nested
    @DisplayName("fetchDisplayBannerSlides() - 배너 타입별 슬라이드 조회")
    class FetchDisplayBannerSlidesTest {

        @Test
        @DisplayName("배너 타입으로 전시 중인 슬라이드 목록을 조회하여 반환한다")
        void fetchDisplayBannerSlides_ValidBannerType_ReturnsSlideList() {
            // given
            BannerType bannerType = BannerQueryFixtures.defaultBannerType();
            List<BannerSlide> expected = BannerQueryFixtures.activeBannerSlides();

            given(queryPort.fetchDisplayBannerSlides(bannerType)).willReturn(expected);

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchDisplayBannerSlides(bannerType);
        }

        @Test
        @DisplayName("전시 중인 슬라이드가 없으면 빈 목록을 반환한다")
        void fetchDisplayBannerSlides_NoDisplaySlides_ReturnsEmptyList() {
            // given
            BannerType bannerType = BannerQueryFixtures.defaultBannerType();

            given(queryPort.fetchDisplayBannerSlides(bannerType)).willReturn(List.of());

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchDisplayBannerSlides(bannerType);
        }

        @Test
        @DisplayName("CATEGORY 타입으로 슬라이드를 조회할 수 있다")
        void fetchDisplayBannerSlides_CategoryType_ReturnsSlideList() {
            // given
            BannerType bannerType = BannerType.CATEGORY;
            List<BannerSlide> expected = BannerQueryFixtures.activeBannerSlides();

            given(queryPort.fetchDisplayBannerSlides(bannerType)).willReturn(expected);

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchDisplayBannerSlides(bannerType);
        }

        @Test
        @DisplayName("LOGIN 타입으로 조회 시 매칭 슬라이드가 없으면 빈 목록을 반환한다")
        void fetchDisplayBannerSlides_LoginType_ReturnsEmptyList() {
            // given
            BannerType bannerType = BannerType.LOGIN;

            given(queryPort.fetchDisplayBannerSlides(bannerType)).willReturn(List.of());

            // when
            List<BannerSlide> result = sut.fetchDisplayBannerSlides(bannerType);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchDisplayBannerSlides(bannerType);
        }
    }
}
