package com.connectly.partnerAdmin.module.qna.service;

import com.connectly.partnerAdmin.module.qna.dto.CreateQnaAnswerResponse;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.dto.query.UpdateQnaAnswer;

public interface QnaAnswerRegistrationService {

    CreateQnaAnswerResponse doAnswer(CreateQnaAnswer createQnaAnswer);
    CreateQnaAnswerResponse updateAnswer(UpdateQnaAnswer updateQnaAnswer);

}
