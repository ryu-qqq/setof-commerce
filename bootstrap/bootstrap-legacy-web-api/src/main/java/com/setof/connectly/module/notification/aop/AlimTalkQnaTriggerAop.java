package com.setof.connectly.module.notification.aop;

import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.notification.service.alimtalk.QnaAlimTalkService;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AlimTalkQnaTriggerAop {
    private final QnaAlimTalkService qnaAlimTalkService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.qna.service.query.QnaQueryService.doQuestion(..))"
                        + " ")
    private void triggerQnaEnrollPointCut() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.qna.service.answer.query.QnaAnswerQueryService.replyQna(..))"
                        + "  && args(qnaId, createQna) ",
            argNames = "qnaId,createQna")
    private void triggerReplyQnaEnrollPointCut(long qnaId, CreateQna createQna) {}

    @AfterReturning(value = "triggerQnaEnrollPointCut()", returning = "qna", argNames = "pjp, qna")
    public void triggerQnaAlimTalk(JoinPoint pjp, Qna qna) {
        QnaSheet qnaSheet = new QnaSheet(qna.getId(), qna.getQnaType());
        qnaAlimTalkService.sendAlimTalk(qnaSheet);
    }

    @Around(
            value = "triggerReplyQnaEnrollPointCut(qnaId, createQna)",
            argNames = "pjp, qnaId, createQna")
    public Object triggerReplyQnaAlimTalk(ProceedingJoinPoint pjp, long qnaId, CreateQna createQna)
            throws Throwable {
        Object proceed = pjp.proceed();
        QnaSheet qnaSheet = new QnaSheet(qnaId, createQna.getQnaType());
        qnaAlimTalkService.sendAlimTalk(qnaSheet);
        return proceed;
    }
}
