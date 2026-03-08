package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.command.QnaImageCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import org.springframework.stereotype.Component;

/**
 * QnaImageCommandManager - Q&A 이미지 명령 Manager.
 *
 * <p>QnaImageCommandPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaImageCommandManager {

    private final QnaImageCommandPort imageCommandPort;

    public QnaImageCommandManager(QnaImageCommandPort imageCommandPort) {
        this.imageCommandPort = imageCommandPort;
    }

    public void persistAll(long qnaId, QnaImages images) {
        imageCommandPort.persistAll(qnaId, images);
    }

    public void deleteAllByQnaId(long qnaId) {
        imageCommandPort.deleteAllByQnaId(qnaId);
    }
}
