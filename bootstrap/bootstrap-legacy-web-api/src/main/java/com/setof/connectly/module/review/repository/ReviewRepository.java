package com.setof.connectly.module.review.repository;

import com.setof.connectly.module.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {}
