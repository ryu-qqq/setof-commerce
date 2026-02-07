package com.ryuqq.setof.storage.legacy.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LegacyUserEntity - 레거시 사용자 엔티티.
 *
 * <p>레거시 DB의 users 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "users")
public class LegacyUserEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_pk_id")
    private String socialPkId;

    @Column(name = "user_grade_id")
    private long userGradeId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "social_login_type")
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyUserEntity() {}

    public Long getId() {
        return id;
    }

    public String getSocialPkId() {
        return socialPkId;
    }

    public long getUserGradeId() {
        return userGradeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SocialLoginType getSocialLoginType() {
        return socialLoginType;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** SocialLoginType - 소셜 로그인 타입. */
    public enum SocialLoginType {
        KAKAO,
        NAVER,
        GOOGLE,
        APPLE,
        EMAIL
    }

    /** Gender - 성별. */
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
