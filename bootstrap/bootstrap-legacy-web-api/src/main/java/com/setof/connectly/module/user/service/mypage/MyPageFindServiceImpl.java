package com.setof.connectly.module.user.service.mypage;

import com.setof.connectly.module.exception.user.UserNotFoundException;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import com.setof.connectly.module.user.mapper.UserMapper;
import com.setof.connectly.module.user.repository.mypage.MyPageFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MyPageFindServiceImpl implements MyPageFindService {

    private final OrderFindService orderFindService;
    private final MyPageFindRepository myPageFindRepository;
    private final UserMapper userMapper;

    @Override
    public MyPageResponse fetchMyPage() {
        long userId = SecurityUtils.currentUserId();
        Map<OrderStatus, Long> orderStatusLongMap =
                orderFindService.fetchCountOrdersByStatusInMyPage(
                        userId, OrderStatus.getMyPageOrderStatus());
        MyPageResponse myPageResponse =
                myPageFindRepository.fetchMyPage(userId).orElseThrow(UserNotFoundException::new);

        return userMapper.toMyPageResponse(myPageResponse, orderStatusLongMap);
    }
}
