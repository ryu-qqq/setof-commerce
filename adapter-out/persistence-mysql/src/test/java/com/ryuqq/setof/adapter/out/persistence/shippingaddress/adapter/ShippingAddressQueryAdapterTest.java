package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ShippingAddressQueryAdapter 통합 테스트
 *
 * <p>ShippingAddressQueryPort 구현체의 Domain 변환 및 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressQueryAdapter 통합 테스트")
class ShippingAddressQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private ShippingAddressQueryAdapter shippingAddressQueryAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final UUID MEMBER_ID = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
    private static final UUID OTHER_MEMBER_ID =
            UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfb");

    private ShippingAddressJpaEntity createEntity(
            UUID memberId, String addressName, boolean isDefault, Instant deletedAt) {
        return ShippingAddressJpaEntity.of(
                null,
                memberId.toString(),
                addressName,
                "홍길동",
                "01012345678",
                "06234",
                "서울시 강남구 테헤란로 123",
                "서울시 강남구 역삼동 123-45",
                "101동 1001호",
                "문 앞에 놔주세요",
                isDefault,
                NOW,
                NOW,
                deletedAt);
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        private ShippingAddressJpaEntity savedAddress;

        @BeforeEach
        void setUp() {
            savedAddress = persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
        }

        @Test
        @DisplayName("성공 - ID로 배송지 도메인을 조회한다")
        void findById_existingId_returnsShippingAddressDomain() {
            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findById(
                            ShippingAddressId.of(savedAddress.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressNameValue()).isEqualTo("집");
            assertThat(result.get().getReceiverNameValue()).isEqualTo("홍길동");
            assertThat(result.get().isDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findById(ShippingAddressId.of(9999L));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 배송지는 조회되지 않는다")
        void findById_deletedAddress_returnsEmpty() {
            // Given
            ShippingAddressJpaEntity deleted =
                    persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));
            flushAndClear();

            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findById(ShippingAddressId.of(deleted.getId()));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIdIncludeDeleted 메서드")
    class FindByIdIncludeDeleted {

        private ShippingAddressJpaEntity deletedAddress;

        @BeforeEach
        void setUp() {
            deletedAddress = persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));
        }

        @Test
        @DisplayName("성공 - 삭제된 배송지도 조회한다")
        void findByIdIncludeDeleted_deletedAddress_returnsAddress() {
            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findByIdIncludeDeleted(
                            ShippingAddressId.of(deletedAddress.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressNameValue()).isEqualTo("삭제됨");
            assertThat(result.get().isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("findAllByMemberId 메서드")
    class FindAllByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
            persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));
            persistAndFlush(createEntity(OTHER_MEMBER_ID, "다른회원집", true, null));
        }

        @Test
        @DisplayName("성공 - 회원별 배송지 도메인 목록을 조회한다")
        void findAllByMemberId_existingMember_returnsDomains() {
            // When
            List<ShippingAddress> result = shippingAddressQueryAdapter.findAllByMemberId(MEMBER_ID);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).noneMatch(addr -> "삭제됨".equals(addr.getAddressNameValue()));
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 먼저 정렬된 도메인 목록 반환")
        void findAllByMemberId_orderedByDefaultFirst() {
            // When
            List<ShippingAddress> result = shippingAddressQueryAdapter.findAllByMemberId(MEMBER_ID);

            // Then
            assertThat(result.get(0).isDefault()).isTrue();
            assertThat(result.get(0).getAddressNameValue()).isEqualTo("집");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 조회 시 빈 리스트 반환")
        void findAllByMemberId_nonExistingMember_returnsEmptyList() {
            // When
            List<ShippingAddress> result =
                    shippingAddressQueryAdapter.findAllByMemberId(UUID.randomUUID());

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findDefaultByMemberId 메서드")
    class FindDefaultByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
        }

        @Test
        @DisplayName("성공 - 회원의 기본 배송지 도메인을 조회한다")
        void findDefaultByMemberId_existingDefault_returnsDefaultDomain() {
            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findDefaultByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressNameValue()).isEqualTo("집");
            assertThat(result.get().isDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 없는 경우 빈 Optional 반환")
        void findDefaultByMemberId_noDefault_returnsEmpty() {
            // Given
            UUID memberWithoutDefault = UUID.randomUUID();
            persistAndFlush(createEntity(memberWithoutDefault, "회사", false, null));
            flushAndClear();

            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findDefaultByMemberId(memberWithoutDefault);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByMemberId 메서드")
    class CountByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
            persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));
        }

        @Test
        @DisplayName("성공 - 회원별 배송지 개수를 조회한다")
        void countByMemberId_existingMember_returnsCount() {
            // When
            long result = shippingAddressQueryAdapter.countByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isEqualTo(2L);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 조회 시 0 반환")
        void countByMemberId_nonExistingMember_returnsZero() {
            // When
            long result = shippingAddressQueryAdapter.countByMemberId(UUID.randomUUID());

            // Then
            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("findLatestByMemberIdExcluding 메서드")
    class FindLatestByMemberIdExcluding {

        private ShippingAddressJpaEntity firstAddress;
        private ShippingAddressJpaEntity secondAddress;

        @BeforeEach
        void setUp() {
            firstAddress = persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            secondAddress = persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
        }

        @Test
        @DisplayName("성공 - 특정 ID 제외 최신 배송지 도메인을 조회한다")
        void findLatestByMemberIdExcluding_excludingId_returnsLatestOtherDomain() {
            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findLatestByMemberIdExcluding(
                            MEMBER_ID, ShippingAddressId.of(firstAddress.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getIdValue()).isEqualTo(secondAddress.getId());
        }

        @Test
        @DisplayName("성공 - 다른 배송지가 없는 경우 빈 Optional 반환")
        void findLatestByMemberIdExcluding_noOtherAddress_returnsEmpty() {
            // Given
            UUID memberWithOneAddress = UUID.randomUUID();
            ShippingAddressJpaEntity onlyAddress =
                    persistAndFlush(createEntity(memberWithOneAddress, "집", true, null));
            flushAndClear();

            // When
            Optional<ShippingAddress> result =
                    shippingAddressQueryAdapter.findLatestByMemberIdExcluding(
                            memberWithOneAddress, ShippingAddressId.of(onlyAddress.getId()));

            // Then
            assertThat(result).isEmpty();
        }
    }
}
