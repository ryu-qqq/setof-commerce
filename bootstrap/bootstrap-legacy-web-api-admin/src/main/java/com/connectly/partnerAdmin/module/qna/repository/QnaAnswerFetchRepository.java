package com.connectly.partnerAdmin.module.qna.repository;

import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;

import java.util.Optional;

public interface QnaAnswerFetchRepository {
    Optional<QnaAnswer> fetchQnaAnswerEntity(long qnaAnswerId, Optional<Long> sellerIdOpt);

    Optional<QnaAnswer> fetchQnaAnswerOpenStatus(long qnaId);

}
