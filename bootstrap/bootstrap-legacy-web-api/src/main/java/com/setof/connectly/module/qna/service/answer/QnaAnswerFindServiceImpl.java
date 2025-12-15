package com.setof.connectly.module.qna.service.answer;


import com.setof.connectly.module.exception.qna.QnaAnswerNotFoundException;
import com.setof.connectly.module.exception.qna.QnaReplyException;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.repository.answer.QnaAnswerFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class QnaAnswerFindServiceImpl implements QnaAnswerFindService{

    private final QnaAnswerFindRepository qnaAnswerFindRepository;


    @Override
    public QnaAnswer fetchQnaAnswerEntity(long qnaAnswerId) {
        return qnaAnswerFindRepository.fetchQnaAnswerEntity(qnaAnswerId)
                .orElseThrow(()-> new QnaAnswerNotFoundException(qnaAnswerId));
    }

    @Override
    public QnaAnswer fetchLastQnaAnswerBySeller(long qnaId) {
        return qnaAnswerFindRepository.fetchQnaAnswerOpenStatus(qnaId)
                .orElseThrow(()-> new QnaReplyException(qnaId));
    }

}
