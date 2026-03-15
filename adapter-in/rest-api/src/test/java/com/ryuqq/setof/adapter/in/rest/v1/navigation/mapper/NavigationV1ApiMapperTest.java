package com.ryuqq.setof.adapter.in.rest.v1.navigation.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.navigation.NavigationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response.NavigationMenuV1ApiResponse;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * NavigationV1ApiMapper 단위 테스트.
 *
 * <p>네비게이션 Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("NavigationV1ApiMapper 단위 테스트")
class NavigationV1ApiMapperTest {

    private NavigationV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NavigationV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("NavigationMenu를 NavigationMenuV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            NavigationMenu menu = NavigationApiFixtures.navigationMenu(1L);

            // when
            NavigationMenuV1ApiResponse response = mapper.toResponse(menu);

            // then
            assertThat(response.gnbId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("신상품");
            assertThat(response.linkUrl()).isEqualTo("/new-arrivals");
        }

        @Test
        @DisplayName("커스텀 필드를 가진 NavigationMenu를 정확히 변환한다")
        void toResponse_WithCustomFields() {
            // given
            NavigationMenu menu = NavigationApiFixtures.navigationMenu(2L, "베스트", "/best");

            // when
            NavigationMenuV1ApiResponse response = mapper.toResponse(menu);

            // then
            assertThat(response.gnbId()).isEqualTo(2L);
            assertThat(response.title()).isEqualTo("베스트");
            assertThat(response.linkUrl()).isEqualTo("/best");
        }
    }

    @Nested
    @DisplayName("toListResponse")
    class ToListResponseTest {

        @Test
        @DisplayName("NavigationMenu 목록을 NavigationMenuV1ApiResponse 목록으로 변환한다")
        void toListResponse_Success() {
            // given
            List<NavigationMenu> menus = NavigationApiFixtures.navigationMenuList();

            // when
            List<NavigationMenuV1ApiResponse> response = mapper.toListResponse(menus);

            // then
            assertThat(response).hasSize(3);
            assertThat(response.get(0).gnbId()).isEqualTo(1L);
            assertThat(response.get(0).title()).isEqualTo("신상품");
            assertThat(response.get(0).linkUrl()).isEqualTo("/new-arrivals");
            assertThat(response.get(1).gnbId()).isEqualTo(2L);
            assertThat(response.get(1).title()).isEqualTo("베스트");
            assertThat(response.get(2).gnbId()).isEqualTo(3L);
            assertThat(response.get(2).title()).isEqualTo("세일");
        }

        @Test
        @DisplayName("빈 목록을 빈 응답으로 변환한다")
        void toListResponse_Empty() {
            // given
            List<NavigationMenu> menus = List.of();

            // when
            List<NavigationMenuV1ApiResponse> response = mapper.toListResponse(menus);

            // then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("단일 메뉴 목록을 변환한다")
        void toListResponse_SingleItem() {
            // given
            List<NavigationMenu> menus = List.of(NavigationApiFixtures.navigationMenu(1L));

            // when
            List<NavigationMenuV1ApiResponse> response = mapper.toListResponse(menus);

            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).gnbId()).isEqualTo(1L);
        }
    }
}
