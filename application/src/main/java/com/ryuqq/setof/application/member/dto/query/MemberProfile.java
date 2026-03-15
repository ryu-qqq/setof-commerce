package com.ryuqq.setof.application.member.dto.query;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import com.ryuqq.setof.domain.order.vo.MyPageOrderCounts;

/**
 * 회원 프로필 복합 결과.
 *
 * <p>회원 기본 정보, 마일리지 요약, 주문 상태별 건수를 포함하는 마이페이지용 복합 객체입니다.
 *
 * @param loginInfo 회원 로그인 정보 (회원 + 소셜 로그인)
 * @param mileageSummary 마일리지 요약
 * @param orderCounts 마이페이지 주문 상태별 건수
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MemberProfile(
        MemberLoginInfo loginInfo, MileageSummary mileageSummary, MyPageOrderCounts orderCounts) {

    public Member member() {
        return loginInfo.member();
    }

    public String socialLoginType() {
        return loginInfo.socialLoginType();
    }

    public String socialPkId() {
        return loginInfo.socialPkId();
    }

    public double currentMileage() {
        return mileageSummary.currentMileage();
    }
}
