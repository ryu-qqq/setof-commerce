package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaImageCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaOrderCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
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
@DisplayName("OrderQnaPersistFacade 단위 테스트")
class OrderQnaPersistFacadeTest {

    @InjectMocks private OrderQnaPersistFacade sut;

    @Mock private QnaCommandManager qnaCommandManager;
    @Mock private QnaOrderCommandManager orderCommandManager;
    @Mock private QnaImageCommandManager imageCommandManager;

    @Nested
    @DisplayName("persist() - 주문 Q&A 번들 영속")
    class PersistTest {

        @Test
        @DisplayName("이미지 없는 번들을 영속화하고 qnaId를 반환한다")
        void persist_BundleWithoutImages_ReturnsQnaId() {
            // given
            OrderQnaBundle bundle = QnaCommandFixtures.orderQnaBundle();
            Long expectedQnaId = 2001L;

            given(qnaCommandManager.persist(bundle.qna())).willReturn(expectedQnaId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(qnaCommandManager).should().persist(bundle.qna());
            then(orderCommandManager).should().persist(any(QnaOrder.class));
            then(imageCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("이미지 있는 번들을 영속화하고 이미지도 저장한다")
        void persist_BundleWithImages_PersistsImagesAlso() {
            // given
            OrderQnaBundle bundle = QnaCommandFixtures.orderQnaBundleWithImages();
            Long expectedQnaId = 2002L;

            given(qnaCommandManager.persist(bundle.qna())).willReturn(expectedQnaId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(qnaCommandManager).should().persist(bundle.qna());
            then(orderCommandManager).should().persist(any(QnaOrder.class));
            then(imageCommandManager).should().persistAll(expectedQnaId, bundle.images());
        }
    }
}
