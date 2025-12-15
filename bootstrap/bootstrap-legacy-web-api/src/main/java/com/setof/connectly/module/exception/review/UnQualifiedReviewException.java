package com.setof.connectly.module.exception.review;

import org.springframework.http.HttpStatus;

public class UnQualifiedReviewException extends ReviewException {

    public static final String CODE = "REVIEW-400";
    public static final String MESSAGE = "해당 주문 상태에 대해선 아직 리뷰를 작성 할 수 없습니다.";

    public UnQualifiedReviewException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
