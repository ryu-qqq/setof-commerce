package com.ryuqq.setof.application.shippingaddress.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressDomainFixtures;
import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressCommandPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
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
@DisplayName("ShippingAddressCommandManager 단위 테스트")
class ShippingAddressCommandManagerTest {

    @InjectMocks private ShippingAddressCommandManager sut;

    @Mock private ShippingAddressCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 배송지 저장")
    class PersistTest {

        @Test
        @DisplayName("배송지를 저장하고 생성된 ID를 반환한다")
        void persist_SavesShippingAddress_ReturnsId() {
            // given
            ShippingAddress shippingAddress =
                    ShippingAddressDomainFixtures.activeShippingAddress(100L, 1L);
            Long expectedId = 100L;

            given(commandPort.persist(shippingAddress)).willReturn(expectedId);

            // when
            Long result = sut.persist(shippingAddress);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(shippingAddress);
        }
    }

    @Nested
    @DisplayName("persistAll() - 배송지 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("배송지 목록을 각각 persist 호출로 저장한다")
        void persistAll_SavesAllShippingAddresses_CallsPersistForEach() {
            // given
            Long userId = 1L;
            List<ShippingAddress> addresses =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);

            given(commandPort.persist(addresses.get(0))).willReturn(100L);
            given(commandPort.persist(addresses.get(1))).willReturn(101L);
            given(commandPort.persist(addresses.get(2))).willReturn(102L);

            // when
            sut.persistAll(addresses);

            // then
            then(commandPort).should().persist(addresses.get(0));
            then(commandPort).should().persist(addresses.get(1));
            then(commandPort).should().persist(addresses.get(2));
        }

        @Test
        @DisplayName("빈 목록으로 호출하면 persist를 호출하지 않는다")
        void persistAll_EmptyList_DoesNotCallPersist() {
            // given
            List<ShippingAddress> emptyList = List.of();

            // when
            sut.persistAll(emptyList);

            // then
            then(commandPort).shouldHaveNoInteractions();
        }
    }
}
