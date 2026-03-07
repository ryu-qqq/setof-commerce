package com.ryuqq.setof.application.seller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.manager.SellerReadManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.exception.SellerNameDuplicateException;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
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
@DisplayName("SellerValidator 단위 테스트")
class SellerValidatorTest {

    @InjectMocks private SellerValidator sut;

    @Mock private SellerReadManager sellerReadManager;

    @Nested
    @DisplayName("findExistingOrThrow() - 셀러 조회 또는 예외 발생")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 셀러를 반환한다")
        void findExistingOrThrow_ExistingSeller_ReturnsSeller() {
            // given
            SellerId sellerId = SellerId.of(1L);
            Seller expected = SellerDomainFixtures.activeSeller();

            given(sellerReadManager.getById(sellerId.value())).willReturn(expected);

            // when
            Seller result = sut.findExistingOrThrow(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerReadManager).should().getById(sellerId.value());
        }

        @Test
        @DisplayName("셀러가 존재하지 않으면 예외가 발생한다")
        void findExistingOrThrow_NonExistingSeller_ThrowsException() {
            // given
            SellerId sellerId = SellerId.of(999L);

            given(sellerReadManager.getById(sellerId.value()))
                    .willThrow(new SellerNotFoundException(sellerId.value()));

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(sellerId))
                    .isInstanceOf(SellerNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("validateSellerNameNotDuplicate() - 셀러명 중복 검사")
    class ValidateSellerNameNotDuplicateTest {

        @Test
        @DisplayName("사용 가능한 셀러명이면 예외가 발생하지 않는다")
        void validateSellerNameNotDuplicate_UniqueName_NoException() {
            // given
            String sellerName = "새로운셀러";

            given(sellerReadManager.existsBySellerName(sellerName)).willReturn(false);

            // when & then (no exception thrown)
            sut.validateSellerNameNotDuplicate(sellerName);

            then(sellerReadManager).should().existsBySellerName(sellerName);
        }

        @Test
        @DisplayName("이미 사용 중인 셀러명이면 예외가 발생한다")
        void validateSellerNameNotDuplicate_DuplicateName_ThrowsException() {
            // given
            String sellerName = "중복셀러";

            given(sellerReadManager.existsBySellerName(sellerName)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validateSellerNameNotDuplicate(sellerName))
                    .isInstanceOf(SellerNameDuplicateException.class);
        }
    }

    @Nested
    @DisplayName("validateSellerNameNotDuplicateExcluding() - 특정 셀러 제외 셀러명 중복 검사")
    class ValidateSellerNameNotDuplicateExcludingTest {

        @Test
        @DisplayName("자신을 제외하고 사용 가능한 셀러명이면 예외가 발생하지 않는다")
        void validateSellerNameNotDuplicateExcluding_UniqueNameExcludingSelf_NoException() {
            // given
            String sellerName = "수정된셀러";
            SellerId excludeId = SellerId.of(1L);

            given(sellerReadManager.existsBySellerNameExcluding(sellerName, excludeId))
                    .willReturn(false);

            // when & then (no exception thrown)
            sut.validateSellerNameNotDuplicateExcluding(sellerName, excludeId);

            then(sellerReadManager).should().existsBySellerNameExcluding(sellerName, excludeId);
        }

        @Test
        @DisplayName("자신을 제외하고 중복된 셀러명이면 예외가 발생한다")
        void validateSellerNameNotDuplicateExcluding_DuplicateNameExcludingSelf_ThrowsException() {
            // given
            String sellerName = "중복셀러";
            SellerId excludeId = SellerId.of(1L);

            given(sellerReadManager.existsBySellerNameExcluding(sellerName, excludeId))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(
                            () ->
                                    sut.validateSellerNameNotDuplicateExcluding(
                                            sellerName, excludeId))
                    .isInstanceOf(SellerNameDuplicateException.class);
        }
    }
}
