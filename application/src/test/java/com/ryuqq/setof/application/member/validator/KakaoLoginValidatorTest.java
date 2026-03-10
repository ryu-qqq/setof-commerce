package com.ryuqq.setof.application.member.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotActiveException;
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
@DisplayName("KakaoLoginValidator 단위 테스트")
class KakaoLoginValidatorTest {

    @InjectMocks private KakaoLoginValidator sut;

    @Mock private MemberReadManager memberReadManager;

    @Nested
    @DisplayName("validateAndFindExisting() - 기존 회원 조회 및 상태 검증")
    class ValidateAndFindExistingTest {

        @Test
        @DisplayName("미가입 전화번호면 Optional.empty()를 반환한다")
        void validateAndFindExisting_NonExistingPhone_ReturnsOptionalEmpty() {
            // given
            String phoneNumber = "01099999999";

            given(memberReadManager.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.empty());

            // when
            Optional<MemberWithCredentials> result = sut.validateAndFindExisting(phoneNumber);

            // then
            assertThat(result).isEmpty();
            then(memberReadManager).should().findWithCredentialsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("활성 기존 회원이면 Optional.of(MemberWithCredentials)를 반환한다")
        void validateAndFindExisting_ActiveExistingMember_ReturnsMemberWithCredentials() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            MemberWithCredentials credentials = MemberQueryFixtures.kakaoMemberWithCredentials();

            given(memberReadManager.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(credentials));

            // when
            Optional<MemberWithCredentials> result = sut.validateAndFindExisting(phoneNumber);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(credentials);
            then(memberReadManager).should().findWithCredentialsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("탈퇴 회원이면 MemberNotActiveException이 발생한다")
        void validateAndFindExisting_WithdrawnMember_ThrowsMemberNotActiveException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            Member withdrawnMember = MemberFixtures.withdrawnMember();
            MemberWithCredentials credentials =
                    MemberQueryFixtures.memberWithCredentials(withdrawnMember, "KAKAO");

            given(memberReadManager.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(credentials));

            // when & then
            assertThatThrownBy(() -> sut.validateAndFindExisting(phoneNumber))
                    .isInstanceOf(MemberNotActiveException.class);
        }

        @Test
        @DisplayName("정지 회원이면 MemberNotActiveException이 발생한다")
        void validateAndFindExisting_SuspendedMember_ThrowsMemberNotActiveException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            Member suspendedMember = MemberFixtures.suspendedMember();
            MemberWithCredentials credentials =
                    MemberQueryFixtures.memberWithCredentials(suspendedMember, "EMAIL");

            given(memberReadManager.findWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(credentials));

            // when & then
            assertThatThrownBy(() -> sut.validateAndFindExisting(phoneNumber))
                    .isInstanceOf(MemberNotActiveException.class);
        }
    }
}
