package com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * ShippingAddressQueryDslRepository Slice 테스트
 *
 * <p>QueryDSL 기반 ShippingAddress 조회 쿼리를 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressQueryDslRepository Slice 테스트")
@Import(ShippingAddressQueryDslRepository.class)
class ShippingAddressQueryDslRepositoryTest extends JpaSliceTestSupport {

    @Autowired private ShippingAddressQueryDslRepository shippingAddressQueryDslRepository;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String OTHER_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfb";

    private ShippingAddressJpaEntity createEntity(
            String memberId, String addressName, boolean isDefault, Instant deletedAt) {
        return ShippingAddressJpaEntity.of(
                null,
                memberId,
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
        @DisplayName("성공 - ID로 배송지를 조회한다")
        void findById_existingId_returnsAddress() {
            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findById(savedAddress.getId());

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressName()).isEqualTo("집");
            assertThat(result.get().getReceiverName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findById(9999L);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 배송지는 조회되지 않는다")
        void findById_deletedAddress_returnsEmpty() {
            // Given
            ShippingAddressJpaEntity deleted =
                    persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));

            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findById(deleted.getId());

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
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findByIdIncludeDeleted(
                            deletedAddress.getId());

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressName()).isEqualTo("삭제됨");
            assertThat(result.get().getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("findByMemberId 메서드")
    class FindByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
            persistAndFlush(createEntity(MEMBER_ID, "삭제됨", false, NOW));
            persistAndFlush(createEntity(OTHER_MEMBER_ID, "다른회원집", true, null));
        }

        @Test
        @DisplayName("성공 - 회원별 배송지 목록을 조회한다")
        void findByMemberId_existingMember_returnsAddresses() {
            // When
            List<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findByMemberId(MEMBER_ID);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).noneMatch(addr -> "삭제됨".equals(addr.getAddressName()));
            assertThat(result).noneMatch(addr -> "다른회원집".equals(addr.getAddressName()));
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 먼저 정렬된다")
        void findByMemberId_orderedByDefaultFirst() {
            // When
            List<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findByMemberId(MEMBER_ID);

            // Then
            assertThat(result.get(0).isDefault()).isTrue();
            assertThat(result.get(0).getAddressName()).isEqualTo("집");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 조회 시 빈 리스트 반환")
        void findByMemberId_nonExistingMember_returnsEmptyList() {
            // When
            List<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findByMemberId(UUID.randomUUID().toString());

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
        @DisplayName("성공 - 회원의 기본 배송지를 조회한다")
        void findDefaultByMemberId_existingDefault_returnsDefault() {
            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findDefaultByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getAddressName()).isEqualTo("집");
            assertThat(result.get().isDefault()).isTrue();
        }

        @Test
        @DisplayName("성공 - 기본 배송지가 없는 경우 빈 Optional 반환")
        void findDefaultByMemberId_noDefault_returnsEmpty() {
            // Given
            String memberWithoutDefault = UUID.randomUUID().toString();
            persistAndFlush(createEntity(memberWithoutDefault, "회사", false, null));

            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findDefaultByMemberId(memberWithoutDefault);

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
            long result = shippingAddressQueryDslRepository.countByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isEqualTo(2L);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 조회 시 0 반환")
        void countByMemberId_nonExistingMember_returnsZero() {
            // When
            long result =
                    shippingAddressQueryDslRepository.countByMemberId(UUID.randomUUID().toString());

            // Then
            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("findLatestExcluding 메서드")
    class FindLatestExcluding {

        private ShippingAddressJpaEntity firstAddress;
        private ShippingAddressJpaEntity secondAddress;

        @BeforeEach
        void setUp() {
            firstAddress = persistAndFlush(createEntity(MEMBER_ID, "집", true, null));
            secondAddress = persistAndFlush(createEntity(MEMBER_ID, "회사", false, null));
        }

        @Test
        @DisplayName("성공 - 특정 ID 제외 최신 배송지를 조회한다")
        void findLatestExcluding_excludingId_returnsLatestOther() {
            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findLatestExcluding(
                            MEMBER_ID, firstAddress.getId());

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(secondAddress.getId());
        }

        @Test
        @DisplayName("성공 - 다른 배송지가 없는 경우 빈 Optional 반환")
        void findLatestExcluding_noOtherAddress_returnsEmpty() {
            // Given
            String memberWithOneAddress = UUID.randomUUID().toString();
            ShippingAddressJpaEntity onlyAddress =
                    persistAndFlush(createEntity(memberWithOneAddress, "집", true, null));

            // When
            Optional<ShippingAddressJpaEntity> result =
                    shippingAddressQueryDslRepository.findLatestExcluding(
                            memberWithOneAddress, onlyAddress.getId());

            // Then
            assertThat(result).isEmpty();
        }
    }
}
