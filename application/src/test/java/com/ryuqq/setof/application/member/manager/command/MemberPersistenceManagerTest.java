package com.ryuqq.setof.application.member.manager.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.port.out.command.MemberPersistencePort;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberPersistenceManager")
@ExtendWith(MockitoExtension.class)
class MemberPersistenceManagerTest {

    @Mock private MemberPersistencePort memberPersistencePort;

    private MemberPersistenceManager manager;

    @BeforeEach
    void setUp() {
        manager = new MemberPersistenceManager(memberPersistencePort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("LOCAL 회원 저장 성공 - MemberId 반환")
        void shouldPersistLocalMember() {
            // Given
            Member localMember = MemberFixture.createLocalMember();
            MemberId expectedId = localMember.getId();
            when(memberPersistencePort.persist(localMember)).thenReturn(expectedId);

            // When
            MemberId result = manager.persist(localMember);

            // Then
            assertThat(result).isEqualTo(expectedId);
            verify(memberPersistencePort).persist(localMember);
        }

        @Test
        @DisplayName("KAKAO 회원 저장 성공 - MemberId 반환")
        void shouldPersistKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();
            MemberId expectedId = kakaoMember.getId();
            when(memberPersistencePort.persist(kakaoMember)).thenReturn(expectedId);

            // When
            MemberId result = manager.persist(kakaoMember);

            // Then
            assertThat(result).isEqualTo(expectedId);
            verify(memberPersistencePort).persist(kakaoMember);
        }

        @Test
        @DisplayName("ID가 있는 회원 저장 성공 - MemberId 반환")
        void shouldPersistMemberWithId() {
            // Given
            Member memberWithId =
                    MemberFixture.createLocalMemberWithId("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
            MemberId expectedId = memberWithId.getId();
            when(memberPersistencePort.persist(memberWithId)).thenReturn(expectedId);

            // When
            MemberId result = manager.persist(memberWithId);

            // Then
            assertThat(result).isEqualTo(expectedId);
            verify(memberPersistencePort).persist(memberWithId);
        }

        @Test
        @DisplayName("탈퇴한 회원 저장 성공 - MemberId 반환")
        void shouldPersistWithdrawnMember() {
            // Given
            Member withdrawnMember = MemberFixture.createWithdrawnMember();
            MemberId expectedId = withdrawnMember.getId();
            when(memberPersistencePort.persist(withdrawnMember)).thenReturn(expectedId);

            // When
            MemberId result = manager.persist(withdrawnMember);

            // Then
            assertThat(result).isEqualTo(expectedId);
            verify(memberPersistencePort).persist(withdrawnMember);
        }
    }
}
