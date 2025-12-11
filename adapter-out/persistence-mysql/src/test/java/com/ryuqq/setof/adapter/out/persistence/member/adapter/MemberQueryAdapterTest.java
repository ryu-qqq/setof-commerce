package com.ryuqq.setof.adapter.out.persistence.member.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MemberQueryAdapter 통합 테스트
 *
 * <p>MemberQueryPort 구현체의 Domain 변환 및 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("MemberQueryAdapter 통합 테스트")
class MemberQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private MemberQueryAdapter memberQueryAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final String DEFAULT_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
    private static final String DEFAULT_PHONE_NUMBER = "01012345678";
    private static final String DEFAULT_SOCIAL_ID = "kakao_12345678";

    private MemberJpaEntity createTestMemberEntity(
            String id, String phoneNumber, String email, AuthProvider provider, String socialId) {
        return MemberJpaEntity.of(
                id,
                phoneNumber,
                email,
                "$2a$10$hashedPassword",
                "테스트회원",
                LocalDate.of(1990, 1, 1),
                Gender.M,
                provider,
                socialId,
                MemberStatus.ACTIVE,
                true,
                true,
                false,
                null,
                null,
                NOW,
                NOW,
                null);
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        private MemberJpaEntity savedMember;

        @BeforeEach
        void setUp() {
            savedMember =
                    persistAndFlush(
                            createTestMemberEntity(
                                    DEFAULT_MEMBER_ID,
                                    DEFAULT_PHONE_NUMBER,
                                    "test@example.com",
                                    AuthProvider.LOCAL,
                                    null));
        }

        @Test
        @DisplayName("성공 - ID로 Member 도메인을 조회한다")
        void findById_existingId_returnsMemberDomain() {
            // When
            Optional<Member> result = memberQueryAdapter.findById(MemberId.of(DEFAULT_MEMBER_ID));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getIdValue()).isEqualTo(DEFAULT_MEMBER_ID);
            assertThat(result.get().getPhoneNumberValue()).isEqualTo(DEFAULT_PHONE_NUMBER);
            assertThat(result.get().getEmailValue()).isEqualTo("test@example.com");
            assertThat(result.get().getNameValue()).isEqualTo("테스트회원");
            assertThat(result.get().getProvider()).isEqualTo(AuthProvider.LOCAL);
            assertThat(result.get().getStatus()).isEqualTo(MemberStatus.ACTIVE);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_nonExistingId_returnsEmpty() {
            // Given
            String nonExistingId = "01936ddc-8d37-7c6e-8ad6-18c76adc9999";

            // When
            Optional<Member> result = memberQueryAdapter.findById(MemberId.of(nonExistingId));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByPhoneNumber 메서드")
    class FindByPhoneNumber {

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    createTestMemberEntity(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "test@example.com",
                            AuthProvider.LOCAL,
                            null));
            persistAndFlush(
                    createTestMemberEntity(
                            "01936ddc-8d37-7c6e-8ad6-18c76adc9d02",
                            "01087654321",
                            "other@example.com",
                            AuthProvider.KAKAO,
                            "kakao_other"));
        }

        @Test
        @DisplayName("성공 - 핸드폰 번호로 Member 도메인을 조회한다")
        void findByPhoneNumber_existingPhoneNumber_returnsMemberDomain() {
            // When
            Optional<Member> result =
                    memberQueryAdapter.findByPhoneNumber(PhoneNumber.of(DEFAULT_PHONE_NUMBER));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getIdValue()).isEqualTo(DEFAULT_MEMBER_ID);
            assertThat(result.get().getPhoneNumberValue()).isEqualTo(DEFAULT_PHONE_NUMBER);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 핸드폰 번호로 조회 시 빈 Optional 반환")
        void findByPhoneNumber_nonExistingPhoneNumber_returnsEmpty() {
            // When
            Optional<Member> result =
                    memberQueryAdapter.findByPhoneNumber(PhoneNumber.of("01099999999"));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySocialId 메서드")
    class FindBySocialId {

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    createTestMemberEntity(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "kakao@example.com",
                            AuthProvider.KAKAO,
                            DEFAULT_SOCIAL_ID));
        }

        @Test
        @DisplayName("성공 - 소셜 ID로 Member 도메인을 조회한다")
        void findBySocialId_existingSocialId_returnsMemberDomain() {
            // When
            Optional<Member> result =
                    memberQueryAdapter.findBySocialId(SocialId.of(DEFAULT_SOCIAL_ID));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getSocialIdValue()).isEqualTo(DEFAULT_SOCIAL_ID);
            assertThat(result.get().getProvider()).isEqualTo(AuthProvider.KAKAO);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 소셜 ID로 조회 시 빈 Optional 반환")
        void findBySocialId_nonExistingSocialId_returnsEmpty() {
            // When
            Optional<Member> result =
                    memberQueryAdapter.findBySocialId(SocialId.of("kakao_nonexistent"));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByPhoneNumber 메서드")
    class ExistsByPhoneNumber {

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    createTestMemberEntity(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "test@example.com",
                            AuthProvider.LOCAL,
                            null));
        }

        @Test
        @DisplayName("성공 - 존재하는 핸드폰 번호인 경우 true 반환")
        void existsByPhoneNumber_existingPhoneNumber_returnsTrue() {
            // When
            boolean result =
                    memberQueryAdapter.existsByPhoneNumber(PhoneNumber.of(DEFAULT_PHONE_NUMBER));

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 핸드폰 번호인 경우 false 반환")
        void existsByPhoneNumber_nonExistingPhoneNumber_returnsFalse() {
            // When
            boolean result = memberQueryAdapter.existsByPhoneNumber(PhoneNumber.of("01099999999"));

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("소프트 딜리트 처리")
    class SoftDeleteHandling {

        @Test
        @DisplayName("성공 - 삭제된 회원은 조회되지 않는다")
        void findById_deletedMember_returnsEmpty() {
            // Given
            MemberJpaEntity deletedMember =
                    MemberJpaEntity.of(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "deleted@example.com",
                            "$2a$10$hashedPassword",
                            "삭제회원",
                            LocalDate.of(1990, 1, 1),
                            Gender.M,
                            AuthProvider.LOCAL,
                            null,
                            MemberStatus.WITHDRAWN,
                            true,
                            true,
                            false,
                            null,
                            null,
                            NOW,
                            NOW,
                            NOW); // deletedAt 설정

            persistAndFlush(deletedMember);

            // When
            Optional<Member> result = memberQueryAdapter.findById(MemberId.of(DEFAULT_MEMBER_ID));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 회원은 핸드폰 번호로 조회되지 않는다")
        void findByPhoneNumber_deletedMember_returnsEmpty() {
            // Given
            MemberJpaEntity deletedMember =
                    MemberJpaEntity.of(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "deleted@example.com",
                            "$2a$10$hashedPassword",
                            "삭제회원",
                            LocalDate.of(1990, 1, 1),
                            Gender.M,
                            AuthProvider.LOCAL,
                            null,
                            MemberStatus.WITHDRAWN,
                            true,
                            true,
                            false,
                            null,
                            null,
                            NOW,
                            NOW,
                            NOW); // deletedAt 설정

            persistAndFlush(deletedMember);

            // When
            Optional<Member> result =
                    memberQueryAdapter.findByPhoneNumber(PhoneNumber.of(DEFAULT_PHONE_NUMBER));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 삭제된 회원은 existsByPhoneNumber에서 false 반환")
        void existsByPhoneNumber_deletedMember_returnsFalse() {
            // Given
            MemberJpaEntity deletedMember =
                    MemberJpaEntity.of(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_PHONE_NUMBER,
                            "deleted@example.com",
                            "$2a$10$hashedPassword",
                            "삭제회원",
                            LocalDate.of(1990, 1, 1),
                            Gender.M,
                            AuthProvider.LOCAL,
                            null,
                            MemberStatus.WITHDRAWN,
                            true,
                            true,
                            false,
                            null,
                            null,
                            NOW,
                            NOW,
                            NOW); // deletedAt 설정

            persistAndFlush(deletedMember);

            // When
            boolean result =
                    memberQueryAdapter.existsByPhoneNumber(PhoneNumber.of(DEFAULT_PHONE_NUMBER));

            // Then
            assertThat(result).isFalse();
        }
    }
}
