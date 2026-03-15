package com.ryuqq.setof.application.banner.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.banner.port.out.command.BannerGroupCommandPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
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
@DisplayName("BannerGroupCommandManager 단위 테스트")
class BannerGroupCommandManagerTest {

    @InjectMocks private BannerGroupCommandManager sut;

    @Mock private BannerGroupCommandPort bannerGroupCommandPort;

    @Nested
    @DisplayName("persist() - 배너 그룹 단독 저장")
    class PersistTest {

        @Test
        @DisplayName("배너 그룹을 저장하고 생성된 ID를 반환한다")
        void persist_ValidBannerGroup_ReturnsGroupId() {
            // given
            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup();
            Long expectedId = 1L;

            given(bannerGroupCommandPort.persist(bannerGroup)).willReturn(expectedId);

            // when
            Long result = sut.persist(bannerGroup);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(bannerGroupCommandPort).should().persist(bannerGroup);
        }
    }

    @Nested
    @DisplayName("persistWithSlides() - 배너 그룹 및 슬라이드 함께 저장")
    class PersistWithSlidesTest {

        @Test
        @DisplayName("배너 그룹과 슬라이드를 함께 저장하고 생성된 ID를 반환한다")
        void persistWithSlides_ValidBannerGroup_ReturnsGroupId() {
            // given
            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup();
            Long expectedId = 1L;

            given(bannerGroupCommandPort.persist(bannerGroup)).willReturn(expectedId);
            willDoNothing()
                    .given(bannerGroupCommandPort)
                    .persistSlides(expectedId, bannerGroup.slides());

            // when
            Long result = sut.persistWithSlides(bannerGroup);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(bannerGroupCommandPort).should().persist(bannerGroup);
            then(bannerGroupCommandPort).should().persistSlides(expectedId, bannerGroup.slides());
        }
    }

    @Nested
    @DisplayName("persistSlideDiff() - 슬라이드 Diff 영속")
    class PersistSlideDiffTest {

        @Test
        @DisplayName("추가 슬라이드가 있으면 persistSlides를 호출한다")
        void persistSlideDiff_WithAddedSlides_CallsPersistSlides() {
            // given
            long bannerGroupId = 1L;
            List<BannerSlide> added = List.of(BannerFixtures.newBannerSlide());
            BannerSlideDiff diff = BannerSlideDiff.of(added, List.of(), List.of(), Instant.now());

            willDoNothing().given(bannerGroupCommandPort).persistSlides(bannerGroupId, added);

            // when
            sut.persistSlideDiff(bannerGroupId, diff);

            // then
            then(bannerGroupCommandPort).should().persistSlides(bannerGroupId, added);
            then(bannerGroupCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("dirty 슬라이드가 있으면 updateSlides를 호출한다")
        void persistSlideDiff_WithDirtySlides_CallsUpdateSlides() {
            // given
            long bannerGroupId = 1L;
            List<BannerSlide> retained = List.of(BannerFixtures.activeBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), List.of(), retained, Instant.now());

            willDoNothing().given(bannerGroupCommandPort).updateSlides(bannerGroupId, retained);

            // when
            sut.persistSlideDiff(bannerGroupId, diff);

            // then
            then(bannerGroupCommandPort).should().updateSlides(bannerGroupId, retained);
            then(bannerGroupCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("추가 슬라이드와 dirty 슬라이드가 모두 있으면 두 메서드를 모두 호출한다")
        void persistSlideDiff_WithAddedAndDirtySlides_CallsBothMethods() {
            // given
            long bannerGroupId = 1L;
            List<BannerSlide> added = List.of(BannerFixtures.newBannerSlide());
            List<BannerSlide> retained = List.of(BannerFixtures.activeBannerSlide());
            BannerSlideDiff diff = BannerSlideDiff.of(added, List.of(), retained, Instant.now());

            willDoNothing().given(bannerGroupCommandPort).persistSlides(bannerGroupId, added);
            willDoNothing().given(bannerGroupCommandPort).updateSlides(bannerGroupId, retained);

            // when
            sut.persistSlideDiff(bannerGroupId, diff);

            // then
            then(bannerGroupCommandPort).should().persistSlides(bannerGroupId, added);
            then(bannerGroupCommandPort).should().updateSlides(bannerGroupId, retained);
        }

        @Test
        @DisplayName("변경 사항이 없으면 Port 메서드를 호출하지 않는다")
        void persistSlideDiff_WithNoChanges_CallsNoPortMethod() {
            // given
            long bannerGroupId = 1L;
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), List.of(), List.of(), Instant.now());

            // when
            sut.persistSlideDiff(bannerGroupId, diff);

            // then
            then(bannerGroupCommandPort).shouldHaveNoInteractions();
        }
    }
}
