package com.setof.connectly.module.mileage.service.query;

public interface UserMileageRedisQueryService {

    void updateUserMileageInCache(long userId, double mileageAmount);
}
