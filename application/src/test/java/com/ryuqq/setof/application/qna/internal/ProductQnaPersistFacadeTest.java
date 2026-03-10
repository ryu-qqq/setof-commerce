package com.ryuqq.setof.application.qna.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaProductCommandManager;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
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
@DisplayName("ProductQnaPersistFacade 단위 테스트")
class ProductQnaPersistFacadeTest {

    @InjectMocks private ProductQnaPersistFacade sut;

    @Mock private QnaCommandManager qnaCommandManager;
    @Mock private QnaProductCommandManager productCommandManager;

    @Nested
    @DisplayName("persist() - 상품 Q&A 번들 영속")
    class PersistTest {

        @Test
        @DisplayName("번들을 영속화하고 qnaId를 반환한다")
        void persist_ValidBundle_ReturnsQnaId() {
            // given
            ProductQnaBundle bundle = QnaCommandFixtures.productQnaBundle();
            Long expectedQnaId = 1001L;

            given(qnaCommandManager.persist(bundle.qna())).willReturn(expectedQnaId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(qnaCommandManager).should().persist(bundle.qna());
            then(productCommandManager).should().persist(any(QnaProduct.class));
        }

        @Test
        @DisplayName("QnaProduct에 qnaId가 설정된 후 저장된다")
        void persist_QnaProduct_IsPersistedWithQnaId() {
            // given
            ProductQnaBundle bundle = QnaCommandFixtures.productQnaBundle();
            Long expectedQnaId = 9999L;

            given(qnaCommandManager.persist(bundle.qna())).willReturn(expectedQnaId);

            // when
            sut.persist(bundle);

            // then
            then(qnaCommandManager).should().persist(bundle.qna());
            then(productCommandManager).should().persist(any(QnaProduct.class));
        }
    }
}
