package com.setof.connectly.module.exception.user;

import org.springframework.http.HttpStatus;

public class UserFavoriteNotFoundException extends UserException {

    public static final String CODE = "USER_FAVORITE-404";
    public static final String MESSAGE = "찜한 상품을 찾을 수 없습니다.";

    public UserFavoriteNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
