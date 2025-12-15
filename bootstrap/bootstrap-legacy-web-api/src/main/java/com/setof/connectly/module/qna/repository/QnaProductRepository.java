package com.setof.connectly.module.qna.repository;

import com.setof.connectly.module.qna.entity.QnaProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaProductRepository extends JpaRepository<QnaProduct, Long> {
}
