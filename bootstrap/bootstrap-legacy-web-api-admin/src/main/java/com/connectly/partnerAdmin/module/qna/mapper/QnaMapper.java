package com.connectly.partnerAdmin.module.qna.mapper;

import com.connectly.partnerAdmin.module.qna.dto.query.CreateQna;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;

import java.util.Optional;

public interface QnaMapper {
    Qna toQna(CreateQna createQna);
    QnaAnswer toQnaAnswerEntity(Qna qna, CreateQnaAnswer createQnaAnswer, Optional<Long> lastQnaAnswerId);
}


