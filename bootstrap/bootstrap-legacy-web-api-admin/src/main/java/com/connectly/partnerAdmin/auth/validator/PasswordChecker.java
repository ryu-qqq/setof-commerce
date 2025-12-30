package com.connectly.partnerAdmin.auth.validator;

public interface PasswordChecker {

    boolean checkPassword(String password, String confirmPassword);
}
