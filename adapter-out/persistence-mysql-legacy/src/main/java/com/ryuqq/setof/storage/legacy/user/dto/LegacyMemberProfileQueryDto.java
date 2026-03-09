package com.ryuqq.setof.storage.legacy.user.dto;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LegacyMemberProfileQueryDto - 회원 프로필 JOIN 쿼리 결과 DTO.
 *
 * <p>users + user_grade + user_mileage JOIN 쿼리 결과를 담는 DTO입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public class LegacyMemberProfileQueryDto {

    private final long userId;
    private final String phoneNumber;
    private final String name;
    private final String email;
    private final LocalDate dateOfBirth;
    private final LegacyUserEntity.Gender gender;
    private final LegacyUserEntity.SocialLoginType socialLoginType;
    private final String socialPkId;
    private final Yn deleteYn;
    private final Yn withdrawalYn;
    private final String gradeName;
    private final double currentMileage;
    private final LocalDateTime insertDate;

    public LegacyMemberProfileQueryDto(
            long userId,
            String phoneNumber,
            String name,
            String email,
            LocalDate dateOfBirth,
            LegacyUserEntity.Gender gender,
            LegacyUserEntity.SocialLoginType socialLoginType,
            String socialPkId,
            Yn deleteYn,
            Yn withdrawalYn,
            String gradeName,
            double currentMileage,
            LocalDateTime insertDate) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.socialLoginType = socialLoginType;
        this.socialPkId = socialPkId;
        this.deleteYn = deleteYn;
        this.withdrawalYn = withdrawalYn;
        this.gradeName = gradeName;
        this.currentMileage = currentMileage;
        this.insertDate = insertDate;
    }

    public long getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LegacyUserEntity.Gender getGender() {
        return gender;
    }

    public LegacyUserEntity.SocialLoginType getSocialLoginType() {
        return socialLoginType;
    }

    public String getSocialPkId() {
        return socialPkId;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public Yn getWithdrawalYn() {
        return withdrawalYn;
    }

    public String getGradeName() {
        return gradeName;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }
}
