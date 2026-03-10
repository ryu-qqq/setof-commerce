package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;
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
@DisplayName("IsExistUserService 단위 테스트")
class IsExistUserServiceTest {

    @InjectMocks private IsExistUserService sut;

    @Mock private MemberReadManager memberReadManager;

    @Nested
    @DisplayName("execute() - 회원 존재 여부 조회")
    class ExecuteTest {

        @Test
        @DisplayName("가입된 회원이면 joined=true인 IsExistUserResult를 반환한다")
        void execute_ExistingMember_ReturnsJoinedResult() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            Member member = MemberFixtures.activeMigratedMember();
            MemberProfile profile = MemberQueryFixtures.memberProfile(member);

            given(memberReadManager.findByPhoneNumber(phoneNumber)).willReturn(Optional.of(member));
            given(memberReadManager.getProfileByLegacyId(member.legacyMemberIdValue()))
                    .willReturn(profile);

            // when
            IsExistUserResult result = sut.execute(phoneNumber);

            // then
            assertThat(result.joined()).isTrue();
            assertThat(result.userId()).isEqualTo(member.legacyMemberIdValue());
            assertThat(result.name()).isEqualTo(member.memberNameValue());
            then(memberReadManager).should().findByPhoneNumber(phoneNumber);
            then(memberReadManager).should().getProfileByLegacyId(member.legacyMemberIdValue());
        }

        @Test
        @DisplayName("미가입 전화번호면 joined=false인 IsExistUserResult를 반환한다")
        void execute_NonExistingMember_ReturnsNotJoinedResult() {
            // given
            String phoneNumber = "01099999999";

            given(memberReadManager.findByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

            // when
            IsExistUserResult result = sut.execute(phoneNumber);

            // then
            assertThat(result.joined()).isFalse();
            assertThat(result.userId()).isNull();
            assertThat(result.name()).isNull();
            then(memberReadManager).should().findByPhoneNumber(phoneNumber);
            then(memberReadManager).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("미가입이면 프로필 조회를 하지 않는다")
        void execute_NonExistingMember_DoesNotFetchProfile() {
            // given
            String phoneNumber = "01088888888";

            given(memberReadManager.findByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

            // when
            sut.execute(phoneNumber);

            // then
            then(memberReadManager).should().findByPhoneNumber(phoneNumber);
            then(memberReadManager).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("가입된 회원의 이름이 결과에 포함된다")
        void execute_ExistingMember_ReturnsCorrectName() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            Member member = MemberFixtures.activeMigratedMember();
            MemberProfile profile = MemberQueryFixtures.memberProfile(member);

            given(memberReadManager.findByPhoneNumber(phoneNumber)).willReturn(Optional.of(member));
            given(memberReadManager.getProfileByLegacyId(member.legacyMemberIdValue()))
                    .willReturn(profile);

            // when
            IsExistUserResult result = sut.execute(phoneNumber);

            // then
            assertThat(result.name()).isEqualTo(member.memberNameValue());
        }
    }
}
