package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaMyOrderQueryPort;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MyOrderQnaReadManager - ORDER 타입 내 Q&A 조회 Manager.
 *
 * <p>주문 Q&A 조회 시 주문 정보(결제, 금액, 수량, 옵션) + 상품 정보 JOIN이 필요한 복합 조회를 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MyOrderQnaReadManager implements MyQnaReadStrategy {

    private final QnaMyOrderQueryPort queryPort;

    public MyOrderQnaReadManager(QnaMyOrderQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Override
    public QnaType supportType() {
        return QnaType.ORDER;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MyQnaResult> fetchMyQnas(QnaSearchCriteria criteria) {
        return queryPort.fetchMyOrderQnas(criteria);
    }
}
