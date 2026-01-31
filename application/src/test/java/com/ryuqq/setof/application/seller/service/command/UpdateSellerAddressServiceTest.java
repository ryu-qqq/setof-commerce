package com.ryuqq.setof.application.seller.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.seller.SellerCommandFixtures;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerAddressCommand;
import com.ryuqq.setof.application.seller.factory.SellerCommandFactory;
import com.ryuqq.setof.application.seller.manager.SellerAddressCommandManager;
import com.ryuqq.setof.application.seller.validator.SellerAddressValidator;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddressUpdateData;
import com.ryuqq.setof.domain.seller.exception.AddressNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import java.time.Instant;
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
@DisplayName("UpdateSellerAddressService 단위 테스트")
class UpdateSellerAddressServiceTest {

    @InjectMocks private UpdateSellerAddressService sut;

    @Mock private SellerCommandFactory commandFactory;
    @Mock private SellerAddressCommandManager commandManager;
    @Mock private SellerAddressValidator validator;

    @Nested
    @DisplayName("execute() - 셀러 주소 수정")
    class ExecuteTest {

        @Test
        @DisplayName("존재하는 셀러 주소를 수정한다")
        void execute_ExistingAddress_UpdatesSuccessfully() {
            // given
            Long addressId = 1L;
            UpdateSellerAddressCommand command =
                    SellerCommandFixtures.updateAddressCommand(addressId);
            Instant changedAt = Instant.now();

            SellerAddressUpdateData updateData = SellerFixtures.sellerAddressUpdateData();
            UpdateContext<SellerAddressId, SellerAddressUpdateData> context =
                    new UpdateContext<>(SellerAddressId.of(addressId), updateData, changedAt);

            SellerAddress existingAddress = SellerFixtures.activeShippingAddress();

            given(commandFactory.createUpdateContext(command)).willReturn(context);
            given(validator.findExistingOrThrow(context.id())).willReturn(existingAddress);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createUpdateContext(command);
            then(validator).should().findExistingOrThrow(context.id());
            then(commandManager).should().persist(existingAddress);
        }

        @Test
        @DisplayName("존재하지 않는 주소 수정 시 예외가 발생한다")
        void execute_NonExistingAddress_ThrowsException() {
            // given
            Long addressId = 999L;
            UpdateSellerAddressCommand command =
                    SellerCommandFixtures.updateAddressCommand(addressId);
            Instant changedAt = Instant.now();

            SellerAddressUpdateData updateData = SellerFixtures.sellerAddressUpdateData();
            UpdateContext<SellerAddressId, SellerAddressUpdateData> context =
                    new UpdateContext<>(SellerAddressId.of(addressId), updateData, changedAt);

            given(commandFactory.createUpdateContext(command)).willReturn(context);
            given(validator.findExistingOrThrow(context.id()))
                    .willThrow(new AddressNotFoundException());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(AddressNotFoundException.class);

            then(commandManager).should(never()).persist(any(SellerAddress.class));
        }
    }
}
