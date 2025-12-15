package com.setof.connectly.module.qna.repository.image.query;

import com.setof.connectly.module.qna.entity.QnaImage;

import java.util.List;

public interface QnaImageJdbcRepository {

    void saveAll(List<QnaImage> qnaImages);
    void updateAll(List<QnaImage> qnaImages);
    void deleteAll(List<Long> qnaImageIds);
}
