package com.ryuqq.setof.application.wishlist.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.wishlist.WishlistCommandFixtures;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.internal.WishlistRegistrationCoordinator;
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
@DisplayName("AddWishlistItemService 단위 테스트")
class AddWishlistItemServiceTest {

    @InjectMocks private AddWishlistItemService sut;

    @Mock private WishlistRegistrationCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 찜 항목 추가")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 찜 항목을 등록하고 찜 항목 ID를 반환한다")
        void execute_ValidCommand_ReturnsWishlistItemId() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();
            Long expectedId = 10L;

            given(coordinator.register(command)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(coordinator).should().register(command);
        }

        @Test
        @DisplayName("이미 찜한 상품이라도 coordinator.register를 호출하고 기존 ID를 반환한다")
        void execute_DuplicateItem_CallsCoordinatorRegisterAndReturnsExistingId() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand(5L, 200L);
            Long existingId = 99L;

            given(coordinator.register(command)).willReturn(existingId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(existingId);
            then(coordinator).should().register(command);
        }
    }
}
