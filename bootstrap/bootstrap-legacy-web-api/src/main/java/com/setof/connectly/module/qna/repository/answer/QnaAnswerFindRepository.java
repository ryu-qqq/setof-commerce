package com.setof.connectly.module.qna.repository.answer;

import com.setof.connectly.module.qna.entity.QnaAnswer;

import java.util.Optional;

public interface QnaAnswerFindRepository {

    Optional<QnaAnswer> fetchQnaAnswerEntity(long qnaAnswerId);
    Optional<QnaAnswer>  fetchQnaAnswerOpenStatus(long qnaId);
}
