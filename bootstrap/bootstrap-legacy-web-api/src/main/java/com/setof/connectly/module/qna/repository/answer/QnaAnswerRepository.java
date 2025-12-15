package com.setof.connectly.module.qna.repository.answer;

import com.setof.connectly.module.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {
}
