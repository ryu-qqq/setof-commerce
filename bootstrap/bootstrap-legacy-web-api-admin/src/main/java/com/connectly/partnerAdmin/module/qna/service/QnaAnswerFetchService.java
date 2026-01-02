package com.connectly.partnerAdmin.module.qna.service;

import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;

import java.util.Optional;

public interface QnaAnswerFetchService {
    QnaAnswer fetchQnaAnswerEntity(long qnaAnswerId);
    Optional<QnaAnswer> fetchLastQnaAnswerByCustomer(long qnaId);

}
