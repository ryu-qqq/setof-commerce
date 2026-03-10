package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
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
@DisplayName("GetUserService 단위 테스트")
class GetUserServiceTest {

    @InjectMocks private GetUserService sut;

    @Mock private MemberReadManager memberReadManager;

    @Nested
    @DisplayName("execute() - 회원 프로필 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 userId로 회원 프로필을 조회하여 UserResult를 반환한다")
        void execute_ValidUserId_ReturnsUserResult() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(memberReadManager.getProfileByLegacyId(userId)).willReturn(profile);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(profile.member().legacyMemberIdValue());
            assertThat(result.name()).isEqualTo(profile.member().memberNameValue());
            assertThat(result.gradeName()).isEqualTo(profile.gradeName());
            assertThat(result.currentMileage()).isEqualTo(profile.currentMileage());
            then(memberReadManager).should().getProfileByLegacyId(userId);
        }

        @Test
        @DisplayName("활성 회원이면 소셜 로그인 타입이 결과에 포함된다")
        void execute_ActiveMember_ReturnsSocialLoginType() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(memberReadManager.getProfileByLegacyId(userId)).willReturn(profile);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result.socialLoginType())
                    .isEqualTo(MemberQueryFixtures.DEFAULT_SOCIAL_LOGIN_TYPE);
        }

        @Test
        @DisplayName("legacyMemberId가 있는 회원의 userId가 결과에 포함된다")
        void execute_MigratedMember_ReturnsCorrectUserId() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(memberReadManager.getProfileByLegacyId(userId)).willReturn(profile);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result.userId()).isEqualTo(MemberFixtures.DEFAULT_LEGACY_MEMBER_ID);
        }

        @Test
        @DisplayName("마일리지가 올바르게 반환된다")
        void execute_MemberWithMileage_ReturnsMileage() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(memberReadManager.getProfileByLegacyId(userId)).willReturn(profile);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result.currentMileage()).isEqualTo(MemberQueryFixtures.DEFAULT_MILEAGE);
        }
    }
}
