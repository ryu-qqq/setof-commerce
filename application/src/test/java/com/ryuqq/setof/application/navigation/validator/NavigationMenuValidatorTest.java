package com.ryuqq.setof.application.navigation.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.exception.NavigationMenuNotFoundException;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.util.Optional;
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
@DisplayName("NavigationMenuValidator 단위 테스트")
class NavigationMenuValidatorTest {

    @InjectMocks private NavigationMenuValidator sut;

    @Mock private NavigationMenuQueryPort queryPort;

    @Nested
    @DisplayName("findExistingOrThrow() - ID로 네비게이션 메뉴 조회")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 네비게이션 메뉴를 반환한다")
        void findExistingOrThrow_ReturnsNavigationMenu() {
            // given
            NavigationMenuId id = NavigationFixtures.defaultNavigationMenuId();
            NavigationMenu expected = NavigationFixtures.activeNavigationMenu();

            given(queryPort.findById(id.value())).willReturn(Optional.of(expected));

            // when
            NavigationMenu result = sut.findExistingOrThrow(id);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("존재하지 않으면 NavigationMenuNotFoundException을 발생시킨다")
        void findExistingOrThrow_NotFound_ThrowsException() {
            // given
            NavigationMenuId id = NavigationFixtures.navigationMenuId(999L);

            given(queryPort.findById(id.value())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(id))
                    .isInstanceOf(NavigationMenuNotFoundException.class);
        }
    }
}
