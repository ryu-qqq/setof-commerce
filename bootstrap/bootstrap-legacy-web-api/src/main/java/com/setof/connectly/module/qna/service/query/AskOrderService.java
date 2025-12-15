package com.setof.connectly.module.qna.service.query;

import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.dto.query.CreateOrderQna;
import com.setof.connectly.module.qna.entity.QnaOrder;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import com.setof.connectly.module.qna.enums.QnaType;
import com.setof.connectly.module.qna.repository.QnaOrderRepository;
import com.setof.connectly.module.qna.service.image.query.QnaImageQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AskOrderService implements AskService<CreateOrderQna> {

    private final QnaOrderRepository qnaOrderRepository;
    private final QnaImageQueryService qnaImageQueryService;

    @Override
    public QnaType getQnaType() {
        return QnaType.ORDER;
    }

    @Override
    public void doAsk(long qnaId, CreateOrderQna createQna) {
        QnaOrder qnaOrder =
                QnaOrder.builder().qnaId(qnaId).orderId(createQna.getTargetId()).build();

        if (!createQna.getQnaImages().isEmpty())
            saveQnaImages(qnaId, null, createQna.getQnaImages(), QnaIssueType.QUESTION);
        qnaOrderRepository.save(qnaOrder);
    }

    @Override
    public void doReply(long qnaId, Long qnaAnswerId, CreateOrderQna createQna) {
        if (!createQna.getQnaImages().isEmpty())
            saveQnaImages(qnaId, qnaAnswerId, createQna.getQnaImages(), QnaIssueType.ANSWER);
    }

    @Override
    public void doReplyUpdate(long qnaId, Long qnaAnswerId, CreateOrderQna createQna) {
        if (!createQna.getQnaImages().isEmpty()) {
            qnaImageQueryService.updateQnaImages(
                    qnaId, qnaAnswerId, createQna.getQnaImages(), QnaIssueType.ANSWER);
        }
    }

    @Override
    public void doAskUpdate(long qnaId, CreateOrderQna createQna) {
        if (!createQna.getQnaImages().isEmpty()) {
            qnaImageQueryService.updateQnaImages(
                    qnaId, null, createQna.getQnaImages(), QnaIssueType.QUESTION);
        }
    }

    private void saveQnaImages(
            long qnaId,
            Long qnaAnswerId,
            List<CreateQnaImage> qnaImages,
            QnaIssueType qnaIssueType) {
        qnaImageQueryService.saveQnaImages(qnaId, qnaAnswerId, qnaImages, qnaIssueType);
    }
}
