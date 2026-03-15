package com.ryuqq.setof.integration.test.repository.navigation;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NavigationMenu JPA Repository 통합 테스트.
 *
 * <p>NavigationMenuJpaRepository의 기본 CRUD 동작을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.NAVIGATION)
@DisplayName("네비게이션 메뉴 JPA Repository 테스트")
class NavigationMenuRepositoryTest extends RepositoryTestBase {

    @Autowired private NavigationMenuJpaRepository menuRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("새 네비게이션 메뉴 저장 성공")
        void shouldSaveNewNavigationMenu() {
            // given
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.newEntity();

            // when
            NavigationMenuJpaEntity saved = menuRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(NavigationMenuJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("네비게이션 메뉴 정보가 올바르게 저장됩니다")
        void shouldSaveNavigationMenuWithCorrectInfo() {
            // given
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.newEntity();

            // when
            NavigationMenuJpaEntity saved = menuRepository.save(entity);
            flushAndClear();

            // then
            NavigationMenuJpaEntity found = find(NavigationMenuJpaEntity.class, saved.getId());
            assertThat(found.getTitle()).isEqualTo(NavigationMenuJpaEntityFixtures.DEFAULT_TITLE);
            assertThat(found.getLinkUrl())
                    .isEqualTo(NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL);
            assertThat(found.getDisplayOrder())
                    .isEqualTo(NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("비활성 네비게이션 메뉴 저장 성공")
        void shouldSaveInactiveNavigationMenu() {
            // given
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.inactiveEntity();
            // inactiveEntity has hardcoded id=2L, use null id for save
            NavigationMenuJpaEntity newInactive =
                    NavigationMenuJpaEntity.create(
                            null,
                            entity.getTitle(),
                            entity.getLinkUrl(),
                            entity.getDisplayOrder(),
                            entity.getDisplayStartAt(),
                            entity.getDisplayEndAt(),
                            false,
                            entity.getCreatedAt(),
                            entity.getUpdatedAt(),
                            null);

            // when
            NavigationMenuJpaEntity saved = menuRepository.save(newInactive);
            flushAndClear();

            // then
            NavigationMenuJpaEntity found = find(NavigationMenuJpaEntity.class, saved.getId());
            assertThat(found.isActive()).isFalse();
        }
    }
}
