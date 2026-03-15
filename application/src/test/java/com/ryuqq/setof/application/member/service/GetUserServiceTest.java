package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
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
    @Mock private MileageCompositeReadManager mileageReadManager;

    @Nested
    @DisplayName("execute() - 회원 프로필 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 userId로 회원 프로필을 조회하여 UserResult를 반환한다")
        void execute_ValidUserId_ReturnsUserResult() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberLoginInfo loginInfo = MemberQueryFixtures.memberLoginInfo();
            MileageSummary mileage = MemberQueryFixtures.defaultMileageSummary();

            given(memberReadManager.getLoginInfoById(userId)).willReturn(loginInfo);
            given(mileageReadManager.getMileageSummary(userId)).willReturn(mileage);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(loginInfo.member().idValue());
            assertThat(result.name()).isEqualTo(loginInfo.member().memberNameValue());
            assertThat(result.currentMileage()).isEqualTo(mileage.currentMileage());
            then(memberReadManager).should().getLoginInfoById(userId);
            then(mileageReadManager).should().getMileageSummary(userId);
        }

        @Test
        @DisplayName("활성 회원이면 소셜 로그인 타입이 결과에 포함된다")
        void execute_ActiveMember_ReturnsSocialLoginType() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberLoginInfo loginInfo = MemberQueryFixtures.memberLoginInfo();
            MileageSummary mileage = MemberQueryFixtures.defaultMileageSummary();

            given(memberReadManager.getLoginInfoById(userId)).willReturn(loginInfo);
            given(mileageReadManager.getMileageSummary(userId)).willReturn(mileage);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result.socialLoginType())
                    .isEqualTo(MemberQueryFixtures.DEFAULT_SOCIAL_LOGIN_TYPE);
        }

        @Test
        @DisplayName("마일리지가 올바르게 반환된다")
        void execute_MemberWithMileage_ReturnsMileage() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberLoginInfo loginInfo = MemberQueryFixtures.memberLoginInfo();
            MileageSummary mileage = MemberQueryFixtures.defaultMileageSummary();

            given(memberReadManager.getLoginInfoById(userId)).willReturn(loginInfo);
            given(mileageReadManager.getMileageSummary(userId)).willReturn(mileage);

            // when
            UserResult result = sut.execute(userId);

            // then
            assertThat(result.currentMileage()).isEqualTo(MemberQueryFixtures.DEFAULT_MILEAGE);
        }
    }
}
