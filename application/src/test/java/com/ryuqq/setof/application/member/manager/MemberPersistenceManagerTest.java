package com.ryuqq.setof.application.member.manager;

import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.port.out.command.MemberPersistencePort;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
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

    @Mock
    private MemberPersistencePort memberPersistencePort;

    private MemberPersistenceManager manager;

    @BeforeEach
    void setUp() {
        manager = new MemberPersistenceManager(memberPersistencePort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("LOCAL 회원 저장 성공")
        void shouldPersistLocalMember() {
            // Given
            Member localMember = MemberFixture.createLocalMember();

            // When
            manager.persist(localMember);

            // Then
            verify(memberPersistencePort).persist(localMember);
        }

        @Test
        @DisplayName("KAKAO 회원 저장 성공")
        void shouldPersistKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When
            manager.persist(kakaoMember);

            // Then
            verify(memberPersistencePort).persist(kakaoMember);
        }

        @Test
        @DisplayName("ID가 있는 회원 저장 성공")
        void shouldPersistMemberWithId() {
            // Given
            Member memberWithId = MemberFixture.createLocalMemberWithId("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            // When
            manager.persist(memberWithId);

            // Then
            verify(memberPersistencePort).persist(memberWithId);
        }

        @Test
        @DisplayName("탈퇴한 회원 저장 성공")
        void shouldPersistWithdrawnMember() {
            // Given
            Member withdrawnMember = MemberFixture.createWithdrawnMember();

            // When
            manager.persist(withdrawnMember);

            // Then
            verify(memberPersistencePort).persist(withdrawnMember);
        }
    }
}
