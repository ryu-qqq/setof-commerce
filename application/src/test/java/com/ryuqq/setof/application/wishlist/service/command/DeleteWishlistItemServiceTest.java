package com.ryuqq.setof.application.wishlist.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.wishlist.WishlistCommandFixtures;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.internal.WishlistDeletionCoordinator;
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
@DisplayName("DeleteWishlistItemService 단위 테스트")
class DeleteWishlistItemServiceTest {

    @InjectMocks private DeleteWishlistItemService sut;

    @Mock private WishlistDeletionCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 찜 항목 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 찜 항목을 삭제하고 coordinator.delete를 호출한다")
        void execute_ValidCommand_CallsCoordinatorDelete() {
            // given
            DeleteWishlistItemCommand command = WishlistCommandFixtures.deleteCommand();

            willDoNothing().given(coordinator).delete(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().delete(command);
        }

        @Test
        @DisplayName("특정 userId와 productGroupId 커맨드로 coordinator.delete를 호출한다")
        void execute_SpecificIds_CallsCoordinatorDelete() {
            // given
            Long userId = 5L;
            long productGroupId = 999L;
            DeleteWishlistItemCommand command =
                    WishlistCommandFixtures.deleteCommand(userId, productGroupId);

            willDoNothing().given(coordinator).delete(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().delete(command);
        }

        @Test
        @DisplayName("존재하지 않는 찜 항목에 대한 삭제 커맨드도 coordinator.delete를 호출한다")
        void execute_NonExistingItem_StillCallsCoordinatorDelete() {
            // given
            DeleteWishlistItemCommand command = WishlistCommandFixtures.deleteCommand(999L, 888L);

            willDoNothing().given(coordinator).delete(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().delete(command);
        }
    }
}
