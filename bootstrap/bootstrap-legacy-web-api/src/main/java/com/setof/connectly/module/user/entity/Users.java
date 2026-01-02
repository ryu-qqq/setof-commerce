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
@Table(name = "users")
@Entity
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_pk_id")
    private String socialPkId;

    @Column(name = "user_grade_id")
    private long userGradeId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "social_login_type")
    @Enumerated(value = EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "privacy_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn privacyConsent;

    @Column(name = "service_terms_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn serviceTermsConsent;

    @Column(name = "ad_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn adConsent;

    @Column(name = "withdrawal_yn")
    @Enumerated(value = EnumType.STRING)
    private Yn withdrawalYn;

    @Column(name = "withdrawal_reason")
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
