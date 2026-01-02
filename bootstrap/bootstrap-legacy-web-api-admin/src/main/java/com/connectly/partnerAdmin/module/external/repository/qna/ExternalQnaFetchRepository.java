package com.connectly.partnerAdmin.module.external.repository.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;

import java.util.Optional;

public interface ExternalQnaFetchRepository {
    Optional<ExternalQnaMappingDto> fetchHasExternalQna(long qnaId);
}
