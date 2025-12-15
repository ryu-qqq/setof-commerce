package com.setof.connectly.module.qna.service.answer;

import com.setof.connectly.module.qna.entity.QnaAnswer;

public interface QnaAnswerFindService {

    QnaAnswer fetchQnaAnswerEntity(long qnaAnswerId);
    QnaAnswer fetchLastQnaAnswerBySeller(long qnaId);


}
