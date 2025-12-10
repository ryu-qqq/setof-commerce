package com.ryuqq.setof.application.member.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.query.FindMemberByPhoneQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("FindMemberByPhoneQueryService")
@ExtendWith(MockitoExtension.class)
class FindMemberByPhoneQueryServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberAssembler memberAssembler;

    private FindMemberByPhoneQueryService service;

    @BeforeEach
    void setUp() {
        service = new FindMemberByPhoneQueryService(memberReadManager, memberAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("핸드폰 번호로 회원 조회 성공 시 Optional<MemberDetailResponse> 반환")
        void shouldReturnOptionalMemberDetailResponseWhenMemberFound() {
            // Given
            String phoneNumber = "01012345678";
            FindMemberByPhoneQuery query = new FindMemberByPhoneQuery(phoneNumber);
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

            when(memberReadManager.findByPhoneNumberOptional(phoneNumber))
                    .thenReturn(Optional.of(member));
            when(memberAssembler.toMemberDetailResponse(member)).thenReturn(expectedResponse);

            // When
            Optional<MemberDetailResponse> result = service.execute(query);

            // Then
            assertTrue(result.isPresent());
            MemberDetailResponse response = result.get();
            assertNotNull(response);
            assertEquals(expectedResponse.memberId(), response.memberId());
            assertEquals(expectedResponse.phoneNumber(), response.phoneNumber());
            assertEquals(expectedResponse.email(), response.email());
            assertEquals(expectedResponse.name(), response.name());

            verify(memberReadManager).findByPhoneNumberOptional(phoneNumber);
            verify(memberAssembler).toMemberDetailResponse(member);
        }

        @Test
        @DisplayName("핸드폰 번호로 회원이 없을 경우 Optional.empty 반환")
        void shouldReturnEmptyOptionalWhenMemberNotFound() {
            // Given
            String phoneNumber = "01099999999";
            FindMemberByPhoneQuery query = new FindMemberByPhoneQuery(phoneNumber);

            when(memberReadManager.findByPhoneNumberOptional(phoneNumber))
                    .thenReturn(Optional.empty());

            // When
            Optional<MemberDetailResponse> result = service.execute(query);

            // Then
            assertFalse(result.isPresent());

            verify(memberReadManager).findByPhoneNumberOptional(phoneNumber);
        }
    }
}
