package com.ryuqq.setof.application.shippingaddress.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetShippingAddressesService")
@ExtendWith(MockitoExtension.class)
class GetShippingAddressesServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;

    private ShippingAddressAssembler shippingAddressAssembler;
    private GetShippingAddressesService getShippingAddressesService;

    @BeforeEach
    void setUp() {
        shippingAddressAssembler = new ShippingAddressAssembler();
        getShippingAddressesService =
                new GetShippingAddressesService(shippingAddressReadManager, shippingAddressAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원의 배송지 목록 조회 성공")
        void shouldReturnShippingAddresses() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            List<ShippingAddress> shippingAddresses = ShippingAddressFixture.createList();

            when(shippingAddressReadManager.findByMemberId(memberId)).thenReturn(shippingAddresses);

            // When
            List<ShippingAddressResponse> results = getShippingAddressesService.execute(memberId);

            // Then
            assertNotNull(results);
            assertEquals(shippingAddresses.size(), results.size());
            verify(shippingAddressReadManager, times(1)).findByMemberId(memberId);
        }

        @Test
        @DisplayName("배송지가 없을 때 빈 목록 반환")
        void shouldReturnEmptyListWhenNoAddresses() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;

            when(shippingAddressReadManager.findByMemberId(memberId)).thenReturn(List.of());

            // When
            List<ShippingAddressResponse> results = getShippingAddressesService.execute(memberId);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }

        @Test
        @DisplayName("목록에 올바른 배송지 정보 포함")
        void shouldContainCorrectInfoInList() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);

            when(shippingAddressReadManager.findByMemberId(memberId))
                    .thenReturn(List.of(shippingAddress));

            // When
            List<ShippingAddressResponse> results = getShippingAddressesService.execute(memberId);

            // Then
            assertEquals(1, results.size());
            ShippingAddressResponse response = results.get(0);
            assertEquals(shippingAddress.getIdValue(), response.id());
            assertEquals(shippingAddress.getAddressNameValue(), response.addressName());
            assertEquals(shippingAddress.getReceiverNameValue(), response.receiverName());
        }
    }
}
