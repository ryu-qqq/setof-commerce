package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaMyProductQueryPort;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MyProductQnaReadManager - PRODUCT 타입 내 Q&A 조회 Manager.
 *
 * <p>상품 Q&A 조회 시 상품그룹, 브랜드, 이미지 JOIN이 필요한 복합 조회를 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MyProductQnaReadManager implements MyQnaReadStrategy {

    private final QnaMyProductQueryPort queryPort;

    public MyProductQnaReadManager(QnaMyProductQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Override
    public QnaType supportType() {
        return QnaType.PRODUCT;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MyQnaResult> fetchMyQnas(QnaSearchCriteria criteria) {
        return queryPort.fetchMyProductQnas(criteria);
    }
}
