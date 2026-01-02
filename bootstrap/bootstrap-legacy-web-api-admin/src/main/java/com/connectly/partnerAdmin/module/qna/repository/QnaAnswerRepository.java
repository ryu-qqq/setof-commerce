package com.connectly.partnerAdmin.module.qna.repository;

import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {
}
