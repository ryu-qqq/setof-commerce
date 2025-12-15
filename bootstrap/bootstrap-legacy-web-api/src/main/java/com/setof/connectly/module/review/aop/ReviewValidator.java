package com.setof.connectly.module.review.aop;

import com.setof.connectly.module.exception.review.UnQualifiedReviewException;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.review.entity.Review;
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
public class ReviewValidator {
    private final OrderFindService orderFindService;

    @Pointcut(value = "execution(* com.setof.connectly.module.review.service.query.*.doReview(..))")
    public void isQualifiedReview() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.review.service.query.*.deleteReview(..))")
    public void isQualifiedDeleteReview() {}

    @AfterReturning(
            value = "isQualifiedReview()",
            returning = "review",
            argNames = "joinPoint,review")
    public void updateOrderAfterReview(JoinPoint joinPoint, Review review) {
        Order order = orderFindService.fetchOrderEntity(review.getOrderId());

        if (!isQualified(order.getOrderStatus())) throw new UnQualifiedReviewException();

        order.writeReview();
    }

    private boolean isQualified(OrderStatus orderStatus) {
        return orderStatus.isDeliveryProcessing()
                || orderStatus.isDeliveryCompleted()
                || orderStatus.isSettlementProcessing()
                || orderStatus.isSettlementCompleted();
    }

    @AfterReturning(
            value = "isQualifiedDeleteReview()",
            returning = "review",
            argNames = "joinPoint, review")
    public void updateOrderAfterReviewDelete(JoinPoint joinPoint, Review review) {
        Order order = orderFindService.fetchOrderEntity(review.getOrderId());
        order.deleteReview();
    }
}
