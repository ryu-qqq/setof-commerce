package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.internal.MyPageReadFacade;
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
@DisplayName("GetMyPageService 단위 테스트")
class GetMyPageServiceTest {

    @InjectMocks private GetMyPageService sut;

    @Mock private MyPageReadFacade myPageReadFacade;

    @Nested
    @DisplayName("execute() - 마이페이지 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 userId로 마이페이지 정보를 조회하여 MyPageResult를 반환한다")
        void execute_ValidUserId_ReturnsMyPageResult() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(myPageReadFacade.getProfile(userId)).willReturn(profile);

            // when
            MyPageResult result = sut.execute(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(profile.member().idValue());
            assertThat(result.name()).isEqualTo(profile.member().memberNameValue());
            assertThat(result.currentMileage()).isEqualTo(profile.currentMileage());
            assertThat(result.socialLoginType()).isEqualTo(profile.socialLoginType());
            assertThat(result.orderCounts().counts()).hasSize(3);
            then(myPageReadFacade).should().getProfile(userId);
        }

        @Test
        @DisplayName("Facade의 getProfile이 호출된다")
        void execute_ValidUserId_FacadeGetProfileCalled() {
            // given
            long userId = 2001L;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(myPageReadFacade.getProfile(userId)).willReturn(profile);

            // when
            sut.execute(userId);

            // then
            then(myPageReadFacade).should().getProfile(userId);
        }
    }
}
