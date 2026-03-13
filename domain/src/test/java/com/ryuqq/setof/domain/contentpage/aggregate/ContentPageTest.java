package com.ryuqq.setof.domain.contentpage.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ContentPage Aggregate 단위 테스트")
class ContentPageTest {

    @Nested
    @DisplayName("forNew() - 신규 콘텐츠 페이지 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 콘텐츠 페이지를 생성한다")
        void createNewContentPageWithRequiredFields() {
            // given
            String title = "테스트 콘텐츠 페이지";
            String memo = "테스트 메모";
            String imageUrl = "https://example.com/image.jpg";
            DisplayPeriod displayPeriod = ContentPageFixtures.alwaysPeriod();
            boolean active = true;
            Instant now = Instant.now();

            // when
            ContentPage contentPage =
                    ContentPage.forNew(title, memo, imageUrl, displayPeriod, active, now);

            // then
            assertThat(contentPage.id().isNew()).isTrue();
            assertThat(contentPage.title()).isEqualTo(title);
            assertThat(contentPage.memo()).isEqualTo(memo);
            assertThat(contentPage.imageUrl()).isEqualTo(imageUrl);
            assertThat(contentPage.displayPeriod()).isEqualTo(displayPeriod);
            assertThat(contentPage.isActive()).isTrue();
            assertThat(contentPage.isDeleted()).isFalse();
            assertThat(contentPage.createdAt()).isEqualTo(now);
            assertThat(contentPage.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 상태로 신규 콘텐츠 페이지를 생성한다")
        void createNewContentPageWithInactiveStatus() {
            // given
            Instant now = Instant.now();

            // when
            ContentPage contentPage =
                    ContentPage.forNew(
                            "비활성 페이지",
                            "메모",
                            "https://example.com/img.jpg",
                            ContentPageFixtures.alwaysPeriod(),
                            false,
                            now);

            // then
            assertThat(contentPage.isActive()).isFalse();
            assertThat(contentPage.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 DeletionStatus는 active 상태이다")
        void newContentPageHasActiveDeletionStatus() {
            // given
            Instant now = Instant.now();

            // when
            ContentPage contentPage =
                    ContentPage.forNew(
                            "페이지",
                            "메모",
                            "https://example.com/img.jpg",
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            now);

            // then
            assertThat(contentPage.deletionStatus().isDeleted()).isFalse();
            assertThat(contentPage.deletionStatus().isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 콘텐츠 페이지를 복원한다")
        void reconstituteActiveContentPage() {
            // given
            ContentPageId id = ContentPageId.of(1L);
            String title = "복원된 페이지";
            Instant createdAt = Instant.now().minusSeconds(86400);
            Instant updatedAt = Instant.now().minusSeconds(86400);

            // when
            ContentPage contentPage =
                    ContentPage.reconstitute(
                            id,
                            title,
                            "메모",
                            "https://example.com/img.jpg",
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            DeletionStatus.active(),
                            createdAt,
                            updatedAt);

            // then
            assertThat(contentPage.id()).isEqualTo(id);
            assertThat(contentPage.id().isNew()).isFalse();
            assertThat(contentPage.title()).isEqualTo(title);
            assertThat(contentPage.isActive()).isTrue();
            assertThat(contentPage.isDeleted()).isFalse();
            assertThat(contentPage.createdAt()).isEqualTo(createdAt);
            assertThat(contentPage.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("영속성에서 삭제된 콘텐츠 페이지를 복원한다")
        void reconstituteDeletedContentPage() {
            // given
            ContentPageId id = ContentPageId.of(3L);
            Instant deletedAt = Instant.now().minusSeconds(86400);

            // when
            ContentPage contentPage =
                    ContentPage.reconstitute(
                            id,
                            "삭제된 페이지",
                            "메모",
                            "https://example.com/img.jpg",
                            ContentPageFixtures.alwaysPeriod(),
                            false,
                            DeletionStatus.deletedAt(deletedAt),
                            deletedAt,
                            deletedAt);

            // then
            assertThat(contentPage.isDeleted()).isTrue();
            assertThat(contentPage.deletionStatus().deletedAt()).isEqualTo(deletedAt);
        }
    }

    @Nested
    @DisplayName("update() - 콘텐츠 페이지 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("콘텐츠 페이지 정보를 수정한다")
        void updateContentPageInfo() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            ContentPageUpdateData updateData = ContentPageFixtures.contentPageUpdateData();

            // when
            contentPage.update(updateData);

            // then
            assertThat(contentPage.title()).isEqualTo(updateData.title());
            assertThat(contentPage.memo()).isEqualTo(updateData.memo());
            assertThat(contentPage.imageUrl()).isEqualTo(updateData.imageUrl());
            assertThat(contentPage.displayPeriod()).isEqualTo(updateData.displayPeriod());
            assertThat(contentPage.isActive()).isEqualTo(updateData.active());
            assertThat(contentPage.updatedAt()).isEqualTo(updateData.updatedAt());
        }
    }

    @Nested
    @DisplayName("changeDisplayStatus() - 노출 상태 변경")
    class ChangeDisplayStatusTest {

        @Test
        @DisplayName("활성 콘텐츠 페이지를 비활성화한다")
        void deactivateActiveContentPage() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            Instant now = Instant.now();

            // when
            contentPage.changeDisplayStatus(false, now);

            // then
            assertThat(contentPage.isActive()).isFalse();
            assertThat(contentPage.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 콘텐츠 페이지를 활성화한다")
        void activateInactiveContentPage() {
            // given
            ContentPage contentPage = ContentPageFixtures.inactiveContentPage();
            Instant now = Instant.now();

            // when
            contentPage.changeDisplayStatus(true, now);

            // then
            assertThat(contentPage.isActive()).isTrue();
            assertThat(contentPage.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("remove() - 소프트 삭제")
    class RemoveTest {

        @Test
        @DisplayName("활성 콘텐츠 페이지를 소프트 삭제한다")
        void removeActiveContentPage() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            Instant now = Instant.now();

            // when
            contentPage.remove(now);

            // then
            assertThat(contentPage.isDeleted()).isTrue();
            assertThat(contentPage.isActive()).isFalse();
            assertThat(contentPage.updatedAt()).isEqualTo(now);
            assertThat(contentPage.deletionStatus().deletedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제 후 active 상태는 false가 된다")
        void removeSetsActiveFalse() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            Instant now = Instant.now();

            // when
            contentPage.remove(now);

            // then
            assertThat(contentPage.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("isDisplayable() - 노출 가능 여부 판단")
    class IsDisplayableTest {

        @Test
        @DisplayName("활성 상태이고 삭제되지 않았으며 노출 기간 내이면 노출 가능하다")
        void displayableWhenActiveAndNotDeletedAndWithinPeriod() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            Instant now = Instant.now();

            // when
            boolean displayable = contentPage.isDisplayable(now);

            // then
            assertThat(displayable).isTrue();
        }

        @Test
        @DisplayName("비활성 상태이면 노출 불가이다")
        void notDisplayableWhenInactive() {
            // given
            ContentPage contentPage = ContentPageFixtures.inactiveContentPage();
            Instant now = Instant.now();

            // when
            boolean displayable = contentPage.isDisplayable(now);

            // then
            assertThat(displayable).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태이면 노출 불가이다")
        void notDisplayableWhenDeleted() {
            // given
            ContentPage contentPage = ContentPageFixtures.deletedContentPage();
            Instant now = Instant.now();

            // when
            boolean displayable = contentPage.isDisplayable(now);

            // then
            assertThat(displayable).isFalse();
        }

        @Test
        @DisplayName("노출 기간이 아니면 노출 불가이다")
        void notDisplayableWhenOutsidePeriod() {
            // given
            Instant past = Instant.parse("2000-01-01T00:00:00Z");
            Instant pastEnd = Instant.parse("2000-01-02T00:00:00Z");
            ContentPage contentPage =
                    ContentPage.reconstitute(
                            ContentPageId.of(10L),
                            "기간 외 페이지",
                            "메모",
                            "https://example.com/img.jpg",
                            DisplayPeriod.of(past, pastEnd),
                            true,
                            DeletionStatus.active(),
                            past,
                            past);
            Instant now = Instant.now();

            // when
            boolean displayable = contentPage.isDisplayable(now);

            // then
            assertThat(displayable).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 값을 반환한다")
        void idValueReturnsIdValue() {
            // given
            ContentPage contentPage = ContentPageFixtures.activeContentPage(100L);

            // when
            Long idValue = contentPage.idValue();

            // then
            assertThat(idValue).isEqualTo(100L);
        }
    }
}
