package com.ryuqq.setof.application.member.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberAlreadyRegisteredException;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
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
@DisplayName("MemberValidator 단위 테스트")
class MemberValidatorTest {

    @InjectMocks private MemberValidator sut;

    @Mock private MemberReadManager memberReadManager;

    @Nested
    @DisplayName("validateNotRegistered() - 중복 가입 검증")
    class ValidateNotRegisteredTest {

        @Test
        @DisplayName("미가입 전화번호면 예외가 발생하지 않는다")
        void validateNotRegistered_NewPhone_NoException() {
            // given
            String phoneNumber = "01099999999";

            given(memberReadManager.existsByPhoneNumber(phoneNumber)).willReturn(false);

            // when & then (예외 없이 정상 실행)
            sut.validateNotRegistered(phoneNumber);

            then(memberReadManager).should().existsByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("이미 가입된 전화번호면 MemberAlreadyRegisteredException이 발생한다")
        void validateNotRegistered_ExistingPhone_ThrowsMemberAlreadyRegisteredException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;

            given(memberReadManager.existsByPhoneNumber(phoneNumber)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validateNotRegistered(phoneNumber))
                    .isInstanceOf(MemberAlreadyRegisteredException.class);

            then(memberReadManager).should().existsByPhoneNumber(phoneNumber);
        }
    }

    @Nested
    @DisplayName("getByPhoneNumber() - 전화번호로 회원 조회")
    class GetByPhoneNumberTest {

        @Test
        @DisplayName("가입된 전화번호면 회원 도메인 객체를 반환한다")
        void getByPhoneNumber_ExistingPhone_ReturnsMember() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            Member expected = MemberFixtures.activeMember();

            given(memberReadManager.findByPhoneNumber(phoneNumber))
                    .willReturn(Optional.of(expected));

            // when
            Member result = sut.getByPhoneNumber(phoneNumber);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberReadManager).should().findByPhoneNumber(phoneNumber);
        }

        @Test
        @DisplayName("미가입 전화번호면 MemberNotFoundException이 발생한다")
        void getByPhoneNumber_NonExistingPhone_ThrowsMemberNotFoundException() {
            // given
            String phoneNumber = "01099999999";

            given(memberReadManager.findByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getByPhoneNumber(phoneNumber))
                    .isInstanceOf(MemberNotFoundException.class);

            then(memberReadManager).should().findByPhoneNumber(phoneNumber);
        }
    }

    @Nested
    @DisplayName("getById() - 회원 ID로 회원 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 memberId면 회원 도메인 객체를 반환한다")
        void getById_ExistingUser_ReturnsMember() {
            // given
            long memberId = MemberFixtures.DEFAULT_MEMBER_ID;
            Member expected = MemberFixtures.activeMember();

            given(memberReadManager.getById(memberId)).willReturn(expected);

            // when
            Member result = sut.getById(memberId);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberReadManager).should().getById(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 memberId면 MemberNotFoundException이 전파된다")
        void getById_NonExistingUser_PropagatesMemberNotFoundException() {
            // given
            long memberId = 99999L;

            given(memberReadManager.getById(memberId))
                    .willThrow(new MemberNotFoundException(String.valueOf(memberId)));

            // when & then
            assertThatThrownBy(() -> sut.getById(memberId))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}
