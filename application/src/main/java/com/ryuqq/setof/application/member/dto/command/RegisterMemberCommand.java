package com.ryuqq.setof.application.member.dto.command;

import java.time.LocalDate;
import java.util.List;

import com.ryuqq.setof.application.member.dto.bundle.ConsentItem;

/**
 * Register Member Command
 *
 * <p>회원가입 요청 데이터를 담는 순수한 불변 객체
 *
 * @author development-team
 * @since 1.0.0
 */
public record RegisterMemberCommand(
        String phoneNumber,
        String email,
        String rawPassword,
        String name,
        LocalDate dateOfBirth,
        String gender,
        List<ConsentItem> consents) {
    /** Compact Constructor: 불변화만 */
    public RegisterMemberCommand {
        consents = (consents != null) ? List.copyOf(consents) : List.of();
    }
}
