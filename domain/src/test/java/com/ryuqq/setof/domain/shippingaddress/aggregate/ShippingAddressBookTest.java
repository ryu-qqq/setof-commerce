package com.ryuqq.setof.domain.shippingaddress.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressLimitExceededException;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import com.setof.commerce.domain.shippingaddress.ShippingAddressFixtures;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressBook 도메인 VO 단위 테스트")
class ShippingAddressBookTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("배송지 목록으로 ShippingAddressBook을 생성한다")
        void createWithAddressList() {
            // given
            List<ShippingAddress> addresses =
                    List.of(
                            ShippingAddressFixtures.activeShippingAddress(1L),
                            ShippingAddressFixtures.activeShippingAddress(2L));

            // when
            ShippingAddressBook book = ShippingAddressBook.of(addresses);

            // then
            assertThat(book.size()).isEqualTo(2);
            assertThat(book.all()).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록으로 ShippingAddressBook을 생성한다")
        void createWithEmptyList() {
            // when
            ShippingAddressBook book = ShippingAddressFixtures.emptyBook();

            // then
            assertThat(book.size()).isEqualTo(0);
            assertThat(book.all()).isEmpty();
        }
    }

    @Nested
    @DisplayName("validateCanAdd() - 배송지 추가 가능 여부 검증 (MAX_SIZE = 5)")
    class ValidateCanAddTest {

        @Test
        @DisplayName("배송지가 4개일 때 추가 가능하다")
        void canAddWhenSizeIsBelowMax() {
            // given
            List<ShippingAddress> addresses =
                    new ArrayList<>(
                            List.of(
                                    ShippingAddressFixtures.activeShippingAddress(1L),
                                    ShippingAddressFixtures.activeShippingAddress(2L),
                                    ShippingAddressFixtures.activeShippingAddress(3L),
                                    ShippingAddressFixtures.activeShippingAddress(4L)));
            ShippingAddressBook book = ShippingAddressBook.of(addresses);

            // when & then
            assertThat(book.size()).isEqualTo(4);
            book.validateCanAdd(); // 예외 없이 통과해야 함
        }

        @Test
        @DisplayName("배송지가 0개일 때 추가 가능하다")
        void canAddWhenEmpty() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.emptyBook();

            // when & then
            book.validateCanAdd(); // 예외 없이 통과해야 함
        }

        @Test
        @DisplayName("배송지가 최대 5개이면 추가 시 예외가 발생한다")
        void throwsExceptionWhenAtMaxSize() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.fullBook();
            assertThat(book.size()).isEqualTo(5);

            // when & then
            assertThatThrownBy(book::validateCanAdd)
                    .isInstanceOf(ShippingAddressLimitExceededException.class);
        }

        @Test
        @DisplayName("배송지 5개 초과 시 ShippingAddressLimitExceededException의 에러 코드를 확인한다")
        void limitExceededExceptionHasCorrectErrorCode() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.fullBook();

            // when & then
            assertThatThrownBy(book::validateCanAdd)
                    .isInstanceOf(ShippingAddressLimitExceededException.class)
                    .satisfies(
                            e -> {
                                ShippingAddressLimitExceededException ex =
                                        (ShippingAddressLimitExceededException) e;
                                assertThat(ex.code()).isEqualTo("SHP-003");
                                assertThat(ex.httpStatus()).isEqualTo(400);
                            });
        }
    }

    @Nested
    @DisplayName("findById() - ID로 배송지 조회")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 배송지를 조회한다")
        void findExistingAddressById() {
            // given
            ShippingAddress target = ShippingAddressFixtures.activeShippingAddress(10L);
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(
                                    List.of(
                                            ShippingAddressFixtures.activeShippingAddress(9L),
                                            target,
                                            ShippingAddressFixtures.activeShippingAddress(11L))));

            // when
            ShippingAddress found = book.findById(10L);

            // then
            assertThat(found.idValue()).isEqualTo(10L);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ShippingAddressNotFoundException이 발생한다")
        void throwsExceptionWhenIdNotFound() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.bookWithOneAddress();

            // when & then
            assertThatThrownBy(() -> book.findById(999L))
                    .isInstanceOf(ShippingAddressNotFoundException.class);
        }

        @Test
        @DisplayName("빈 목록에서 조회하면 ShippingAddressNotFoundException이 발생한다")
        void throwsExceptionWhenBookIsEmpty() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.emptyBook();

            // when & then
            assertThatThrownBy(() -> book.findById(1L))
                    .isInstanceOf(ShippingAddressNotFoundException.class)
                    .satisfies(
                            e -> {
                                ShippingAddressNotFoundException ex =
                                        (ShippingAddressNotFoundException) e;
                                assertThat(ex.code()).isEqualTo("SHP-001");
                                assertThat(ex.httpStatus()).isEqualTo(404);
                            });
        }
    }

    @Nested
    @DisplayName("unmarkDefaults() - 기본 배송지 일괄 해제")
    class UnmarkDefaultsTest {

        @Test
        @DisplayName("기본 배송지가 있으면 해제하고 변경된 목록을 반환한다")
        void unmarksDefaultAndReturnsChangedList() {
            // given
            ShippingAddress defaultAddr = ShippingAddressFixtures.defaultShippingAddress(1L);
            ShippingAddress normalAddr = ShippingAddressFixtures.activeShippingAddress(2L);
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(List.of(defaultAddr, normalAddr)));
            Instant now = CommonVoFixtures.now();

            // when
            List<ShippingAddress> changed = book.unmarkDefaults(now);

            // then
            assertThat(changed).hasSize(1);
            assertThat(changed.get(0).idValue()).isEqualTo(1L);
            assertThat(changed.get(0).isDefault()).isFalse();
            assertThat(changed.get(0).updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기본 배송지가 없으면 빈 목록을 반환한다")
        void returnsEmptyListWhenNoDefault() {
            // given
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(
                                    List.of(
                                            ShippingAddressFixtures.activeShippingAddress(1L),
                                            ShippingAddressFixtures.activeShippingAddress(2L))));

            // when
            List<ShippingAddress> changed = book.unmarkDefaults(CommonVoFixtures.now());

            // then
            assertThat(changed).isEmpty();
        }

        @Test
        @DisplayName("여러 기본 배송지가 있으면 모두 해제하고 변경된 목록을 반환한다")
        void unmarksMultipleDefaultsAndReturnsAll() {
            // given
            ShippingAddress default1 = ShippingAddressFixtures.defaultShippingAddress(1L);
            ShippingAddress default2 = ShippingAddressFixtures.defaultShippingAddress(2L);
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(List.of(default1, default2)));
            Instant now = CommonVoFixtures.now();

            // when
            List<ShippingAddress> changed = book.unmarkDefaults(now);

            // then
            assertThat(changed).hasSize(2);
            assertThat(changed).allMatch(a -> !a.isDefault());
        }

        @Test
        @DisplayName("빈 목록에서 unmarkDefaults()를 호출하면 빈 목록을 반환한다")
        void returnsEmptyListWhenBookIsEmpty() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.emptyBook();

            // when
            List<ShippingAddress> changed = book.unmarkDefaults(CommonVoFixtures.now());

            // then
            assertThat(changed).isEmpty();
        }
    }

    @Nested
    @DisplayName("reassignDefaultAfterRemove() - 삭제 후 기본 배송지 재지정")
    class ReassignDefaultAfterRemoveTest {

        @Test
        @DisplayName("삭제 후 다른 배송지를 기본 배송지로 재지정한다")
        void reassignsDefaultToAnotherAddress() {
            // given
            ShippingAddress toBeRemoved = ShippingAddressFixtures.activeShippingAddress(1L);
            ShippingAddress remaining = ShippingAddressFixtures.activeShippingAddress(2L);
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(List.of(toBeRemoved, remaining)));
            Instant now = CommonVoFixtures.now();

            // when
            Optional<ShippingAddress> reassigned = book.reassignDefaultAfterRemove(1L, now);

            // then
            assertThat(reassigned).isPresent();
            assertThat(reassigned.get().idValue()).isEqualTo(2L);
            assertThat(reassigned.get().isDefault()).isTrue();
            assertThat(reassigned.get().updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("배송지가 하나뿐인 경우 삭제 후 재지정할 배송지가 없으면 empty를 반환한다")
        void returnsEmptyWhenNoOtherAddressExists() {
            // given
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(
                                    List.of(ShippingAddressFixtures.activeShippingAddress(1L))));

            // when
            Optional<ShippingAddress> reassigned =
                    book.reassignDefaultAfterRemove(1L, CommonVoFixtures.now());

            // then
            assertThat(reassigned).isEmpty();
        }

        @Test
        @DisplayName("빈 목록에서 reassignDefaultAfterRemove()를 호출하면 empty를 반환한다")
        void returnsEmptyWhenBookIsEmpty() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.emptyBook();

            // when
            Optional<ShippingAddress> reassigned =
                    book.reassignDefaultAfterRemove(1L, CommonVoFixtures.now());

            // then
            assertThat(reassigned).isEmpty();
        }

        @Test
        @DisplayName("여러 배송지 중 삭제 대상을 제외한 첫 번째 배송지를 기본으로 재지정한다")
        void reassignsToFirstNonRemovedAddress() {
            // given
            ShippingAddress addr1 = ShippingAddressFixtures.activeShippingAddress(1L);
            ShippingAddress addr2 = ShippingAddressFixtures.activeShippingAddress(2L);
            ShippingAddress addr3 = ShippingAddressFixtures.activeShippingAddress(3L);
            ShippingAddressBook book =
                    ShippingAddressFixtures.bookWithAddresses(
                            new ArrayList<>(List.of(addr1, addr2, addr3)));
            Instant now = CommonVoFixtures.now();

            // when
            Optional<ShippingAddress> reassigned = book.reassignDefaultAfterRemove(1L, now);

            // then
            assertThat(reassigned).isPresent();
            assertThat(reassigned.get().idValue()).isEqualTo(2L);
            assertThat(reassigned.get().isDefault()).isTrue();
        }
    }

    @Nested
    @DisplayName("all() / size() - 목록 조회")
    class ListAccessTest {

        @Test
        @DisplayName("all()은 불변 목록을 반환한다")
        void allReturnsUnmodifiableList() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.bookWithOneAddress();

            // when
            List<ShippingAddress> all = book.all();

            // then
            assertThat(all).hasSize(1);
            assertThatThrownBy(() -> all.add(ShippingAddressFixtures.activeShippingAddress(99L)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("size()는 배송지 개수를 반환한다")
        void sizeReturnsCount() {
            // given
            ShippingAddressBook book = ShippingAddressFixtures.fullBook();

            // then
            assertThat(book.size()).isEqualTo(5);
        }
    }
}
