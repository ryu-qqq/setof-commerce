package com.connectly.partnerAdmin.module.notification.aop;

import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.notification.dto.qna.QnaSheet;
import com.connectly.partnerAdmin.module.notification.service.alimtalk.QnaAlimTalkService;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AlimTalkQnaTriggerAop {

    private final QnaAlimTalkService qnaAlimTalkService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.external.service.qna.ExternalQnaRegistrationServiceImpl.syncQna(..)) ")
    private void triggerQnaIssueAlimTalkPointCut(){}

    @AfterReturning(value = "triggerQnaIssueAlimTalkPointCut()", returning = "externalQna", argNames = "jp, externalQna")
    public void triggerQnaAlimTalk(JoinPoint jp, ExternalQna  externalQna) {
        QnaSheet qnaSheet = new QnaSheet(externalQna.getQnaId(), QnaType.PRODUCT);
        qnaAlimTalkService.sendAlimTalk(qnaSheet);
    }

}
