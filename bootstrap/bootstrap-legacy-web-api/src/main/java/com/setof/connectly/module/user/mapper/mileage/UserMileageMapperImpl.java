package com.setof.connectly.module.user.mapper.mileage;

import com.setof.connectly.module.mileage.dto.page.MileagePage;
import com.setof.connectly.module.mileage.dto.query.UserMileageQueryDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMileageMapperImpl implements UserMileageMapper {

    public static final int DEFAULT_EXPIRE_DATE = 30;
    public static final String DEFAULT_USE_MILEAGE = "주문 결제 시 사용";
    public static final String DEFAULT_EXPIRE_MILEAGE = "적립금 유효기간 만료";
    public static final String DEFAULT_REFUND_MILEAGE = "적립금 사용주문 취소";

    @Override
    public void setTitle(List<UserMileageHistoryDto> userMileageHistories) {
        userMileageHistories.forEach(
                m -> {
                    if (m.getReason().isUse()) m.setTitle(DEFAULT_USE_MILEAGE);
                    if (m.getReason().isExpired()) m.setTitle(DEFAULT_EXPIRE_MILEAGE);
                    if (m.getReason().isRefund()) m.setTitle(DEFAULT_REFUND_MILEAGE);
                });
    }

    public UserMileageDto toMyMileages(
            long userId, List<UserMileageQueryDto> mileageQueries, double pendingMileages) {
        double expiringMileageAmount = getExpiringMileageAmount(mileageQueries);
        double currentMileageAmount =
                mileageQueries.stream().mapToDouble(UserMileageQueryDto::getCurrentMileage).sum();

        double mileageAmount = currentMileageAmount - expiringMileageAmount;
        if (mileageAmount < 0) mileageAmount = 0.0;

        return new UserMileageDto(userId, mileageAmount, pendingMileages, expiringMileageAmount);
    }

    @Override
    public MileagePage<UserMileageHistoryDto> toMileagePage(
            UserMileageDto userMileage, Page<UserMileageHistoryDto> page) {

        return MileagePage.<UserMileageHistoryDto>builder()
                .content(page.getContent())
                .pageable(page.getPageable())
                .last(page.isLast())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .number(page.getNumber())
                .sort(page.getSort())
                .size(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .empty(page.isEmpty())
                .userMileage(userMileage)
                .originalPage(page)
                .build();
    }

    private double getExpiringMileageAmount(List<UserMileageQueryDto> mileageQueries) {
        LocalDateTime now = LocalDateTime.now();
        return mileageQueries.stream()
                .filter(
                        mileage ->
                                mileage.getExpirationDate() != null
                                        && mileage.getExpirationDate()
                                                .isBefore(now)) // 현재 시점 이전에 만료된 마일리지만 포함
                .mapToDouble(UserMileageQueryDto::getCurrentMileage) // 현재 남아 있는 마일리지만 합산
                .sum();
    }
}
