package com.connectly.partnerAdmin.auth.validator;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordCheckerImpl implements PasswordChecker {

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean checkPassword(String password, String confirmPassword) {
        return passwordEncoder.matches(password, confirmPassword);
    }

}

