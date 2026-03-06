package com.ryuqq.setof.application.productnotice.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticeCommandPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
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
@DisplayName("ProductNoticeCommandManager 단위 테스트")
class ProductNoticeCommandManagerTest {

    @InjectMocks private ProductNoticeCommandManager sut;

    @Mock private ProductNoticeCommandPort commandPort;

    @Nested
    @DisplayName("persist() - ProductNotice 저장")
    class PersistTest {

        @Test
        @DisplayName("ProductNotice를 저장하고 ID를 반환한다")
        void persist_ReturnsNoticeId() {
            // given
            ProductNotice notice = ProductNoticeFixtures.newNotice();
            Long expectedId = 1L;

            given(commandPort.persist(notice)).willReturn(expectedId);

            // when
            Long result = sut.persist(notice);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(notice);
        }

        @Test
        @DisplayName("활성 상태의 ProductNotice도 저장하고 ID를 반환한다")
        void persist_ActiveNotice_ReturnsNoticeId() {
            // given
            ProductNotice notice = ProductNoticeFixtures.activeNotice();
            Long expectedId = 1L;

            given(commandPort.persist(notice)).willReturn(expectedId);

            // when
            Long result = sut.persist(notice);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(notice);
        }
    }
}
