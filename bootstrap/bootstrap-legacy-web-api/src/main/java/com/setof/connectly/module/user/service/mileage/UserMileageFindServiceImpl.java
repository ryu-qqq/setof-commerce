package com.setof.connectly.module.user.service.mileage;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.exception.mileage.ExceedUserMileageException;
import com.setof.connectly.module.mileage.dto.filter.MileageFilter;
import com.setof.connectly.module.mileage.dto.page.MileagePage;
import com.setof.connectly.module.mileage.dto.query.PendingMileageDto;
import com.setof.connectly.module.mileage.dto.query.UserMileageQueryDto;
import com.setof.connectly.module.mileage.repository.history.fetch.MileageHistoryFindRepository;
import com.setof.connectly.module.user.dto.mileage.UserMileageDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import com.setof.connectly.module.user.mapper.mileage.UserMileageMapper;
import com.setof.connectly.module.user.repository.mileage.UserMileageFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserMileageFindServiceImpl implements UserMileageFindService {

    private final UserMileageMapper userMileageMapper;
    private final UserMileageFindRepository userMileageFindRepository;
    private final MileageHistoryFindRepository mileageHistoryFindRepository;

    @Override
    public UserMileageDto fetchUserMileage(long userId) {
        List<UserMileageQueryDto> userMileageQueries =
                userMileageFindRepository.fetchUserMileageQueryInMyPage(userId);
        // double pendingMileages= fetchUserPendingMileages(userId);
        return userMileageMapper.toMyMileages(userId, userMileageQueries, 0.0);
    }

    public double fetchUserPendingMileages(long userId) {
        List<PendingMileageDto> pendingMileages =
                userMileageFindRepository.fetchUserPendingMileages(userId);
        return pendingMileages.stream()
                .mapToDouble(PendingMileageDto::getMileageTransactionAmount)
                .sum();
    }

    @Override
    public MileagePage<UserMileageHistoryDto> fetchMyMileageHistories(
            MileageFilter filter, Pageable pageable) {
        long userId = SecurityUtils.currentUserId();
        List<UserMileageHistoryDto> userMileageHistories =
                mileageHistoryFindRepository.fetchMileageHistories(filter, userId, pageable);
        userMileageMapper.setTitle(userMileageHistories);
        JPAQuery<Long> mileageHistoryCount =
                mileageHistoryFindRepository.fetchMileageHistoryCount(filter, userId);
        Page<UserMileageHistoryDto> page =
                PageableExecutionUtils.getPage(
                        userMileageHistories, pageable, mileageHistoryCount::fetchCount);
        return userMileageMapper.toMileagePage(fetchUserMileage(userId), page);
    }

    @Override
    public void hasMileageEnough(long userId, long mileageAmount) {
        UserMileageDto userMileageDto = fetchUserMileage(userId);
        if (userMileageDto.getCurrentMileage() < mileageAmount)
            throw new ExceedUserMileageException();
    }
}
