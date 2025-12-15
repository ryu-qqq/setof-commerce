package com.setof.connectly.module.user.service.query;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.exception.user.JoinedUserException;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.repository.UserJdbcRepository;
import com.setof.connectly.module.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UsersRepository userRepository;
    private final UserRedisQueryService userRedisQueryService;
    private final UserJdbcRepository userJdbcRepository;

    @Override
    public Users saveUser(Users users) {
        return saveAndCacheUser(users);
    }

    @Override
    public JoinedDto integrationUser(JoinedDto joinedDto, OAuth2UserInfo oAuth2User) {
        userJdbcRepository.updateUser(joinedDto.getUserId(), oAuth2User);
        joinedDto.integrationSocial(oAuth2User);
        userRedisQueryService.saveUser(joinedDto);
        return joinedDto;
    }

    private Users saveAndCacheUser(Users users) {
        try {
            Users savedUser = userRepository.save(users);
            saveUserInCache(savedUser);
            return savedUser;
        } catch (DuplicateKeyException e) {
            throw new JoinedUserException();
        }
    }

    private void saveUserInCache(Users users) {
        JoinedDto joinedDto = new JoinedDto(users);
        userRedisQueryService.saveUser(joinedDto);
    }
}
