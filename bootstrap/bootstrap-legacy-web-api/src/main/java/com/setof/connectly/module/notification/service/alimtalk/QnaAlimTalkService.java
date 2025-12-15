package com.setof.connectly.module.notification.service.alimtalk;


import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.notification.repository.MessageQueueJdbcRepository;
import com.setof.connectly.module.qna.service.fetch.QnaFindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Transactional
@Service
public class QnaAlimTalkService extends AbstractAlimTalkService<QnaSheet>{

    private final QnaAlimTalkMessageConversion qnaAlimTalkMessageConversion;
    private final QnaFindService qnaFindService;

    public QnaAlimTalkService(MessageQueueJdbcRepository messageQueueJdbcRepository, QnaAlimTalkMessageConversion qnaAlimTalkMessageConversion, QnaFindService qnaFindService) {
        super(messageQueueJdbcRepository);
        this.qnaAlimTalkMessageConversion = qnaAlimTalkMessageConversion;
        this.qnaFindService = qnaFindService;
    }


    @Override
    public void sendAlimTalk(QnaSheet qnaSheet) {
        QnaSheet findQnaSheet = qnaFindService.fetchQnaSheet(qnaSheet.getQnaId(), qnaSheet.getQnaType());
        List<MessageQueueContext> messageQueueContexts = qnaAlimTalkMessageConversion.convert(findQnaSheet);
        saveMessageQueueContexts(new HashSet<>(messageQueueContexts));

    }
}
