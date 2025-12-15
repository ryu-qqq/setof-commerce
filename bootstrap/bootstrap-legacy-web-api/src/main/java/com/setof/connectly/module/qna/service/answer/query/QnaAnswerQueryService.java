package com.setof.connectly.module.qna.service.answer.query;

import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.QnaAnswer;

public interface QnaAnswerQueryService {

    QnaAnswer replyQna(long qnaId, CreateQna createQna);

    QnaAnswer updateReplyQna(long qnaId, long qnaAnswerId, CreateQna createQna);

}
