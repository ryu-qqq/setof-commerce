package com.setof.connectly.module.exception.review;

import org.springframework.http.HttpStatus;

public class AlreadyReviewWrittenException extends ReviewException {

    public static final String CODE = "REVIEW-400";
    public static final String MESSAGE = "이미 리뷰를 작성한 주문 입니다.";

    public AlreadyReviewWrittenException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
