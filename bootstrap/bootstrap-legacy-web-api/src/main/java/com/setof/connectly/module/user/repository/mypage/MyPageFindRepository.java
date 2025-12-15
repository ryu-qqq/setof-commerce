package com.setof.connectly.module.user.repository.mypage;

import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import java.util.Optional;

public interface MyPageFindRepository {

    Optional<MyPageResponse> fetchMyPage(long userId);
}
