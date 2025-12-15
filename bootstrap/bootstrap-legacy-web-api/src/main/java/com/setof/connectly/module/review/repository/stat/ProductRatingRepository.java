package com.setof.connectly.module.review.repository.stat;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRatingStats, Long> {}
