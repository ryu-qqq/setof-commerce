package com.ryuqq.setof.application.order.assembler;

import com.ryuqq.setof.application.order.dto.response.OrderDetailResult;
import com.ryuqq.setof.application.order.dto.response.OrderSliceResult;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * OrderAssembler - 주문 Result DTO 조립.
 *
 * <p>Manager에서 조회한 도메인 데이터를 조합하여 최종 응답 객체를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderAssembler {

    /**
     * 주문 목록 슬라이스 결과 조립.
     *
     * @param details 주문 상세 목록 (도메인 VO)
     * @param statusCounts 상태별 건수 (도메인 VO)
     * @param criteria 검색 조건 (size 판단용)
     * @param orderIds 조회된 주문 ID 목록 (hasNext 판단용, fetchSize 포함)
     * @return OrderSliceResult
     */
    public OrderSliceResult toSliceResult(
            List<OrderDetail> details,
            List<OrderStatusCount> statusCounts,
            OrderSearchCriteria criteria,
            List<Long> orderIds) {

        boolean hasNext = orderIds.size() > criteria.size();
        List<Long> pageOrderIds = hasNext ? orderIds.subList(0, criteria.size()) : orderIds;
        Long lastId = pageOrderIds.isEmpty() ? null : pageOrderIds.get(pageOrderIds.size() - 1);

        List<OrderDetailResult> content = details.stream().map(OrderDetailResult::from).toList();

        List<OrderStatusCountResult> countResults =
                statusCounts.stream().map(OrderStatusCountResult::from).toList();

        return new OrderSliceResult(content, hasNext, lastId, countResults);
    }
}
