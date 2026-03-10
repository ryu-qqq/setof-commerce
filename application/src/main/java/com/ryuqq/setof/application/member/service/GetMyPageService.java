package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.internal.MyPageReadFacade;
import com.ryuqq.setof.application.member.port.in.GetMyPageUseCase;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 마이페이지 조회 서비스.
 *
 * <p>GetMyPageUseCase를 구현하며, MyPageReadFacade를 통해 회원 프로필과 주문 건수를 조합합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class GetMyPageService implements GetMyPageUseCase {

    private final MyPageReadFacade myPageReadFacade;

    public GetMyPageService(MyPageReadFacade myPageReadFacade) {
        this.myPageReadFacade = myPageReadFacade;
    }

    @Override
    public MyPageResult execute(long userId) {
        MemberProfile profile = myPageReadFacade.fetchProfile(userId);
        List<OrderStatusCountResult> orderCounts = myPageReadFacade.fetchOrderCounts(userId);
        Member member = profile.member();

        return new MyPageResult(
                member.legacyMemberIdValue(),
                member.phoneNumberValue(),
                member.memberNameValue(),
                member.emailValue(),
                profile.gradeName(),
                profile.currentMileage(),
                profile.socialLoginType(),
                member.createdAt(),
                orderCounts);
    }
}
