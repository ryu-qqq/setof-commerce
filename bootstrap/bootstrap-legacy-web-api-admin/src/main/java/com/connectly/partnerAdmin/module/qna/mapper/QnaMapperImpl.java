package com.connectly.partnerAdmin.module.qna.mapper;

import com.connectly.partnerAdmin.module.qna.dto.query.CreateOrderQna;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQna;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.entity.*;
import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaIssueType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QnaMapperImpl implements QnaMapper{

    @Override
    public QnaAnswer toQnaAnswerEntity(Qna qna, CreateQnaAnswer createQnaAnswer, Optional<Long> lastQnaAnswerId) {
        QnaAnswer qnaAnswer = new QnaAnswer(qna, createQnaAnswer, lastQnaAnswerId);

        List<QnaImage> qnaImages = createQnaAnswer.getQnaImages().stream()
                    .map(c -> QnaImage.builder()
                            .qnaIssueType(QnaIssueType.ANSWER)
                            .imageUrl(c.getImageUrl())
                            .displayOrder(c.getDisplayOrder())
                            .build())
                    .toList();

        qnaImages.forEach(qnaAnswer::addQnaImages);

        return qnaAnswer;
    }



    @Override
    public Qna toQna(CreateQna createQna) {

        QnaContents qnaContents = new QnaContents(createQna.getQnaContents().getTitle(), createQna.getQnaContents().getContent());

        Qna qna = new Qna(qnaContents, createQna.isPrivate(), createQna.getQnaType(), createQna.getQnaDetailType(), 1L, createQna.getSellerId());

        if(createQna.getQnaType().isProductQna()){
            QnaProduct qnaProduct = new QnaProduct(qna, createQna.getTargetId());
            qna.setQnaProduct(qnaProduct);

        } else if (createQna.getQnaType().isOrderQna()) {

            CreateOrderQna createOrderQna = (CreateOrderQna) createQna;
            List<QnaImage> qnaImages = createOrderQna.getQnaImages().stream()
                    .map(c -> QnaImage.builder()
                            .qnaIssueType(QnaIssueType.ANSWER)
                            .imageUrl(c.getImageUrl())
                            .displayOrder(c.getDisplayOrder())
                            .build())
                    .toList();

            qnaImages.forEach(qna::addQnaImages);
            QnaOrder qnaOrder = new QnaOrder(qna, createOrderQna.getOrderId());
            qna.setQnaOrder(qnaOrder);
        }

        return qna;
    }






}
