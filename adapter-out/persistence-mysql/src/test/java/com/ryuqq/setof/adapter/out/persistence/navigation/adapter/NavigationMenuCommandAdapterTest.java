package com.ryuqq.setof.adapter.out.persistence.navigation.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.mapper.NavigationMenuJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * NavigationMenuCommandAdapterTest - 네비게이션 메뉴 Command Adapter 단위 테스트.
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
@DisplayName("NavigationMenuCommandAdapter 단위 테스트")
class NavigationMenuCommandAdapterTest {

    @Mock private NavigationMenuJpaRepository jpaRepository;

    @Mock private NavigationMenuJpaEntityMapper mapper;

    @InjectMocks private NavigationMenuCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persist 테스트
    // ========================================================================

    @Nested
    @DisplayName("persist 메서드 테스트")
    class PersistTest {

        @Test
        @DisplayName("Domain을 Entity로 변환 후 저장하고 ID를 반환합니다")
        void persist_WithValidDomain_SavesAndReturnsId() {
            // given
            NavigationMenu domain = NavigationFixtures.newNavigationMenu();
            NavigationMenuJpaEntity entityToSave = NavigationMenuJpaEntityFixtures.newEntity();
            NavigationMenuJpaEntity savedEntity =
                    NavigationMenuJpaEntityFixtures.activeEntity(100L);

            given(mapper.toEntity(domain)).willReturn(entityToSave);
            given(jpaRepository.save(entityToSave)).willReturn(savedEntity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(100L);
            then(mapper).should().toEntity(domain);
            then(jpaRepository).should().save(entityToSave);
        }

        @Test
        @DisplayName("활성 상태 네비게이션 메뉴를 저장합니다")
        void persist_WithActiveNavigationMenu_Saves() {
            // given
            NavigationMenu domain = NavigationFixtures.activeNavigationMenu();
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("비활성 상태 네비게이션 메뉴를 저장합니다")
        void persist_WithInactiveNavigationMenu_Saves() {
            // given
            NavigationMenu domain = NavigationFixtures.inactiveNavigationMenu();
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.inactiveEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("삭제된 상태 네비게이션 메뉴를 저장합니다")
        void persist_WithDeletedNavigationMenu_Saves() {
            // given
            NavigationMenu domain = NavigationFixtures.deletedNavigationMenu();
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.deletedEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("Mapper가 정확히 한 번 호출됩니다")
        void persist_CallsMapperOnce() {
            // given
            NavigationMenu domain = NavigationFixtures.newNavigationMenu();
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(mapper).should(times(1)).toEntity(domain);
        }
    }
}
