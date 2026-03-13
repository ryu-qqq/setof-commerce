package com.ryuqq.setof.domain.banner.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerSlide Entity 테스트")
class BannerSlideTest {

    @Nested
    @DisplayName("forNew() - 신규 슬라이드 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 배너 슬라이드를 생성한다")
        void createNewBannerSlide() {
            // when
            var slide = BannerFixtures.newBannerSlide();

            // then
            assertThat(slide.id().isNew()).isTrue();
            assertThat(slide.title()).isEqualTo(BannerFixtures.DEFAULT_SLIDE_TITLE);
            assertThat(slide.imageUrl()).isEqualTo(BannerFixtures.DEFAULT_IMAGE_URL);
            assertThat(slide.linkUrl()).isEqualTo(BannerFixtures.DEFAULT_LINK_URL);
            assertThat(slide.displayOrder()).isEqualTo(1);
            assertThat(slide.isActive()).isTrue();
            assertThat(slide.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("커스텀 제목과 순서로 슬라이드를 생성한다")
        void createWithCustomTitleAndOrder() {
            // when
            var slide = BannerFixtures.newBannerSlide("커스텀 슬라이드", 5);

            // then
            assertThat(slide.title()).isEqualTo("커스텀 슬라이드");
            assertThat(slide.displayOrder()).isEqualTo(5);
        }

        @Test
        @DisplayName("신규 생성 시 DeletionStatus가 active 상태이다")
        void newSlideHasActiveDeletionStatus() {
            // when
            var slide = BannerFixtures.newBannerSlide();

            // then
            assertThat(slide.deletionStatus().isDeleted()).isFalse();
            assertThat(slide.deletionStatus().isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 슬라이드를 복원한다")
        void reconstituteActiveSlide() {
            // when
            var slide = BannerFixtures.activeBannerSlide();

            // then
            assertThat(slide.id().isNew()).isFalse();
            assertThat(slide.idValue()).isEqualTo(1L);
            assertThat(slide.isActive()).isTrue();
            assertThat(slide.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태의 슬라이드를 복원한다")
        void reconstituteInactiveSlide() {
            // when
            var slide = BannerFixtures.inactiveBannerSlide();

            // then
            assertThat(slide.idValue()).isEqualTo(2L);
            assertThat(slide.isActive()).isFalse();
            assertThat(slide.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 슬라이드를 복원한다")
        void reconstituteDeletedSlide() {
            // when
            var slide = BannerFixtures.deletedBannerSlide();

            // then
            assertThat(slide.isDeleted()).isTrue();
            assertThat(slide.deletionStatus().deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 슬라이드 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("슬라이드 정보를 수정한다")
        void updateSlide() {
            // given
            var slide = BannerFixtures.activeBannerSlide();
            Instant now = CommonVoFixtures.now();

            // when
            slide.update(
                    "수정된 슬라이드",
                    "https://example.com/new-image.png",
                    "https://example.com/new-link",
                    3,
                    BannerFixtures.defaultDisplayPeriod(),
                    false,
                    now);

            // then
            assertThat(slide.title()).isEqualTo("수정된 슬라이드");
            assertThat(slide.imageUrl()).isEqualTo("https://example.com/new-image.png");
            assertThat(slide.linkUrl()).isEqualTo("https://example.com/new-link");
            assertThat(slide.displayOrder()).isEqualTo(3);
            assertThat(slide.isActive()).isFalse();
            assertThat(slide.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("remove() - 슬라이드 삭제 (Soft Delete)")
    class RemoveTest {

        @Test
        @DisplayName("슬라이드를 소프트 삭제한다")
        void removeSlide() {
            // given
            var slide = BannerFixtures.activeBannerSlide();
            Instant now = CommonVoFixtures.now();

            // when
            slide.remove(now);

            // then
            assertThat(slide.isDeleted()).isTrue();
            assertThat(slide.isActive()).isFalse();
            assertThat(slide.updatedAt()).isEqualTo(now);
            assertThat(slide.deletionStatus().deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()와 idValue()가 올바른 값을 반환한다")
        void returnsIdValues() {
            var slide = BannerFixtures.activeBannerSlide();

            assertThat(slide.id()).isNotNull();
            assertThat(slide.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("displayPeriod()가 올바른 값을 반환한다")
        void returnsDisplayPeriod() {
            var slide = BannerFixtures.activeBannerSlide();

            assertThat(slide.displayPeriod()).isNotNull();
            assertThat(slide.displayPeriod().startDate()).isNotNull();
            assertThat(slide.displayPeriod().endDate()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus()가 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var slide = BannerFixtures.activeBannerSlide();

            assertThat(slide.deletionStatus()).isNotNull();
            assertThat(slide.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()과 updatedAt()이 올바른 값을 반환한다")
        void returnsTimeValues() {
            var slide = BannerFixtures.activeBannerSlide();

            assertThat(slide.createdAt()).isNotNull();
            assertThat(slide.updatedAt()).isNotNull();
        }
    }
}
