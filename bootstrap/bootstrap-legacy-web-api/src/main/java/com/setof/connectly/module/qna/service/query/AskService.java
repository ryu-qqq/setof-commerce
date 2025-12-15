package com.setof.connectly.module.qna.service.query;

import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.enums.QnaType;

public interface AskService<T extends CreateQna> {
    QnaType getQnaType();

    void doAsk(long qnaId, T createQna);

    void doReply(long qnaId, Long qnaAnswerId, T createQna);

    void doReplyUpdate(long qnaId, Long qnaAnswerId, T createQna);

    void doAskUpdate(long qnaId, T createQna);
}
