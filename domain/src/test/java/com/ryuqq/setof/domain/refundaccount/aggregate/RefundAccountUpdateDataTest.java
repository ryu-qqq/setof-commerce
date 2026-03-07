package com.ryuqq.setof.domain.refundaccount.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import com.setof.commerce.domain.refundaccount.RefundAccountFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccountUpdateData Record 단위 테스트")
class RefundAccountUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("of()로 RefundAccountUpdateData를 생성한다")
        void createWithOf() {
            // given
            RefundBankInfo bankInfo = RefundBankInfo.of("우리은행", "111222333444", "이민수");
            Instant occurredAt = CommonVoFixtures.now();

            // when
            RefundAccountUpdateData updateData = RefundAccountUpdateData.of(bankInfo, occurredAt);

            // then
            assertThat(updateData.bankInfo()).isEqualTo(bankInfo);
            assertThat(updateData.occurredAt()).isEqualTo(occurredAt);
        }

        @Test
        @DisplayName("fixtures 기반으로 기본 수정 데이터를 생성한다")
        void createDefaultUpdateDataFromFixtures() {
            // when
            RefundAccountUpdateData updateData = RefundAccountFixtures.defaultUpdateData();

            // then
            assertThat(updateData).isNotNull();
            assertThat(updateData.bankInfo().bankName()).isEqualTo("신한은행");
            assertThat(updateData.bankInfo().accountNumber()).isEqualTo("987654321098");
            assertThat(updateData.bankInfo().accountHolderName()).isEqualTo("김철수");
            assertThat(updateData.occurredAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("동일한 값으로 생성한 두 record는 동등하다")
        void sameValuesAreEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            RefundBankInfo bankInfo = RefundBankInfo.of("국민은행", "111222333444", "홍길동");

            RefundAccountUpdateData data1 = RefundAccountUpdateData.of(bankInfo, now);
            RefundAccountUpdateData data2 = RefundAccountUpdateData.of(bankInfo, now);

            // then
            assertThat(data1).isEqualTo(data2);
            assertThat(data1.hashCode()).isEqualTo(data2.hashCode());
        }

        @Test
        @DisplayName("다른 은행 정보를 가진 두 record는 동등하지 않다")
        void differentBankInfoNotEquals() {
            // given
            Instant now = CommonVoFixtures.now();

            RefundAccountUpdateData data1 =
                    RefundAccountUpdateData.of(
                            RefundBankInfo.of("국민은행", "111222333444", "홍길동"), now);
            RefundAccountUpdateData data2 =
                    RefundAccountUpdateData.of(
                            RefundBankInfo.of("신한은행", "987654321098", "김철수"), now);

            // then
            assertThat(data1).isNotEqualTo(data2);
        }
    }
}
