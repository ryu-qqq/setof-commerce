package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;

public interface ExternalQnaAnswerService {
    void syncQna(ExternalQnaMappingDto externalQnaMappingDto, CreateQnaContents qnaContents);
}
