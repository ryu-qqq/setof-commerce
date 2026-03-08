package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.qna.aggregate.QnaImage;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * QnaImageDiff - Q&A 이미지 Diff Value Object.
 *
 * <p>기존 이미지와 신규 이미지의 차이를 나타냅니다. CartItemDiff 패턴을 따릅니다.
 *
 * <p>Q&A 이미지 수정은 전체 교체(replace) 방식입니다: 기존 이미지 전체 삭제 + 신규 이미지 전체 추가.
 *
 * @param deleted 삭제 대상 기존 이미지 목록
 * @param added 추가 대상 신규 이미지 목록
 * @param occurredAt 변경 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaImageDiff(List<QnaImage> deleted, QnaImages added, Instant occurredAt) {

    public static QnaImageDiff of(List<QnaImage> deleted, QnaImages added, Instant occurredAt) {
        return new QnaImageDiff(Collections.unmodifiableList(deleted), added, occurredAt);
    }

    public boolean hasChanges() {
        return !deleted.isEmpty() || !added.isEmpty();
    }

    public boolean hasDeleted() {
        return !deleted.isEmpty();
    }

    public boolean hasAdded() {
        return !added.isEmpty();
    }
}
