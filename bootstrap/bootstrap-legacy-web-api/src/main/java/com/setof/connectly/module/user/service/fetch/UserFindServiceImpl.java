package com.setof.connectly.module.user.service.fetch;

import com.setof.connectly.module.exception.user.UserNotFoundException;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.join.IsJoinedUser;
import com.setof.connectly.module.user.dto.join.JoinedUser;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.repository.fetch.UserFindRepository;
import com.setof.connectly.module.user.service.query.UserRedisQueryService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFindServiceImpl implements UserFindService {
    private final UserFindRepository userFindRepository;
    private final UserFindRedisService userFindRedisService;
    private final UserRedisQueryService userRedisQueryService;

    @Override
    public Users fetchUserEntity(String phoneNumber) {
        return userFindRepository
                .fetchUserEntity(phoneNumber)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Users fetchUserEntity(long userId) {
        return userFindRepository.fetchUserEntity(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public JoinedUser fetchJoinedUser(IsJoinedUser isJoinedUser) {
        Optional<JoinedDto> joinedDto = fetchJoinedUserInDb(isJoinedUser.getPhoneNumber());
        return joinedDto
                .map(dto -> new JoinedUser(true, dto))
                .orElseGet(() -> new JoinedUser(false));
    }

    @Override
    public Optional<JoinedDto> isJoinedUserByEmail(String email) {
        Optional<JoinedDto> joinedUser = userFindRedisService.isJoinedUser(email);
        if (joinedUser.isPresent()) return joinedUser;
        return fetchJoinedEmailUserInDb(email);
    }

    @Override
    public Optional<JoinedDto> isJoinedUser(String phoneNumber) {
        Optional<JoinedDto> joinedUser = userFindRedisService.isJoinedUser(phoneNumber);
        if (joinedUser.isPresent()) return joinedUser;
        return fetchJoinedUserInDb(phoneNumber);
    }

    @Override
    public UserDto fetchUser(long userId) {
        Optional<UserDto> userDtoOptional = userFindRedisService.fetchUser(userId);
        return userDtoOptional.orElseGet(() -> fetchUserInDb(userId));
    }

    @Override
    public JoinedUser fetchJoinedUser() {
        long userId = SecurityUtils.currentUserId();
        Optional<UserDto> userDtoOptional = userFindRepository.fetchUser(userId);
        return userDtoOptional
                .map(userDto -> new JoinedUser(true, userDto.joinedDto()))
                .orElseGet(() -> new JoinedUser(false));
    }

    private UserDto fetchUserInDb(long userId) {
        Optional<UserDto> userDtoOptional = userFindRepository.fetchUser(userId);
        if (userDtoOptional.isPresent()) {
            userRedisQueryService.saveUser(userId, userDtoOptional.get());
            return userDtoOptional.get();
        }
        throw new UserNotFoundException();
    }

    private Optional<JoinedDto> fetchJoinedUserInDb(String phoneNumber) {
        Optional<JoinedDto> joinedMember = userFindRepository.isJoinedMember(phoneNumber);
        if (joinedMember.isPresent()) {
            userRedisQueryService.saveUser(joinedMember.get());
            return joinedMember;
        }
        return Optional.empty();
    }

    private Optional<JoinedDto> fetchJoinedEmailUserInDb(String email) {
        Optional<JoinedDto> joinedMember = userFindRepository.isJoinedMemberByEmail(email);
        if (joinedMember.isPresent()) {
            userRedisQueryService.saveUser(joinedMember.get());
            return joinedMember;
        }
        return Optional.empty();
    }
}
