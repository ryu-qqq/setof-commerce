package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
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

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberAssembler memberAssembler;

    private GetCurrentMemberService service;

    @BeforeEach
    void setUp() {
        service = new GetCurrentMemberService(memberReader, memberAssembler);
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
            MemberDetailResponse expectedResponse = new MemberDetailResponse(
                    member.getIdValue(),
                    member.getPhoneNumberValue(),
                    member.getEmailValue(),
                    member.getNameValue(),
                    member.getDateOfBirth(),
                    member.getGender().name(),
                    member.getProvider().name(),
                    member.getStatus().name(),
                    member.getCreatedAt());

            when(memberReader.getById(memberId)).thenReturn(member);
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

            verify(memberReader).getById(memberId);
            verify(memberAssembler).toMemberDetailResponse(member);
        }

        @Test
        @DisplayName("회원 ID로 조회 실패 시 MemberNotFoundException 발생")
        void shouldThrowMemberNotFoundExceptionWhenMemberNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            GetCurrentMemberQuery query = new GetCurrentMemberQuery(memberId);

            when(memberReader.getById(memberId)).thenThrow(new MemberNotFoundException());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(query));

            verify(memberReader).getById(memberId);
            verify(memberAssembler, never()).toMemberDetailResponse(any());
        }
    }
}
