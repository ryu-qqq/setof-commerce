package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * QnaRegistrationStrategyProvider - QnaType 기반 등록 전략 O(1) 제공자.
 *
 * <p>Map&lt;QnaType, QnaRegistrationStrategy&gt;로 O(1) 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaRegistrationStrategyProvider {

    private final Map<QnaType, QnaRegistrationStrategy> strategyMap;

    public QnaRegistrationStrategyProvider(List<QnaRegistrationStrategy> strategies) {
        this.strategyMap =
                strategies.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        QnaRegistrationStrategy::supportType, Function.identity()));
    }

    /**
     * QnaType에 맞는 등록 전략을 반환합니다.
     *
     * @param qnaType Q&A 유형
     * @return 해당 타입의 등록 전략
     * @throws IllegalArgumentException 지원하지 않는 QnaType인 경우
     */
    public QnaRegistrationStrategy getStrategy(QnaType qnaType) {
        QnaRegistrationStrategy strategy = strategyMap.get(qnaType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 Q&A 유형입니다: " + qnaType);
        }
        return strategy;
    }
}
