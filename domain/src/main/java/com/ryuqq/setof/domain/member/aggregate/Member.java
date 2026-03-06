package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Email;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.DateOfBirth;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import java.time.Instant;

/**
 * 회원 Aggregate Root.
 *
 * <p>이커머스 회원의 프로필 정보를 관리합니다. 인증 수단(MemberAuth)은 별도 Aggregate로 분리되어 있습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Member {

    private final MemberId id;
    private final LegacyMemberId legacyMemberId;
    private MemberName memberName;
    private Email email;
    private PhoneNumber phoneNumber;
    private DateOfBirth dateOfBirth;
    private Gender gender;
    private MemberStatus status;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Member(
            MemberId id,
            LegacyMemberId legacyMemberId,
            MemberName memberName,
            Email email,
            PhoneNumber phoneNumber,
            DateOfBirth dateOfBirth,
            Gender gender,
            MemberStatus status,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.legacyMemberId = legacyMemberId;
        this.memberName = memberName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 회원 생성.
     *
     * @param id 회원 ID (UUIDv7, 외부 주입)
     * @param memberName 회원 이름
     * @param email 이메일
     * @param phoneNumber 전화번호
     * @param dateOfBirth 생년월일
     * @param gender 성별
     * @param occurredAt 생성 시각
     * @return 새 Member 인스턴스
     */
    public static Member forNew(
            MemberId id,
            MemberName memberName,
            Email email,
            PhoneNumber phoneNumber,
            DateOfBirth dateOfBirth,
            Gender gender,
            Instant occurredAt) {
        return new Member(
                id,
                null,
                memberName,
                email,
                phoneNumber,
                dateOfBirth,
                gender,
                MemberStatus.ACTIVE,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /**
     * 레거시 마이그레이션용 회원 생성.
     *
     * @param id 회원 ID (UUIDv7, 외부 주입)
     * @param legacyMemberId 레거시 user_id
     * @param memberName 회원 이름
     * @param email 이메일
     * @param phoneNumber 전화번호
     * @param dateOfBirth 생년월일
     * @param gender 성별
     * @param occurredAt 생성 시각
     * @return 마이그레이션된 Member 인스턴스
     */
    public static Member forMigration(
            MemberId id,
            LegacyMemberId legacyMemberId,
            MemberName memberName,
            Email email,
            PhoneNumber phoneNumber,
            DateOfBirth dateOfBirth,
            Gender gender,
            Instant occurredAt) {
        return new Member(
                id,
                legacyMemberId,
                memberName,
                email,
                phoneNumber,
                dateOfBirth,
                gender,
                MemberStatus.ACTIVE,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static Member reconstitute(
            MemberId id,
            LegacyMemberId legacyMemberId,
            MemberName memberName,
            Email email,
            PhoneNumber phoneNumber,
            DateOfBirth dateOfBirth,
            Gender gender,
            MemberStatus status,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new Member(
                id,
                legacyMemberId,
                memberName,
                email,
                phoneNumber,
                dateOfBirth,
                gender,
                status,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * 프로필 정보 수정.
     *
     * @param memberName 회원 이름
     * @param email 이메일
     * @param phoneNumber 전화번호
     * @param dateOfBirth 생년월일
     * @param gender 성별
     * @param occurredAt 수정 시각
     */
    public void updateProfile(
            MemberName memberName,
            Email email,
            PhoneNumber phoneNumber,
            DateOfBirth dateOfBirth,
            Gender gender,
            Instant occurredAt) {
        this.memberName = memberName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.updatedAt = occurredAt;
    }

    /** 회원 활성화. */
    public void activate(Instant occurredAt) {
        this.status = MemberStatus.ACTIVE;
        this.updatedAt = occurredAt;
    }

    /** 회원 정지. */
    public void suspend(Instant occurredAt) {
        this.status = MemberStatus.SUSPENDED;
        this.updatedAt = occurredAt;
    }

    /** 회원 탈퇴. */
    public void withdraw(Instant occurredAt) {
        this.status = MemberStatus.WITHDRAWN;
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE && !isDeleted();
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean canLogin() {
        return status.canLogin() && !isDeleted();
    }

    /** 레거시 시스템에서 마이그레이션된 회원인지 확인. */
    public boolean isMigrated() {
        return legacyMemberId != null;
    }

    // VO Getters
    public MemberId id() {
        return id;
    }

    public String idValue() {
        return id.value();
    }

    public LegacyMemberId legacyMemberId() {
        return legacyMemberId;
    }

    public Long legacyMemberIdValue() {
        return legacyMemberId != null ? legacyMemberId.value() : null;
    }

    public MemberName memberName() {
        return memberName;
    }

    public String memberNameValue() {
        return memberName.value();
    }

    public Email email() {
        return email;
    }

    public String emailValue() {
        return email.value();
    }

    public PhoneNumber phoneNumber() {
        return phoneNumber;
    }

    public String phoneNumberValue() {
        return phoneNumber.value();
    }

    public DateOfBirth dateOfBirth() {
        return dateOfBirth;
    }

    public Gender gender() {
        return gender;
    }

    public MemberStatus status() {
        return status;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
