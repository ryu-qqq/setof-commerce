package com.ryuqq.setof.application.qna.dto.response;

import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;

/**
 * MyQnaSliceResult - 내 Q&A 목록 Cursor 기반 페이징 결과 DTO.
 *
 * <p>GET /api/v1/qna/my-page 응답에 사용됩니다.
 *
 * @param content 내 Q&A 항목 목록
 * @param sliceMeta 슬라이스 메타 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MyQnaSliceResult(List<MyQnaDetailResult> content, SliceMeta sliceMeta) {

    public static MyQnaSliceResult of(List<MyQnaDetailResult> content, SliceMeta sliceMeta) {
        return new MyQnaSliceResult(content, sliceMeta);
    }

    public static MyQnaSliceResult empty() {
        return new MyQnaSliceResult(List.of(), SliceMeta.empty());
    }

    public boolean hasNext() {
        return sliceMeta.hasNext();
    }
}
