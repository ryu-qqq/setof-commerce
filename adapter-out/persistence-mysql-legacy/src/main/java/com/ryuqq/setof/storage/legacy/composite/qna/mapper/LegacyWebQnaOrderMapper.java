package com.ryuqq.setof.storage.legacy.composite.qna.mapper;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerResult;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebMyOrderQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaAnswerQueryDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaOrderMapper - 내 주문 Q&A QueryDto -> Application Result 변환 Mapper.
 *
 * <p>비밀글 마스킹, 이름 마스킹은 Application Layer에서 처리합니다.
 *
 * <p>이 Mapper는 순수한 구조 변환만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaOrderMapper {

    public List<MyQnaResult> toMyQnaResults(
            List<LegacyWebMyOrderQnaQueryDto> dtos, Map<Long, String> optionMap) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(dto -> toMyQnaResult(dto, optionMap)).toList();
    }

    private MyQnaResult toMyQnaResult(
            LegacyWebMyOrderQnaQueryDto dto, Map<Long, String> optionMap) {
        Set<QnaAnswerResult> answers = toAnswerResults(dto.answers());
        String option = dto.orderId() != null ? optionMap.getOrDefault(dto.orderId(), null) : null;

        return MyQnaResult.ofOrder(
                dto.qnaId(),
                dto.title(),
                dto.content(),
                dto.privateYn(),
                dto.qnaStatus(),
                dto.qnaType(),
                dto.qnaDetailType(),
                dto.userType(),
                dto.userId(),
                dto.userName(),
                dto.productGroupId(),
                dto.productGroupName(),
                dto.imageUrl(),
                dto.brandId(),
                dto.brandName(),
                dto.orderId(),
                dto.paymentId(),
                dto.orderAmount(),
                dto.quantity(),
                option,
                dto.insertDate(),
                dto.updateDate(),
                answers);
    }

    private Set<QnaAnswerResult> toAnswerResults(Set<LegacyWebQnaAnswerQueryDto> answerDtos) {
        if (answerDtos == null) {
            return Set.of();
        }
        return answerDtos.stream()
                .filter(LegacyWebQnaAnswerQueryDto::isPresent)
                .map(this::toAnswerResult)
                .collect(Collectors.toSet());
    }

    private QnaAnswerResult toAnswerResult(LegacyWebQnaAnswerQueryDto dto) {
        return QnaAnswerResult.of(
                dto.qnaAnswerId(),
                dto.qnaParentId(),
                dto.qnaWriterType(),
                dto.title(),
                dto.content(),
                dto.insertDate(),
                dto.updateDate());
    }
}
