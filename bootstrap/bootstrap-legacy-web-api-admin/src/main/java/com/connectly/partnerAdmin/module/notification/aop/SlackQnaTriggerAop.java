package com.connectly.partnerAdmin.module.notification.aop;


import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackQnaIssueService;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateProductQna;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;
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
public class SlackQnaTriggerAop {

    private final SlackQnaIssueService slackQnaIssueService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.external.service.qna.ExternalQnaRegistrationServiceImpl.syncQna(..)) && args(exMallQna)", argNames = "exMallQna")
    private void triggerQnaIssueAlimTalkPointCut(ExMallQna exMallQna){}

    @AfterReturning(value = "triggerQnaIssueAlimTalkPointCut(exMallQna)", returning = "externalQna", argNames = "jp, externalQna, exMallQna")
    public void triggerQnaAlimTalk(JoinPoint jp, ExternalQna externalQna, ExMallQna exMallQna) {
        CreateProductQna createProductQna = new CreateProductQna(new CreateQnaContents(exMallQna.getTitle(), exMallQna.getContent()), QnaType.PRODUCT);
        slackQnaIssueService.sendSlackMessage(createProductQna);
    }

}
