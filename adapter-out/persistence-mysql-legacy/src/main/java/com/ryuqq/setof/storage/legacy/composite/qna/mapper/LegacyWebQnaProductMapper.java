package com.ryuqq.setof.storage.legacy.composite.qna.mapper;

import com.ryuqq.setof.application.qna.dto.response.QnaAnswerResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaAnswerQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaQueryDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaProductMapper - 상품 Q&A QueryDto -> Application Result 변환 Mapper.
 *
 * <p>비밀글 마스킹, 이름 마스킹은 Application Layer에서 처리합니다.
 *
 * <p>이 Mapper는 순수한 구조 변환만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaProductMapper {

    public QnaWithAnswersResult toQnaWithAnswersResult(LegacyWebQnaQueryDto dto) {
        Set<QnaAnswerResult> answers = toAnswerResults(dto.answers());
        return QnaWithAnswersResult.of(
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
                dto.insertDate(),
                dto.updateDate(),
                answers);
    }

    public List<QnaWithAnswersResult> toQnaWithAnswersResults(List<LegacyWebQnaQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toQnaWithAnswersResult).toList();
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
