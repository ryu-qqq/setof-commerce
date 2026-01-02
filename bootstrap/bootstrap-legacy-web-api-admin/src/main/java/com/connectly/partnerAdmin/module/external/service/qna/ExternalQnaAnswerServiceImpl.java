package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ExternalQnaAnswerServiceImpl implements ExternalQnaAnswerService {

    private ExternalQnaAnswerProvider externalQnaAnswerProvider;

    @Override
    public void syncQna(ExternalQnaMappingDto externalQnaMapping, CreateQnaContents qnaContents) {
        ExternalQnaSyncService externalQnaSyncService = externalQnaAnswerProvider.get(SiteName.of(externalQnaMapping.getSiteId()));
        externalQnaSyncService.doAnswer(externalQnaMapping.getExternalIdx(), qnaContents);
    }
}
