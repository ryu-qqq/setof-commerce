package com.connectly.partnerAdmin.module.external.service.qna;


import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ExternalQnaRegistrationServiceImpl implements ExternalQnaRegistrationService{

    private final ExternalQnaRegistrationProvider<? extends ExMallQna> externalQnaRegistrationProvider;

    @Override
    public <T extends ExMallQna> ExternalQna syncQna(T t) {
        ExternalQnaService<ExMallQna> exMallQnaExternalQnaService = (ExternalQnaService<ExMallQna>) externalQnaRegistrationProvider.get(t.getSiteName());
        return exMallQnaExternalQnaService.syncQna(t);
    }

}
