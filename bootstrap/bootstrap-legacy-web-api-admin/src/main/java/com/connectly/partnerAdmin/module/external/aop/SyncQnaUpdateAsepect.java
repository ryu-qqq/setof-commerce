package com.connectly.partnerAdmin.module.external.aop;


import com.connectly.partnerAdmin.module.external.dto.qna.ExternalQnaMappingDto;
import com.connectly.partnerAdmin.module.external.service.qna.ExternalQnaFetchService;
import com.connectly.partnerAdmin.module.external.service.qna.ExternalQnaAnswerService;
import com.connectly.partnerAdmin.module.qna.dto.CreateQnaAnswerResponse;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class SyncQnaUpdateAsepect {

    private final ExternalQnaFetchService externalQnaFetchService;
    private final ExternalQnaAnswerService externalQnaAnswerService;

    @Pointcut("execution(* com.connectly.partnerAdmin.module.qna.service.QnaAnswerRegistrationServiceImpl.doAnswer(..)) && args(createQnaAnswer)")
    private void syncExMallQna(CreateQnaAnswer createQnaAnswer){}

    @AfterReturning(value = "syncExMallQna(createQnaAnswer)", returning = "createQnaAnswerResponse", argNames = "jp, createQnaAnswerResponse, createQnaAnswer")
    public void syncExternalQna(JoinPoint jp, CreateQnaAnswerResponse createQnaAnswerResponse, CreateQnaAnswer createQnaAnswer) throws Throwable {
        interlockingQna(createQnaAnswer);
    }

    private void interlockingQna(CreateQnaAnswer createQnaAnswer){
        Optional<ExternalQnaMappingDto> externalQnaMappingOpt = externalQnaFetchService.fetchHasExternalQna(createQnaAnswer.getQnaId());
        externalQnaMappingOpt.ifPresent( externalQnaMappingDto -> {
            externalQnaAnswerService.syncQna(externalQnaMappingDto, createQnaAnswer.getQnaContents());
        });
    }

}
