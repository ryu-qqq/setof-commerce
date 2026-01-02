package com.connectly.partnerAdmin.module.notification.service.alimtalk;


import com.connectly.partnerAdmin.module.notification.dto.qna.QnaSheet;
import com.connectly.partnerAdmin.module.notification.repository.MessageQueueJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QnaAlimTalkService extends AbstractAlimTalkService<QnaSheet>{

    public QnaAlimTalkService(MessageQueueJdbcRepository messageQueueJdbcRepository) {
        super(messageQueueJdbcRepository);
    }

    @Override
    public void sendAlimTalk(QnaSheet qnaSheet) {

    }

    //private final InterlockingQnaFindService interlockingQnaFindService;
//    private final QnaAlimTalkMessageConversion qnaAlimTalkMessageConversion;
//    private final QnaFindService qnaFindService;
//
//    public QnaAlimTalkService(MessageQueueJdbcRepository messageQueueJdbcRepository, InterlockingQnaFindService interlockingQnaFindService, QnaAlimTalkMessageConversion qnaAlimTalkMessageConversion, QnaFindService qnaFindService) {
//        super(messageQueueJdbcRepository);
//        this.interlockingQnaFindService = interlockingQnaFindService;
//        this.qnaAlimTalkMessageConversion = qnaAlimTalkMessageConversion;
//        this.qnaFindService = qnaFindService;
//    }
//
//
//    @Override
//    public void sendAlimTalk(QnaSheet qnaSheet) {
//        Optional<ExternalQnaMappingDto> interlockingQnaDto = interlockingQnaFindService.fetchExternalQnaId(qnaSheet.getQnaId());
//        if(interlockingQnaDto.isEmpty()){
//            QnaSheet findQnaSheet = qnaFindService.fetchQnaSheet(qnaSheet.getQnaId(), qnaSheet.getQnaType());
//            List<MessageQueueContext> messageQueueContexts = qnaAlimTalkMessageConversion.convert(findQnaSheet);
//            saveMessageQueueContexts(new HashSet<>(messageQueueContexts));
//        }
//    }
}
