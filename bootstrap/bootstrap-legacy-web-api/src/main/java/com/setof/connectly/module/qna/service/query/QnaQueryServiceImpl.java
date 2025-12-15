package com.setof.connectly.module.qna.service.query;


import com.setof.connectly.module.exception.qna.QnaContentsUpdateException;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.mapper.QnaMapper;
import com.setof.connectly.module.qna.repository.QnaRepository;
import com.setof.connectly.module.qna.service.AskStrategy;
import com.setof.connectly.module.qna.service.fetch.QnaFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class QnaQueryServiceImpl implements QnaQueryService{
    private final QnaRepository qnaRepository;
    private final QnaMapper qnaMapper;
    private final AskStrategy askStrategy;

    private final QnaFindService qnaFindService;


    @Override
    public Qna doQuestion(CreateQna createQna){
        Qna qna = qnaMapper.toEntity(createQna);
        Qna savedQna = qnaRepository.save(qna);
        @SuppressWarnings("unchecked")
        AskService<CreateQna> askService = (AskService<CreateQna>) askStrategy.get(savedQna.getQnaType());
        askService.doAsk(savedQna.getId(), createQna);
        return savedQna;
    }

    @Override
    public Qna updateQuestion(long qnaId, CreateQna createQna) {
        Qna qna = qnaFindService.fetchQnaEntity(qnaId);
        if(qna.isClosed()) throw new QnaContentsUpdateException(qnaId);
        qna.updateContents(qna.getQnaContents());

        @SuppressWarnings("unchecked")
        AskService<CreateQna> askService = (AskService<CreateQna>) askStrategy.get(qna.getQnaType());
        askService.doAskUpdate(qnaId, createQna);

        return qna;
    }

}
