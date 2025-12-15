package com.setof.connectly.module.qna.service.answer.query;


import com.setof.connectly.module.exception.qna.QnaContentsUpdateException;
import com.setof.connectly.module.exception.qna.QnaReplyException;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.mapper.QnaMapper;
import com.setof.connectly.module.qna.repository.answer.QnaAnswerRepository;
import com.setof.connectly.module.qna.service.AskStrategy;
import com.setof.connectly.module.qna.service.answer.QnaAnswerFindService;
import com.setof.connectly.module.qna.service.fetch.QnaFindService;
import com.setof.connectly.module.qna.service.query.AskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class QnaAnswerQueryServiceImpl implements QnaAnswerQueryService{

    private final QnaFindService qnaFindService;
    private final QnaAnswerFindService qnaAnswerFindService;
    private final QnaAnswerRepository qnaAnswerRepository;
    private final QnaMapper qnaMapper;
    private final AskStrategy askStrategy;

    @Override
    public QnaAnswer replyQna(long qnaId, CreateQna createQna) {
        QnaStatus qnaStatus = qnaFindService.fetchQnaStatus(qnaId);
        if(qnaStatus.isOpen()) throw new QnaReplyException(qnaId);
        QnaAnswer findQnaAnswer = qnaAnswerFindService.fetchLastQnaAnswerBySeller(qnaId);

        QnaAnswer qnaAnswer = qnaMapper.toQnaAnswerEntity(qnaId, createQna, Optional.empty());
        QnaAnswer savedQnaAnswer = qnaAnswerRepository.save(qnaAnswer);

        if(createQna.getQnaType().isOrderQna()){
            @SuppressWarnings("unchecked")
            AskService<CreateQna> askService = (AskService<CreateQna>) askStrategy.get(createQna.getQnaType());
            askService.doReply(qnaId, savedQnaAnswer.getId(), createQna);
        }

        findQnaAnswer.reply();

        return savedQnaAnswer;
    }

    @Override
    public QnaAnswer updateReplyQna(long qnaId, long qnaAnswerId, CreateQna createQna){
        QnaAnswer qnaAnswer = qnaAnswerFindService.fetchQnaAnswerEntity(qnaAnswerId);
        if(qnaAnswer.isClosed()) throw new QnaContentsUpdateException(qnaId);
        qnaAnswer.update(createQna.getQnaContents());

        if(createQna.getQnaType().isOrderQna()){
            @SuppressWarnings("unchecked")
            AskService<CreateQna> askService = (AskService<CreateQna>) askStrategy.get(createQna.getQnaType());
            askService.doReplyUpdate(qnaId, qnaAnswer.getId(), createQna);
        }

        return qnaAnswer;
    }
}
