package com.ryuqq.setof.application.member.internal;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * MyPageReadFacade - 마이페이지 조회 Facade.
 *
 * <p>MemberReadManager와 OrderReadManager를 조합하여 마이페이지에 필요한 데이터를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MyPageReadFacade {

    private static final List<String> MY_PAGE_ORDER_STATUSES =
            List.of(
                    "ORDER_PROCESSING",
                    "ORDER_COMPLETED",
                    "DELIVERY_PENDING",
                    "DELIVERY_PROCESSING",
                    "DELIVERY_COMPLETED");

    private final MemberReadManager memberReadManager;
    private final OrderReadManager orderReadManager;

    public MyPageReadFacade(
            MemberReadManager memberReadManager, OrderReadManager orderReadManager) {
        this.memberReadManager = memberReadManager;
        this.orderReadManager = orderReadManager;
    }

    public MemberProfile fetchProfile(long userId) {
        return memberReadManager.getProfileByLegacyId(userId);
    }

    public List<OrderStatusCountResult> fetchOrderCounts(long userId) {
        List<OrderStatusCount> counts =
                orderReadManager.countByStatus(userId, MY_PAGE_ORDER_STATUSES);
        return counts.stream().map(OrderStatusCountResult::from).toList();
    }
}
