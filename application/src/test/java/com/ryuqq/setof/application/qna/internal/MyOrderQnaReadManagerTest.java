package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaMyOrderQueryPort;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.util.List;
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
@DisplayName("MyOrderQnaReadManager 단위 테스트")
class MyOrderQnaReadManagerTest {

    @InjectMocks private MyOrderQnaReadManager sut;

    @Mock private QnaMyOrderQueryPort queryPort;
    @Mock private QnaSearchCriteria criteria;

    @Nested
    @DisplayName("supportType() - 지원 타입 확인")
    class SupportTypeTest {

        @Test
        @DisplayName("지원 타입은 ORDER이다")
        void supportType_ReturnsOrder() {
            assertThat(sut.supportType()).isEqualTo(QnaType.ORDER);
        }
    }

    @Nested
    @DisplayName("fetchMyQnas() - 내 주문 Q&A 목록 조회")
    class FetchMyQnasTest {

        @Test
        @DisplayName("주문 Q&A 목록을 조회하여 반환한다")
        void fetchMyQnas_ValidCriteria_ReturnsMyQnaResultList() {
            // given
            List<MyQnaResult> expected = List.of(QnaQueryFixtures.myOrderQnaResult());
            given(queryPort.fetchMyOrderQnas(criteria)).willReturn(expected);

            // when
            List<MyQnaResult> result = sut.fetchMyQnas(criteria);

            // then
            assertThat(result).hasSize(expected.size());
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchMyOrderQnas(criteria);
        }

        @Test
        @DisplayName("주문 Q&A가 없으면 빈 목록을 반환한다")
        void fetchMyQnas_NoQnas_ReturnsEmptyList() {
            // given
            given(queryPort.fetchMyOrderQnas(criteria)).willReturn(List.of());

            // when
            List<MyQnaResult> result = sut.fetchMyQnas(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }
}
