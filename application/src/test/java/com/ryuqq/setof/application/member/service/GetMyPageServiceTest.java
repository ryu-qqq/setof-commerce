package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.internal.MyPageReadFacade;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.domain.member.MemberFixtures;
import java.util.Collections;
import java.util.List;
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
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();
            List<OrderStatusCountResult> orderCounts =
                    MemberQueryFixtures.orderStatusCountResults();

            given(myPageReadFacade.fetchProfile(userId)).willReturn(profile);
            given(myPageReadFacade.fetchOrderCounts(userId)).willReturn(orderCounts);

            // when
            MyPageResult result = sut.execute(userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(profile.member().legacyMemberIdValue());
            assertThat(result.name()).isEqualTo(profile.member().memberNameValue());
            assertThat(result.gradeName()).isEqualTo(profile.gradeName());
            assertThat(result.currentMileage()).isEqualTo(profile.currentMileage());
            assertThat(result.socialLoginType()).isEqualTo(profile.socialLoginType());
            assertThat(result.orderCounts()).hasSize(3);
            then(myPageReadFacade).should().fetchProfile(userId);
            then(myPageReadFacade).should().fetchOrderCounts(userId);
        }

        @Test
        @DisplayName("주문이 없어도 빈 주문 목록과 함께 MyPageResult를 반환한다")
        void execute_NoOrders_ReturnsMyPageResultWithEmptyOrderCounts() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile profile = MemberQueryFixtures.memberProfile();

            given(myPageReadFacade.fetchProfile(userId)).willReturn(profile);
            given(myPageReadFacade.fetchOrderCounts(userId)).willReturn(Collections.emptyList());

            // when
            MyPageResult result = sut.execute(userId);

            // then
            assertThat(result.orderCounts()).isEmpty();
            then(myPageReadFacade).should().fetchProfile(userId);
            then(myPageReadFacade).should().fetchOrderCounts(userId);
        }

        @Test
        @DisplayName("Facade의 fetchProfile과 fetchOrderCounts가 모두 호출된다")
        void execute_ValidUserId_BothFacadeMethodsCalled() {
            // given
            long userId = 2001L;
            MemberProfile profile = MemberQueryFixtures.memberProfile();
            List<OrderStatusCountResult> orderCounts =
                    MemberQueryFixtures.orderStatusCountResults();

            given(myPageReadFacade.fetchProfile(userId)).willReturn(profile);
            given(myPageReadFacade.fetchOrderCounts(userId)).willReturn(orderCounts);

            // when
            sut.execute(userId);

            // then
            then(myPageReadFacade).should().fetchProfile(userId);
            then(myPageReadFacade).should().fetchOrderCounts(userId);
        }
    }
}
