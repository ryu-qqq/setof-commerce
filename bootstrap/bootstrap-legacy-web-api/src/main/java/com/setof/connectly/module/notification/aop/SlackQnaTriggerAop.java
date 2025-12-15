package com.setof.connectly.module.notification.aop;

import com.setof.connectly.module.notification.service.slack.SlackQnaIssueService;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackQnaTriggerAop {

    private final SlackQnaIssueService slackQnaIssueService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.qna.service.query.QnaQueryService.doQuestion(..))"
                        + " && args(createQna)",
            argNames = "createQna")
    private void triggerQnaEnrollPointCut(CreateQna createQna) {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.qna.service.answer.query.QnaAnswerQueryService.replyQna(..))"
                        + "  && args(qnaId, createQna) ",
            argNames = "qnaId,createQna")
    private void triggerReplyQnaEnrollPointCut(long qnaId, CreateQna createQna) {}

    @Around(value = "triggerQnaEnrollPointCut(createQna)", argNames = "pjp, createQna")
    public Object triggerSlackMessageQnaIssue(ProceedingJoinPoint pjp, CreateQna createQna)
            throws Throwable {
        Object proceed = pjp.proceed();
        slackQnaIssueService.sendSlackMessage(createQna);
        return proceed;
    }

    @Around(
            value = "triggerReplyQnaEnrollPointCut(qnaId, createQna)",
            argNames = "pjp, qnaId, createQna")
    public Object triggerSlackMessageQnaIssue(
            ProceedingJoinPoint pjp, long qnaId, CreateQna createQna) throws Throwable {
        Object proceed = pjp.proceed();
        slackQnaIssueService.sendSlackMessage(createQna);
        return proceed;
    }
}
