package com.setof.connectly.module.exception.review;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ReviewException {

    public static final String CODE = "REVIEW-404";
    public static final String MESSAGE = "해당 리뷰가 존재하지 않습니다.";

    public ReviewNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
