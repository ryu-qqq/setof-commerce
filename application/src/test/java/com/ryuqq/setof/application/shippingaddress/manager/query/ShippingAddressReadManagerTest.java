package com.ryuqq.setof.application.shippingaddress.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.port.out.query.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ShippingAddressReadManager")
@ExtendWith(MockitoExtension.class)
class ShippingAddressReadManagerTest {

    @Mock private ShippingAddressQueryPort shippingAddressQueryPort;

    private ShippingAddressReadManager shippingAddressReadManager;

    @BeforeEach
    void setUp() {
        shippingAddressReadManager = new ShippingAddressReadManager(shippingAddressQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 배송지 조회 성공")
        void shouldReturnShippingAddress() {
            // Given
            Long shippingAddressId = 1L;
            ShippingAddress shippingAddress =
                    ShippingAddressFixture.createWithId(shippingAddressId);
            ShippingAddressId id = ShippingAddressId.of(shippingAddressId);

            when(shippingAddressQueryPort.findById(any())).thenReturn(Optional.of(shippingAddress));

            // When
            ShippingAddress result = shippingAddressReadManager.findById(shippingAddressId);

            // Then
            assertNotNull(result);
            assertEquals(shippingAddressId, result.getIdValue());
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 예외 발생")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            Long shippingAddressId = 999L;

            when(shippingAddressQueryPort.findById(any())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    ShippingAddressNotFoundException.class,
                    () -> shippingAddressReadManager.findById(shippingAddressId));
        }
    }

    @Nested
    @DisplayName("findByIdOptional")
    class FindByIdOptionalTest {

        @Test
        @DisplayName("ID로 배송지 조회 (Optional)")
        void shouldReturnOptionalShippingAddress() {
            // Given
            Long shippingAddressId = 1L;
            ShippingAddress shippingAddress =
                    ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressQueryPort.findById(any())).thenReturn(Optional.of(shippingAddress));

            // When
            Optional<ShippingAddress> result =
                    shippingAddressReadManager.findByIdOptional(shippingAddressId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(shippingAddressId, result.get().getIdValue());
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            Long shippingAddressId = 999L;

            when(shippingAddressQueryPort.findById(any())).thenReturn(Optional.empty());

            // When
            Optional<ShippingAddress> result =
                    shippingAddressReadManager.findByIdOptional(shippingAddressId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByMemberId")
    class FindByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 배송지 목록 조회")
        void shouldReturnShippingAddressList() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            List<ShippingAddress> addresses = ShippingAddressFixture.createList();

            when(shippingAddressQueryPort.findAllByMemberId(memberId)).thenReturn(addresses);

            // When
            List<ShippingAddress> results = shippingAddressReadManager.findByMemberId(memberId);

            // Then
            assertEquals(addresses.size(), results.size());
            verify(shippingAddressQueryPort, times(1)).findAllByMemberId(memberId);
        }

        @Test
        @DisplayName("배송지가 없을 때 빈 목록 반환")
        void shouldReturnEmptyListWhenNoAddresses() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;

            when(shippingAddressQueryPort.findAllByMemberId(memberId)).thenReturn(List.of());

            // When
            List<ShippingAddress> results = shippingAddressReadManager.findByMemberId(memberId);

            // Then
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("findDefaultByMemberId")
    class FindDefaultByMemberIdTest {

        @Test
        @DisplayName("기본 배송지 조회 성공")
        void shouldReturnDefaultAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            ShippingAddress defaultAddress = ShippingAddressFixture.createWithId(1L);

            when(shippingAddressQueryPort.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.of(defaultAddress));

            // When
            Optional<ShippingAddress> result =
                    shippingAddressReadManager.findDefaultByMemberId(memberId);

            // Then
            assertTrue(result.isPresent());
            assertTrue(result.get().isDefault());
        }

        @Test
        @DisplayName("기본 배송지가 없을 때 빈 Optional 반환")
        void shouldReturnEmptyWhenNoDefaultAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;

            when(shippingAddressQueryPort.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.empty());

            // When
            Optional<ShippingAddress> result =
                    shippingAddressReadManager.findDefaultByMemberId(memberId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("countByMemberId")
    class CountByMemberIdTest {

        @Test
        @DisplayName("회원의 배송지 개수 조회")
        void shouldReturnCount() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;

            when(shippingAddressQueryPort.countByMemberId(memberId)).thenReturn(3L);

            // When
            long count = shippingAddressReadManager.countByMemberId(memberId);

            // Then
            assertEquals(3L, count);
        }
    }

    @Nested
    @DisplayName("findLatestExcluding")
    class FindLatestExcludingTest {

        @Test
        @DisplayName("특정 ID 제외하고 최근 배송지 조회")
        void shouldReturnLatestExcludingId() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long excludeId = 1L;
            ShippingAddress latestAddress = ShippingAddressFixture.createNonDefault(2L);

            when(shippingAddressQueryPort.findLatestByMemberIdExcluding(any(), any()))
                    .thenReturn(Optional.of(latestAddress));

            // When
            Optional<ShippingAddress> result =
                    shippingAddressReadManager.findLatestExcluding(memberId, excludeId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(2L, result.get().getIdValue());
        }
    }
}
