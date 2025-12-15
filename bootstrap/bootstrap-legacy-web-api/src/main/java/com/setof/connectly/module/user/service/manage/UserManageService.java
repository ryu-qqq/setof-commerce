package com.setof.connectly.module.user.service.manage;

import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.account.WithdrawalDto;
import com.setof.connectly.module.user.dto.join.CreateUser;
import com.setof.connectly.module.user.dto.join.LoginUser;
import com.setof.connectly.module.user.entity.Users;
import jakarta.servlet.http.HttpServletResponse;

public interface UserManageService {

    TokenDto joinUser(CreateUser createUser, HttpServletResponse response);

    TokenDto loginUser(LoginUser loginUser, HttpServletResponse response);

    UserDto resetPassword(LoginUser loginUser);

    Users withdrawal(WithdrawalDto withdrawal, HttpServletResponse response);
}
