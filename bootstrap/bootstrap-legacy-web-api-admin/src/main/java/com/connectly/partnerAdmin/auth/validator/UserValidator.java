package com.connectly.partnerAdmin.auth.validator;

import com.connectly.partnerAdmin.auth.dto.CreateAuthToken;
import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.auth.exception.AuthUserNotFoundException;
import com.connectly.partnerAdmin.auth.exception.InvalidPasswordException;
import com.connectly.partnerAdmin.auth.exception.InvalidRoleTypeException;
import com.connectly.partnerAdmin.auth.service.CustomUserDetailsService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<UserValidate, CreateAuthToken> {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordChecker passwordChecker;
    private final RoleChecker roleChecker;

    @Override
    public boolean isValid(CreateAuthToken createAuthToken, ConstraintValidatorContext context) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(createAuthToken.userId());

            RoleType roleType = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(RoleType::of)
                    .orElse(RoleType.SELLER);


            boolean isPasswordEqual = passwordChecker.checkPassword(createAuthToken.password(), userDetails.getPassword());
            boolean isRoleTypeEqual = roleChecker.checkRoleType(createAuthToken.roleType(), roleType);

            if (!isPasswordEqual) {
                throw new InvalidPasswordException();

            }

            if (!isRoleTypeEqual) {
                throw new InvalidRoleTypeException();
            }

            return true;
        } catch (UsernameNotFoundException e) {
            throw new AuthUserNotFoundException();
        }
    }

}
