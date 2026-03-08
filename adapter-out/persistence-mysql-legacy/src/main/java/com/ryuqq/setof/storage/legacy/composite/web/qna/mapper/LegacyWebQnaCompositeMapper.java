package com.ryuqq.setof.storage.legacy.composite.web.qna.mapper;

import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerResult;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebMyQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebQnaAnswerQueryDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaCompositeMapper - 내 Q&A 복합 조회 QueryDto -> Application Result 변환 Mapper.
 *
 * <p>비밀글 마스킹, 이름 마스킹은 Application Layer에서 처리합니다.
 *
 * <p>이 Mapper는 순수한 구조 변환만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebQnaCompositeMapper {

    public MyQnaResult toMyQnaResult(LegacyWebMyQnaQueryDto dto) {
        Set<QnaAnswerResult> answers = toAnswerResults(dto.answers());
        return MyQnaResult.ofProduct(
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
                dto.insertDate(),
                dto.updateDate(),
                answers);
    }

    public List<MyQnaResult> toMyQnaResults(List<LegacyWebMyQnaQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toMyQnaResult).toList();
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
