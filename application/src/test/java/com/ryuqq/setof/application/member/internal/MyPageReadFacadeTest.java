package com.ryuqq.setof.application.member.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import com.ryuqq.setof.domain.order.vo.MyPageOrderCounts;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
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
@DisplayName("MyPageReadFacade 단위 테스트")
class MyPageReadFacadeTest {

    @InjectMocks private MyPageReadFacade sut;

    @Mock private MemberReadManager memberReadManager;
    @Mock private MileageCompositeReadManager mileageReadManager;
    @Mock private OrderReadManager orderReadManager;

    @Nested
    @DisplayName("getProfile() - 회원 복합 프로필 조회")
    class GetProfileTest {

        @Test
        @DisplayName(
                "userId로 MemberLoginInfo, MileageSummary, MyPageOrderCounts를 조합한 MemberProfile을"
                        + " 반환한다")
        void getProfile_ValidUserId_ReturnsCompositeMemberProfile() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberLoginInfo loginInfo = MemberQueryFixtures.memberLoginInfo();
            MileageSummary mileageSummary = MemberQueryFixtures.defaultMileageSummary();
            List<OrderStatusCount> domainCounts =
                    List.of(
                            new OrderStatusCount("ORDER_PROCESSING", 2),
                            new OrderStatusCount("ORDER_COMPLETED", 5));

            given(memberReadManager.getLoginInfoById(userId)).willReturn(loginInfo);
            given(mileageReadManager.getMileageSummary(userId)).willReturn(mileageSummary);
            given(orderReadManager.countByStatus(userId, MyPageOrderCounts.STATUSES))
                    .willReturn(domainCounts);

            // when
            MemberProfile result = sut.getProfile(userId);

            // then
            assertThat(result.member()).isEqualTo(loginInfo.member());
            assertThat(result.socialLoginType()).isEqualTo(loginInfo.socialLoginType());
            assertThat(result.currentMileage()).isEqualTo(MemberQueryFixtures.DEFAULT_MILEAGE);
            assertThat(result.orderCounts().counts()).hasSize(2);
            then(memberReadManager).should().getLoginInfoById(userId);
            then(mileageReadManager).should().getMileageSummary(userId);
            then(orderReadManager).should().countByStatus(userId, MyPageOrderCounts.STATUSES);
        }

        @Test
        @DisplayName("주문이 없으면 빈 MyPageOrderCounts를 포함한 MemberProfile을 반환한다")
        void getProfile_NoOrders_ReturnsProfileWithEmptyOrderCounts() {
            // given
            long userId = MemberFixtures.DEFAULT_MEMBER_ID;
            MemberLoginInfo loginInfo = MemberQueryFixtures.memberLoginInfo();
            MileageSummary mileageSummary = MileageSummary.empty();

            given(memberReadManager.getLoginInfoById(userId)).willReturn(loginInfo);
            given(mileageReadManager.getMileageSummary(userId)).willReturn(mileageSummary);
            given(orderReadManager.countByStatus(userId, MyPageOrderCounts.STATUSES))
                    .willReturn(Collections.emptyList());

            // when
            MemberProfile result = sut.getProfile(userId);

            // then
            assertThat(result.orderCounts().counts()).isEmpty();
            assertThat(result.orderCounts().totalCount()).isZero();
            assertThat(result.currentMileage()).isZero();
        }
    }
}
