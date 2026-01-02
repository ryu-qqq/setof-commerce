package com.setof.connectly.module.user.repository;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void updateUser(long userId, OAuth2UserInfo oAuth2User) {

        String sql =
                "UPDATE users SET social_login_type = :socialLoginType, social_pk_id = :socialPkId,"
                    + " GENDER = :gender, DATE_OF_BIRTH = :dateOfBirth, update_date = :updateDate"
                    + " WHERE user_id = :userId";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("socialLoginType", oAuth2User.getSocialLoginType().name());
        params.put("socialPkId", oAuth2User.getId());
        params.put("gender", oAuth2User.getGender().name());
        params.put("dateOfBirth", oAuth2User.getDateOfBirth());
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }
}
