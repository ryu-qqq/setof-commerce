package com.ryuqq.setof.storage.legacy.composite.qna.adapter;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaMyOrderQueryPort;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebMyOrderQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.mapper.LegacyWebQnaOrderMapper;
import com.ryuqq.setof.storage.legacy.composite.qna.repository.LegacyWebQnaCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.composite.qna.repository.LegacyWebQnaOrderQueryDslRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaOrderQueryAdapter - ORDER 타입 내 Q&A 복합 조회 Adapter.
 *
 * <p>QnaMyOrderQueryPort 구현체입니다. 주문 정보(결제, 금액, 수량, 옵션) + 상품 정보 JOIN이 필요한 복합 조회를 처리합니다.
 *
 * <p>2-step ID 조회는 공통 Repository(LegacyWebQnaCompositeQueryDslRepository)를 재사용하고, ORDER 전용 상세 조회는
 * LegacyWebQnaOrderQueryDslRepository에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaOrderQueryAdapter implements QnaMyOrderQueryPort {

    private final LegacyWebQnaCompositeQueryDslRepository compositeRepository;
    private final LegacyWebQnaOrderQueryDslRepository orderRepository;
    private final LegacyWebQnaOrderMapper mapper;

    public LegacyWebQnaOrderQueryAdapter(
            LegacyWebQnaCompositeQueryDslRepository compositeRepository,
            LegacyWebQnaOrderQueryDslRepository orderRepository,
            LegacyWebQnaOrderMapper mapper) {
        this.compositeRepository = compositeRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MyQnaResult> fetchMyOrderQnas(QnaSearchCriteria criteria) {
        List<Long> qnaIds =
                compositeRepository.fetchMyQnaIds(
                        criteria.memberIdValue(),
                        criteria.cursor(),
                        criteria.qnaTypeValue(),
                        criteria.startDate(),
                        criteria.endDate(),
                        criteria.fetchSize());

        if (qnaIds.isEmpty()) {
            return List.of();
        }

        List<LegacyWebMyOrderQnaQueryDto> dtos = orderRepository.fetchMyOrderQnasByIds(qnaIds);

        List<Long> orderIds =
                dtos.stream()
                        .map(LegacyWebMyOrderQnaQueryDto::orderId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();

        Map<Long, String> optionMap = orderRepository.fetchOptionsByOrderIds(orderIds);

        return mapper.toMyQnaResults(dtos, optionMap);
    }
}
