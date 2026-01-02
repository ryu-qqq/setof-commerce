package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;
import com.connectly.partnerAdmin.module.external.repository.qna.ExternalQnaFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExternalQnaFetchServiceImpl implements ExternalQnaFetchService {

    private final ExternalQnaFetchRepository externalQnaFetchRepository;

    @Override
    public Optional<ExternalQnaMappingDto> fetchHasExternalQna(long qnaId){
        return externalQnaFetchRepository.fetchHasExternalQna(qnaId);
    }

}
