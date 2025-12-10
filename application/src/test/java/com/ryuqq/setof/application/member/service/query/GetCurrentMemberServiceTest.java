package com.ryuqq.setof.application.member.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetCurrentMemberService")
@ExtendWith(MockitoExtension.class)
class GetCurrentMemberServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberAssembler memberAssembler;

    private GetCurrentMemberService service;

    @BeforeEach
    void setUp() {
        service = new GetCurrentMemberService(memberReadManager, memberAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("회원 ID로 조회 성공 시 MemberDetailResponse 반환")
        void shouldReturnMemberDetailResponseWhenMemberFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            GetCurrentMemberQuery query = new GetCurrentMemberQuery(memberId);
            Member member = MemberFixture.createLocalMember();
            MemberDetailResponse expectedResponse =
                    new MemberDetailResponse(
                            member.getIdValue(),
                            member.getPhoneNumberValue(),
                            member.getEmailValue(),
                            member.getNameValue(),
                            member.getDateOfBirth(),
                            member.getGender().name(),
                            member.getProvider().name(),
                            member.getStatus().name(),
                            member.getCreatedAt());

            when(memberReadManager.findById(memberId)).thenReturn(member);
            when(memberAssembler.toMemberDetailResponse(member)).thenReturn(expectedResponse);

            // When
            MemberDetailResponse result = service.execute(query);

            // Then
            assertNotNull(result);
            assertEquals(expectedResponse.memberId(), result.memberId());
            assertEquals(expectedResponse.phoneNumber(), result.phoneNumber());
            assertEquals(expectedResponse.email(), result.email());
            assertEquals(expectedResponse.name(), result.name());
            assertEquals(expectedResponse.gender(), result.gender());
            assertEquals(expectedResponse.provider(), result.provider());
            assertEquals(expectedResponse.status(), result.status());

            verify(memberReadManager).findById(memberId);
            verify(memberAssembler).toMemberDetailResponse(member);
        }

        @Test
        @DisplayName("회원 ID로 조회 실패 시 MemberNotFoundException 발생")
        void shouldThrowMemberNotFoundExceptionWhenMemberNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            GetCurrentMemberQuery query = new GetCurrentMemberQuery(memberId);

            when(memberReadManager.findById(memberId))
                    .thenThrow(new MemberNotFoundException(UUID.fromString(memberId)));

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(query));

            verify(memberReadManager).findById(memberId);
            verify(memberAssembler, never()).toMemberDetailResponse(any());
        }
    }
}
