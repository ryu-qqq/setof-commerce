package com.ryuqq.setof.application.member.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
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
@DisplayName("MemberReadManager 단위 테스트")
class MemberReadManagerTest {

    @InjectMocks private MemberReadManager sut;

    @Mock private MemberQueryPort memberQueryPort;

    @Nested
    @DisplayName("getByLegacyId() - 레거시 ID로 회원 조회")
    class GetByLegacyIdTest {

        @Test
        @DisplayName("존재하는 userId로 회원을 조회한다")
        void getByLegacyId_ExistingUser_ReturnsMember() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            Member expected = MemberFixtures.activeMigratedMember();

            given(memberQueryPort.findByLegacyId(userId)).willReturn(Optional.of(expected));

            // when
            Member result = sut.getByLegacyId(userId);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberQueryPort).should().findByLegacyId(userId);
        }

        @Test
        @DisplayName("존재하지 않는 userId면 MemberNotFoundException이 발생한다")
        void getByLegacyId_NonExistingUser_ThrowsMemberNotFoundException() {
            // given
            long userId = 99999L;

            given(memberQueryPort.findByLegacyId(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByLegacyId(userId))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByPhoneNumber() - 전화번호로 회원 조회 (Optional)")
    class FindByPhoneNumberTest {

        @Test
        @DisplayName("가입된 전화번호면 Optional.of(member)를 반환한다")
        void findByPhoneNumber_ExistingPhone_ReturnsOptionalMember() {
            // given
            String phoneNumber = "01012345678";
            Member expected = MemberFixtures.activeMigratedMember();

            given(memberQueryPort.findByPhoneNumber(phoneNumber)).willReturn(Optional.of(expected));

            // when
            Optional<Member> result = sut.findByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(memberQueryPort).should().findByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("미가입 전화번호면 Optional.empty()를 반환한다")
        void findByPhoneNumber_NonExistingPhone_ReturnsOptionalEmpty() {
            // given
            String phoneNumber = "01099999999";

            given(memberQueryPort.findByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

            // when
            Optional<Member> result = sut.findByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isEmpty();
            then(memberQueryPort).should().findByPhoneNumber(phoneNumber);
        }
    }

    @Nested
    @DisplayName("existsByPhoneNumber() - 전화번호 가입 여부 확인")
    class ExistsByPhoneNumberTest {

        @Test
        @DisplayName("가입된 전화번호면 true를 반환한다")
        void existsByPhoneNumber_ExistingPhone_ReturnsTrue() {
            // given
            String phoneNumber = "01012345678";

            given(memberQueryPort.existsByPhoneNumber(phoneNumber)).willReturn(true);

            // when
            boolean result = sut.existsByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isTrue();
            then(memberQueryPort).should().existsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("미가입 전화번호면 false를 반환한다")
        void existsByPhoneNumber_NonExistingPhone_ReturnsFalse() {
            // given
            String phoneNumber = "01099999999";

            given(memberQueryPort.existsByPhoneNumber(phoneNumber)).willReturn(false);

            // when
            boolean result = sut.existsByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isFalse();
            then(memberQueryPort).should().existsByPhoneNumber(phoneNumber);
        }
    }

    @Nested
    @DisplayName("findWithCredentialsByPhoneNumber() - 전화번호로 회원+인증 정보 조회 (Optional)")
    class FindWithCredentialsByPhoneNumberTest {

        @Test
        @DisplayName("가입된 전화번호면 Optional.of(MemberWithCredentials)를 반환한다")
        void findWithCredentialsByPhoneNumber_ExistingPhone_ReturnsOptionalCredentials() {
            // given
            String phoneNumber = "01012345678";
            MemberWithCredentials expected = MemberQueryFixtures.memberWithCredentials();

            given(memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(expected));

            // when
            Optional<MemberWithCredentials> result =
                    sut.findWithCredentialsByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(memberQueryPort).should().findWithCredentialsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("미가입 전화번호면 Optional.empty()를 반환한다")
        void findWithCredentialsByPhoneNumber_NonExistingPhone_ReturnsOptionalEmpty() {
            // given
            String phoneNumber = "01099999999";

            given(memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.empty());

            // when
            Optional<MemberWithCredentials> result =
                    sut.findWithCredentialsByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getWithCredentialsByPhoneNumber() - 전화번호로 회원+인증 정보 조회 (로그인용)")
    class GetWithCredentialsByPhoneNumberTest {

        @Test
        @DisplayName("가입된 전화번호면 MemberWithCredentials를 반환한다")
        void getWithCredentialsByPhoneNumber_ExistingPhone_ReturnsCredentials() {
            // given
            String phoneNumber = "01012345678";
            MemberWithCredentials expected = MemberQueryFixtures.memberWithCredentials();

            given(memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(expected));

            // when
            MemberWithCredentials result = sut.getWithCredentialsByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberQueryPort).should().findWithCredentialsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("미가입 전화번호면 MemberNotFoundException이 발생한다")
        void getWithCredentialsByPhoneNumber_NonExistingPhone_ThrowsMemberNotFoundException() {
            // given
            String phoneNumber = "01099999999";

            given(memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getWithCredentialsByPhoneNumber(phoneNumber))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getProfileByLegacyId() - 레거시 ID로 프로필 조회")
    class GetProfileByLegacyIdTest {

        @Test
        @DisplayName("존재하는 userId로 MemberProfile을 반환한다")
        void getProfileByLegacyId_ExistingUser_ReturnsMemberProfile() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile expected = MemberQueryFixtures.memberProfile();

            given(memberQueryPort.findProfileByLegacyId(userId)).willReturn(Optional.of(expected));

            // when
            MemberProfile result = sut.getProfileByLegacyId(userId);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberQueryPort).should().findProfileByLegacyId(userId);
        }

        @Test
        @DisplayName("존재하지 않는 userId면 MemberNotFoundException이 발생한다")
        void getProfileByLegacyId_NonExistingUser_ThrowsMemberNotFoundException() {
            // given
            long userId = 99999L;

            given(memberQueryPort.findProfileByLegacyId(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getProfileByLegacyId(userId))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}
