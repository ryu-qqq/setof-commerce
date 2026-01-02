package com.connectly.partnerAdmin.module.mileage.service;


import com.connectly.partnerAdmin.module.user.entity.UserMileage;

public interface UserMileageFetchService {
    UserMileage fetchUserMileageEntity(long userId);
}
