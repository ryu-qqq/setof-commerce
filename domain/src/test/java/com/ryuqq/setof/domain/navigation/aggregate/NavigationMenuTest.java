package com.ryuqq.setof.domain.navigation.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NavigationMenu Aggregate 테스트")
class NavigationMenuTest {

    @Nested
    @DisplayName("forNew() - 신규 네비게이션 메뉴 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 네비게이션 메뉴를 생성한다")
        void createNewNavigationMenu() {
            // when
            var menu = NavigationFixtures.newNavigationMenu();

            // then
            assertThat(menu.id().isNew()).isTrue();
            assertThat(menu.title()).isEqualTo(NavigationFixtures.DEFAULT_TITLE);
            assertThat(menu.linkUrl()).isEqualTo(NavigationFixtures.DEFAULT_LINK_URL);
            assertThat(menu.displayOrder()).isEqualTo(NavigationFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(menu.isActive()).isTrue();
            assertThat(menu.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("커스텀 값으로 네비게이션 메뉴를 생성한다")
        void createWithCustomValues() {
            // when
            var menu = NavigationFixtures.newNavigationMenu("남성", "https://example.com/men");

            // then
            assertThat(menu.title()).isEqualTo("남성");
            assertThat(menu.linkUrl()).isEqualTo("https://example.com/men");
        }

        @Test
        @DisplayName("신규 생성 시 DeletionStatus가 active 상태이다")
        void newNavigationMenuHasActiveDeletionStatus() {
            // when
            var menu = NavigationFixtures.newNavigationMenu();

            // then
            assertThat(menu.deletionStatus().isDeleted()).isFalse();
            assertThat(menu.deletionStatus().isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 네비게이션 메뉴를 복원한다")
        void reconstituteActiveNavigationMenu() {
            // when
            var menu = NavigationFixtures.activeNavigationMenu();

            // then
            assertThat(menu.id().isNew()).isFalse();
            assertThat(menu.idValue()).isEqualTo(1L);
            assertThat(menu.isActive()).isTrue();
            assertThat(menu.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태의 네비게이션 메뉴를 복원한다")
        void reconstituteInactiveNavigationMenu() {
            // when
            var menu = NavigationFixtures.inactiveNavigationMenu();

            // then
            assertThat(menu.idValue()).isEqualTo(2L);
            assertThat(menu.isActive()).isFalse();
            assertThat(menu.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 네비게이션 메뉴를 복원한다")
        void reconstituteDeletedNavigationMenu() {
            // when
            var menu = NavigationFixtures.deletedNavigationMenu();

            // then
            assertThat(menu.isDeleted()).isTrue();
            assertThat(menu.deletionStatus().deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 네비게이션 메뉴 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("네비게이션 메뉴 정보를 수정한다")
        void updateNavigationMenu() {
            // given
            var menu = NavigationFixtures.activeNavigationMenu();
            var updateData = NavigationFixtures.defaultNavigationMenuUpdateData();

            // when
            menu.update(updateData);

            // then
            assertThat(menu.title()).isEqualTo("남성");
            assertThat(menu.linkUrl()).isEqualTo("https://example.com/men");
            assertThat(menu.displayOrder()).isEqualTo(2);
            assertThat(menu.isActive()).isFalse();
        }

        @Test
        @DisplayName("수정 시 updatedAt이 갱신된다")
        void updateRefreshesUpdatedAt() {
            // given
            var menu = NavigationFixtures.activeNavigationMenu();
            Instant beforeUpdate = menu.updatedAt();
            var updateData = NavigationFixtures.defaultNavigationMenuUpdateData();

            // when
            menu.update(updateData);

            // then
            assertThat(menu.updatedAt()).isNotEqualTo(beforeUpdate);
            assertThat(menu.updatedAt()).isEqualTo(updateData.updatedAt());
        }

        @Test
        @DisplayName("커스텀 값으로 수정한다")
        void updateWithCustomValues() {
            // given
            var menu = NavigationFixtures.activeNavigationMenu();
            var updateData =
                    NavigationFixtures.navigationMenuUpdateData(
                            "기획전", "https://example.com/event", 5, true);

            // when
            menu.update(updateData);

            // then
            assertThat(menu.title()).isEqualTo("기획전");
            assertThat(menu.linkUrl()).isEqualTo("https://example.com/event");
            assertThat(menu.displayOrder()).isEqualTo(5);
            assertThat(menu.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("remove() - 네비게이션 메뉴 삭제 (Soft Delete)")
    class RemoveTest {

        @Test
        @DisplayName("네비게이션 메뉴를 소프트 삭제한다")
        void removeNavigationMenu() {
            // given
            var menu = NavigationFixtures.activeNavigationMenu();
            Instant now = CommonVoFixtures.now();

            // when
            menu.remove(now);

            // then
            assertThat(menu.isDeleted()).isTrue();
            assertThat(menu.isActive()).isFalse();
            assertThat(menu.updatedAt()).isEqualTo(now);
            assertThat(menu.deletionStatus().deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isDisplayable() - 노출 가능 여부 판단")
    class IsDisplayableTest {

        @Test
        @DisplayName("활성 상태이고 기간 내이면 노출 가능하다")
        void displayableWhenActiveAndWithinPeriod() {
            // given
            var menu = NavigationFixtures.activeNavigationMenu();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(menu.isDisplayable(now)).isTrue();
        }

        @Test
        @DisplayName("비활성 상태이면 노출 불가하다")
        void notDisplayableWhenInactive() {
            // given
            var menu = NavigationFixtures.inactiveNavigationMenu();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(menu.isDisplayable(now)).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태이면 노출 불가하다")
        void notDisplayableWhenDeleted() {
            // given
            var menu = NavigationFixtures.deletedNavigationMenu();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(menu.isDisplayable(now)).isFalse();
        }

        @Test
        @DisplayName("노출 기간이 만료되면 노출 불가하다")
        void notDisplayableWhenPeriodExpired() {
            // given
            var menu = NavigationFixtures.expiredNavigationMenu();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(menu.isDisplayable(now)).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()와 idValue()가 올바른 값을 반환한다")
        void returnsIdValues() {
            var menu = NavigationFixtures.activeNavigationMenu();

            assertThat(menu.id()).isNotNull();
            assertThat(menu.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("displayPeriod()가 올바른 값을 반환한다")
        void returnsDisplayPeriod() {
            var menu = NavigationFixtures.activeNavigationMenu();

            assertThat(menu.displayPeriod()).isNotNull();
            assertThat(menu.displayPeriod().startDate()).isNotNull();
            assertThat(menu.displayPeriod().endDate()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus()가 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var menu = NavigationFixtures.activeNavigationMenu();

            assertThat(menu.deletionStatus()).isNotNull();
            assertThat(menu.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()과 updatedAt()이 올바른 값을 반환한다")
        void returnsTimeValues() {
            var menu = NavigationFixtures.activeNavigationMenu();

            assertThat(menu.createdAt()).isNotNull();
            assertThat(menu.updatedAt()).isNotNull();
        }
    }
}
