package com.ryuqq.setof.application.member.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberCommandManager 단위 테스트")
class MemberCommandManagerTest {

    @InjectMocks private MemberCommandManager sut;

    @Mock private MemberCommandPort memberCommandPort;

    @Nested
    @DisplayName("persist(Member) - 회원 저장")
    class PersistMemberTest {

        @Test
        @DisplayName("회원을 저장하고 생성된 PK를 반환한다")
        void persist_Member_DelegatesAndReturnsId() {
            // given
            Member member = MemberFixtures.newMember();
            Long expectedId = 1001L;

            given(memberCommandPort.persist(member)).willReturn(expectedId);

            // when
            Long result = sut.persist(member);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(memberCommandPort).should().persist(member);
        }
    }
}
