package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.enums.Gender;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.WithdrawalReason;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "USERS")
@Entity
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long id;

    @Column(name = "SOCIAL_PK_ID")
    private String socialPkId;

    @Column(name = "USER_GRADE_ID")
    private long userGradeId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "SOCIAL_LOGIN_TYPE")
    @Enumerated(value = EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "GENDER")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "PRIVACY_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn privacyConsent;

    @Column(name = "SERVICE_TERMS_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn serviceTermsConsent;

    @Column(name = "AD_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn adConsent;

    @Column(name = "WITHDRAWAL_YN")
    @Enumerated(value = EnumType.STRING)
    private Yn withdrawalYn;

    @Column(name = "WITHDRAWAL_REASON")
    @Enumerated(value = EnumType.STRING)
    private WithdrawalReason withdrawalReason;

    @OneToOne(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private UserMileage userMileage;

    public void setUserMileage(UserMileage userMileage) {
        if (userMileage != null) {
            this.userMileage = userMileage;
            userMileage.setUsers(this);
        }
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEmailUser() {
        return socialLoginType.isEmailUser();
    }

    public void withdrawal(WithdrawalReason withdrawalReason) {
        setDeleteYn(Yn.Y);
        this.withdrawalYn = Yn.Y;
        this.withdrawalReason = withdrawalReason;
    }
}
