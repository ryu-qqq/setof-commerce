package com.ryuqq.setof.application.member.internal;

import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import com.ryuqq.setof.domain.order.vo.MyPageOrderCounts;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MyPageReadFacade - 마이페이지 조회 Facade.
 *
 * <p>MemberReadManager, MileageCompositeReadManager, OrderReadManager를 조합하여 복합 MemberProfile을 한번에
 * 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MyPageReadFacade {

    private final MemberReadManager memberReadManager;
    private final MileageCompositeReadManager mileageReadManager;
    private final OrderReadManager orderReadManager;

    public MyPageReadFacade(
            MemberReadManager memberReadManager,
            MileageCompositeReadManager mileageReadManager,
            OrderReadManager orderReadManager) {
        this.memberReadManager = memberReadManager;
        this.mileageReadManager = mileageReadManager;
        this.orderReadManager = orderReadManager;
    }

    /**
     * 회원 복합 프로필 조회.
     *
     * <p>회원 로그인 정보, 마일리지 요약, 주문 상태별 건수를 한번에 조회하여 MemberProfile로 반환.
     *
     * @param userId 사용자 ID
     * @return MemberProfile (MemberLoginInfo + MileageSummary + MyPageOrderCounts)
     */
    @Transactional(readOnly = true)
    public MemberProfile getProfile(long userId) {
        MemberLoginInfo loginInfo = memberReadManager.getLoginInfoById(userId);
        MileageSummary mileageSummary = mileageReadManager.getMileageSummary(userId);

        List<OrderStatusCount> counts =
                orderReadManager.countByStatus(userId, MyPageOrderCounts.STATUSES);
        MyPageOrderCounts orderCounts = MyPageOrderCounts.of(counts);

        return new MemberProfile(loginInfo, mileageSummary, orderCounts);
    }
}
