package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.port.out.command.DiscountOutboxCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountOutboxCommandManager 단위 테스트")
class DiscountOutboxCommandManagerTest {

    @InjectMocks private DiscountOutboxCommandManager sut;

    @Mock private DiscountOutboxCommandPort commandPort;

    @Nested
    @DisplayName("create() - 신규 아웃박스 생성")
    class CreateTest {

        @Test
        @DisplayName("타겟 정보로 신규 PENDING 아웃박스를 생성하여 저장한다")
        void create_ValidTarget_PersistsNewPendingOutbox() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            Instant now = Instant.now();

            // when
            sut.create(targetType, targetId, now);

            // then
            ArgumentCaptor<DiscountOutbox> captor = ArgumentCaptor.forClass(DiscountOutbox.class);
            then(commandPort).should().persist(captor.capture());

            DiscountOutbox captured = captor.getValue();
            assertThat(captured.targetType()).isEqualTo(targetType);
            assertThat(captured.targetId()).isEqualTo(targetId);
            assertThat(captured.isPending()).isTrue();
            assertThat(captured.isNew()).isTrue();
        }

        @Test
        @DisplayName("BRAND 타겟 유형으로 신규 아웃박스를 생성한다")
        void create_BrandTargetType_PersistsBrandOutbox() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;
            Instant now = Instant.now();

            // when
            sut.create(targetType, targetId, now);

            // then
            ArgumentCaptor<DiscountOutbox> captor = ArgumentCaptor.forClass(DiscountOutbox.class);
            then(commandPort).should().persist(captor.capture());

            assertThat(captor.getValue().targetType()).isEqualTo(DiscountTargetType.BRAND);
            assertThat(captor.getValue().targetId()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("persist() - 아웃박스 저장")
    class PersistTest {

        @Test
        @DisplayName("DiscountOutbox를 저장 포트에 위임한다")
        void persist_ValidOutbox_DelegatesToCommandPort() {
            // given
            DiscountOutbox outbox = DiscountDomainFixtures.publishedOutbox();

            // when
            sut.persist(outbox);

            // then
            then(commandPort).should().persist(outbox);
        }

        @Test
        @DisplayName("PENDING 상태 아웃박스도 저장 포트에 위임한다")
        void persist_PendingOutbox_DelegatesToCommandPort() {
            // given
            DiscountOutbox outbox = DiscountDomainFixtures.pendingOutbox();

            // when
            sut.persist(outbox);

            // then
            then(commandPort).should().persist(outbox);
        }
    }
}
