package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.qna.exception.QnaImageLimitExceededException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QnaImages - Q&A 이미지 일급 컬렉션.
 *
 * <p>Q&A 질문에 첨부 가능한 이미지는 최대 5장입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaImages {

    private static final int MAX_IMAGE_COUNT = 5;

    private final List<QnaImage> images;

    private QnaImages(List<QnaImage> images) {
        if (images != null && images.size() > MAX_IMAGE_COUNT) {
            throw new QnaImageLimitExceededException();
        }
        this.images = new ArrayList<>(images != null ? images : List.of());
    }

    public static QnaImages of(List<QnaImage> images) {
        return new QnaImages(images);
    }

    public static QnaImages empty() {
        return new QnaImages(List.of());
    }

    /**
     * 이미지 추가.
     *
     * @param image 추가할 이미지
     * @throws QnaImageLimitExceededException 이미지 수가 최대 5장을 초과하는 경우
     */
    public void add(QnaImage image) {
        if (this.images.size() >= MAX_IMAGE_COUNT) {
            throw new QnaImageLimitExceededException();
        }
        this.images.add(image);
    }

    public List<QnaImage> toList() {
        return Collections.unmodifiableList(images);
    }

    public int size() {
        return images.size();
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }
}
