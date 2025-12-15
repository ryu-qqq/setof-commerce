package com.setof.connectly.module.qna.service.query;

import com.setof.connectly.module.qna.dto.query.CreateProductQna;
import com.setof.connectly.module.qna.entity.QnaProduct;
import com.setof.connectly.module.qna.enums.QnaType;
import com.setof.connectly.module.qna.repository.QnaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AskProductService implements AskService<CreateProductQna> {

    private final QnaProductRepository qnaProductRepository;

    @Override
    public QnaType getQnaType() {
        return QnaType.PRODUCT;
    }

    @Override
    public void doAsk(long qnaId, CreateProductQna createQna) {
        QnaProduct qnaProduct =
                QnaProduct.builder().qnaId(qnaId).productGroupId(createQna.getTargetId()).build();

        qnaProductRepository.save(qnaProduct);
    }

    @Override
    public void doReply(long qnaId, Long qnaAnswerId, CreateProductQna createQna) {
        // Todo Product 는 아직 이미지 지원 안함
    }

    @Override
    public void doReplyUpdate(long qnaId, Long qnaAnswerId, CreateProductQna createQna) {
        // Todo Product 는 아직 이미지 지원 안함
    }

    @Override
    public void doAskUpdate(long qnaId, CreateProductQna createQna) {
        // Todo Product 는 아직 이미지 지원 안함
    }
}
