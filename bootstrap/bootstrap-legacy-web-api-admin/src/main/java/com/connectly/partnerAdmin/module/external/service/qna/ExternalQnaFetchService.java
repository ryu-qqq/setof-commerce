package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;

import java.util.Optional;

public interface ExternalQnaFetchService {
    Optional<ExternalQnaMappingDto> fetchHasExternalQna(long qnaId);
}
