package com.ryuqq.setof.application.shippingaddress.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressDomainFixtures;
import com.ryuqq.setof.application.shippingaddress.ShippingAddressQueryFixtures;
import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.application.shippingaddress.port.out.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import java.util.Collections;
import java.util.List;
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
@DisplayName("ShippingAddressReadManager 단위 테스트")
class ShippingAddressReadManagerTest {

    @InjectMocks private ShippingAddressReadManager sut;

    @Mock private ShippingAddressQueryPort queryPort;

    @Mock private ShippingAddressAssembler assembler;

    @Nested
    @DisplayName("getById() - ID로 배송지 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 배송지를 userId와 shippingAddressId로 조회한다")
        void getById_ExistingAddress_ReturnsShippingAddress() {
            // given
            Long userId = 1L;
            Long shippingAddressId = 100L;
            ShippingAddress expected =
                    ShippingAddressDomainFixtures.activeShippingAddress(shippingAddressId, userId);

            given(queryPort.findById(userId, shippingAddressId)).willReturn(Optional.of(expected));

            // when
            ShippingAddress result = sut.getById(userId, shippingAddressId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(userId, shippingAddressId);
        }

        @Test
        @DisplayName("존재하지 않는 배송지 조회 시 ShippingAddressNotFoundException이 발생한다")
        void getById_NonExistingAddress_ThrowsException() {
            // given
            Long userId = 1L;
            Long shippingAddressId = 999L;

            given(queryPort.findById(userId, shippingAddressId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(userId, shippingAddressId))
                    .isInstanceOf(ShippingAddressNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getAllByUserId() - userId로 배송지 목록 조회")
    class GetAllByUserIdTest {

        @Test
        @DisplayName("userId로 해당 사용자의 배송지 목록을 조회한다")
        void getAllByUserId_ReturnsShippingAddressList() {
            // given
            Long userId = 1L;
            List<ShippingAddress> expected =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);

            given(queryPort.findAllByUserId(userId)).willReturn(expected);

            // when
            List<ShippingAddress> result = sut.getAllByUserId(userId);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findAllByUserId(userId);
        }

        @Test
        @DisplayName("배송지가 없는 사용자는 빈 목록을 반환한다")
        void getAllByUserId_NoAddresses_ReturnsEmptyList() {
            // given
            Long userId = 999L;

            given(queryPort.findAllByUserId(userId)).willReturn(Collections.emptyList());

            // when
            List<ShippingAddress> result = sut.getAllByUserId(userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getBookByUserId() - userId로 ShippingAddressBook 조회")
    class GetBookByUserIdTest {

        @Test
        @DisplayName("userId로 ShippingAddressBook을 생성하여 반환한다")
        void getBookByUserId_ReturnsShippingAddressBook() {
            // given
            Long userId = 1L;
            List<ShippingAddress> addresses =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);

            given(queryPort.findAllByUserId(userId)).willReturn(addresses);

            // when
            ShippingAddressBook result = sut.getBookByUserId(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(3);
            then(queryPort).should().findAllByUserId(userId);
        }

        @Test
        @DisplayName("배송지가 없는 사용자는 빈 ShippingAddressBook을 반환한다")
        void getBookByUserId_NoAddresses_ReturnsEmptyBook() {
            // given
            Long userId = 999L;

            given(queryPort.findAllByUserId(userId)).willReturn(Collections.emptyList());

            // when
            ShippingAddressBook result = sut.getBookByUserId(userId);

            // then
            assertThat(result.size()).isZero();
        }
    }

    @Nested
    @DisplayName("countByUserId() - userId로 배송지 수 조회")
    class CountByUserIdTest {

        @Test
        @DisplayName("userId로 해당 사용자의 배송지 수를 반환한다")
        void countByUserId_ReturnsCount() {
            // given
            Long userId = 1L;
            int expectedCount = 3;

            given(queryPort.countByUserId(userId)).willReturn(expectedCount);

            // when
            int result = sut.countByUserId(userId);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countByUserId(userId);
        }
    }

    @Nested
    @DisplayName("fetchShippingAddresses() - userId로 배송지 결과 목록 조회")
    class FetchShippingAddressesTest {

        @Test
        @DisplayName("userId로 배송지 결과 목록을 조회한다")
        void fetchShippingAddresses_ReturnsResultList() {
            // given
            Long userId = 1L;
            List<ShippingAddress> domains =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);
            List<ShippingAddressResult> expected =
                    ShippingAddressQueryFixtures.shippingAddressResults();

            given(queryPort.findAllByUserId(userId)).willReturn(domains);
            given(assembler.toResults(domains)).willReturn(expected);

            // when
            List<ShippingAddressResult> result = sut.fetchShippingAddresses(userId);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findAllByUserId(userId);
            then(assembler).should().toResults(domains);
        }

        @Test
        @DisplayName("배송지가 없으면 빈 목록을 반환한다")
        void fetchShippingAddresses_NoResults_ReturnsEmptyList() {
            // given
            Long userId = 999L;

            given(queryPort.findAllByUserId(userId)).willReturn(Collections.emptyList());
            given(assembler.toResults(Collections.emptyList())).willReturn(Collections.emptyList());

            // when
            List<ShippingAddressResult> result = sut.fetchShippingAddresses(userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("fetchShippingAddress() - userId와 shippingAddressId로 단건 배송지 결과 조회")
    class FetchShippingAddressTest {

        @Test
        @DisplayName("userId와 shippingAddressId로 배송지 단건 결과를 조회한다")
        void fetchShippingAddress_ExistingAddress_ReturnsResult() {
            // given
            Long userId = 1L;
            Long shippingAddressId = 100L;
            ShippingAddress domain =
                    ShippingAddressDomainFixtures.activeShippingAddress(shippingAddressId, userId);
            ShippingAddressResult expected =
                    ShippingAddressQueryFixtures.shippingAddressResult(shippingAddressId);

            given(queryPort.findById(userId, shippingAddressId)).willReturn(Optional.of(domain));
            given(assembler.toResult(domain)).willReturn(expected);

            // when
            ShippingAddressResult result = sut.fetchShippingAddress(userId, shippingAddressId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.shippingAddressId()).isEqualTo(shippingAddressId);
            then(queryPort).should().findById(userId, shippingAddressId);
            then(assembler).should().toResult(domain);
        }

        @Test
        @DisplayName("존재하지 않는 배송지 단건 조회 시 ShippingAddressNotFoundException이 발생한다")
        void fetchShippingAddress_NonExisting_ThrowsException() {
            // given
            Long userId = 1L;
            Long shippingAddressId = 999L;

            given(queryPort.findById(userId, shippingAddressId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.fetchShippingAddress(userId, shippingAddressId))
                    .isInstanceOf(ShippingAddressNotFoundException.class);
        }
    }
}
