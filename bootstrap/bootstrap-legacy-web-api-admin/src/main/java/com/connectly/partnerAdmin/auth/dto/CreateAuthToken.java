package com.connectly.partnerAdmin.auth.dto;

import org.hibernate.validator.constraints.Length;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.auth.validator.UserValidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@UserValidate
public record CreateAuthToken(

        @NotBlank(message = "userId는 필수값입니다.")
        @Length(max = 100, message = "userId는 최대 100자 이내 입니다.")
        String userId,

        @NotBlank(message = "password는 필수값입니다.")
        @Length(max = 20, message = "password는 최대 40자 이내입니다.")
        String password,

        @NotNull(message = "roleType은 필수값입니다.")
        RoleType roleType
) {}