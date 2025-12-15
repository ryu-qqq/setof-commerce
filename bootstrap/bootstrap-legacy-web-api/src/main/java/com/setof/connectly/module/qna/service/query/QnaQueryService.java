package com.setof.connectly.module.qna.service.query;

import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;

public interface QnaQueryService {
    Qna doQuestion(CreateQna createQna);
    Qna updateQuestion(long qnaId, CreateQna createQna);

}
