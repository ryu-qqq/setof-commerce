package com.ryuqq.setof.application.discount.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.manager.DiscountOutboxReadManager;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
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
@DisplayName("DiscountOutboxValidator 단위 테스트")
class DiscountOutboxValidatorTest {

    @InjectMocks private DiscountOutboxValidator sut;

    @Mock private DiscountOutboxReadManager outboxReadManager;

    @Nested
    @DisplayName("canCreateOutbox() - 아웃박스 생성 가능 여부 검증")
    class CanCreateOutboxTest {

        @Test
        @DisplayName("PENDING과 PUBLISHED 아웃박스가 모두 없으면 true를 반환한다")
        void canCreateOutbox_NoPendingAndNoPublished_ReturnsTrue() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;

            given(outboxReadManager.existsPendingForTarget(targetType, targetId)).willReturn(false);
            given(outboxReadManager.existsPublishedForTarget(targetType, targetId)).willReturn(false);

            // when
            boolean result = sut.canCreateOutbox(targetType, targetId);

            // then
            assertThat(result).isTrue();
            then(outboxReadManager).should().existsPendingForTarget(targetType, targetId);
            then(outboxReadManager).should().existsPublishedForTarget(targetType, targetId);
        }

        @Test
        @DisplayName("PENDING 아웃박스가 이미 존재하면 false를 반환한다")
        void canCreateOutbox_PendingExists_ReturnsFalse() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 300L;

            given(outboxReadManager.existsPendingForTarget(targetType, targetId)).willReturn(true);

            // when
            boolean result = sut.canCreateOutbox(targetType, targetId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("PUBLISHED 아웃박스가 이미 존재하면 false를 반환한다")
        void canCreateOutbox_PublishedExists_ReturnsFalse() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 10L;

            given(outboxReadManager.existsPendingForTarget(targetType, targetId)).willReturn(false);
            given(outboxReadManager.existsPublishedForTarget(targetType, targetId)).willReturn(true);

            // when
            boolean result = sut.canCreateOutbox(targetType, targetId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("PENDING과 PUBLISHED 아웃박스가 모두 존재하면 false를 반환한다")
        void canCreateOutbox_BothPendingAndPublishedExist_ReturnsFalse() {
            // given
            DiscountTargetType targetType = DiscountTargetType.CATEGORY;
            long targetId = 5L;

            given(outboxReadManager.existsPendingForTarget(targetType, targetId)).willReturn(true);
            given(outboxReadManager.existsPublishedForTarget(targetType, targetId)).willReturn(true);

            // when
            boolean result = sut.canCreateOutbox(targetType, targetId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("CATEGORY 타겟 유형에 대해서도 중복 검증을 수행한다")
        void canCreateOutbox_CategoryTargetType_ValidatesCorrectly() {
            // given
            DiscountTargetType targetType = DiscountTargetType.CATEGORY;
            long targetId = 20L;

            given(outboxReadManager.existsPendingForTarget(targetType, targetId)).willReturn(false);
            given(outboxReadManager.existsPublishedForTarget(targetType, targetId)).willReturn(false);

            // when
            boolean result = sut.canCreateOutbox(targetType, targetId);

            // then
            assertThat(result).isTrue();
            then(outboxReadManager).should().existsPendingForTarget(targetType, targetId);
            then(outboxReadManager).should().existsPublishedForTarget(targetType, targetId);
        }
    }
}
