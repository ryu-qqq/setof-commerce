package com.setof.connectly.module.user.repository.mileage.query;

public interface UserMileageJdbcRepository {
    void updateUsingMileage(double currentUsingMileageAmount, long userId);

    void updateRefundMileage(double totalRefundMileage, long userId);
}
