package com.setof.connectly.module.review.aop;

import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.service.stat.query.ProductRatingStatQueryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Aspect
@Component
@RequiredArgsConstructor
public class UpdateProductRatingStats {

    private final ProductRatingStatQueryService productRatingStatQueryService;

    @Pointcut(value = "execution(* com.setof.connectly.module.review.service.query.*.doReview(..))")
    public void updateProductRating() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.review.service.query.*.deleteReview(..))")
    public void rollBackProductRating() {}

    @AfterReturning(
            value = "updateProductRating()",
            returning = "review",
            argNames = "joinPoint,review")
    public void updateProductRating(JoinPoint joinPoint, Review review) {
        productRatingStatQueryService.updateRatingStats(review);
    }

    @AfterReturning(
            value = "rollBackProductRating()",
            returning = "review",
            argNames = "joinPoint,review")
    public void rollBackProductRating(JoinPoint joinPoint, Review review) {
        productRatingStatQueryService.rollBackProductRating(review);
    }
}
