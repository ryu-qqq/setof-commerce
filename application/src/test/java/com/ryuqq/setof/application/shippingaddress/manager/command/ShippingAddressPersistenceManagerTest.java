package com.ryuqq.setof.application.shippingaddress.manager.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressPersistencePort;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ShippingAddressPersistenceManager")
@ExtendWith(MockitoExtension.class)
class ShippingAddressPersistenceManagerTest {

    @Mock private ShippingAddressPersistencePort shippingAddressPersistencePort;

    private ShippingAddressPersistenceManager shippingAddressPersistenceManager;

    @BeforeEach
    void setUp() {
        shippingAddressPersistenceManager =
                new ShippingAddressPersistenceManager(shippingAddressPersistencePort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("배송지 저장 성공")
        void shouldPersistShippingAddress() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createNew();
            ShippingAddressId expectedId = ShippingAddressId.of(1L);

            when(shippingAddressPersistencePort.persist(shippingAddress)).thenReturn(expectedId);

            // When
            ShippingAddressId result = shippingAddressPersistenceManager.persist(shippingAddress);

            // Then
            assertNotNull(result);
            assertEquals(expectedId.value(), result.value());
            verify(shippingAddressPersistencePort, times(1)).persist(shippingAddress);
        }

        @Test
        @DisplayName("기존 배송지 수정 후 저장")
        void shouldPersistExistingShippingAddress() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);
            ShippingAddressId expectedId = ShippingAddressId.of(1L);

            when(shippingAddressPersistencePort.persist(shippingAddress)).thenReturn(expectedId);

            // When
            ShippingAddressId result = shippingAddressPersistenceManager.persist(shippingAddress);

            // Then
            assertEquals(1L, result.value());
            verify(shippingAddressPersistencePort, times(1)).persist(shippingAddress);
        }
    }
}
