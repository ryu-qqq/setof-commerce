package com.connectly.partnerAdmin.module.qna.service;

import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;
import com.connectly.partnerAdmin.module.qna.exception.QnaNotFoundException;
import com.connectly.partnerAdmin.module.qna.repository.QnaAnswerFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class QnaAnswerFetchServiceImpl implements QnaAnswerFetchService{

    private final QnaAnswerFetchRepository qnaAnswerFetchRepository;

    @Override
    public QnaAnswer fetchQnaAnswerEntity(long qnaAnswerId) {
        return qnaAnswerFetchRepository.fetchQnaAnswerEntity(qnaAnswerId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(QnaNotFoundException::new);
    }


    @Override
    public Optional<QnaAnswer> fetchLastQnaAnswerByCustomer(long qnaId) {
        return qnaAnswerFetchRepository.fetchQnaAnswerOpenStatus(qnaId);
    }

}
