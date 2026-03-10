package com.ryuqq.setof.application.review.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.port.out.command.ReviewCommandPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
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
@DisplayName("ReviewCommandManager 단위 테스트")
class ReviewCommandManagerTest {

    @InjectMocks private ReviewCommandManager sut;

    @Mock private ReviewCommandPort commandPort;

    @Mock private Review mockReview;

    @Nested
    @DisplayName("persist() - 리뷰 저장")
    class PersistTest {

        @Test
        @DisplayName("리뷰를 저장하고 생성된 ID를 반환한다")
        void persist_ValidReview_ReturnsReviewId() {
            // given
            Long expectedId = 1L;
            given(commandPort.persist(mockReview)).willReturn(expectedId);

            // when
            Long result = sut.persist(mockReview);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(mockReview);
        }

        @Test
        @DisplayName("새 리뷰를 저장하면 할당된 ID를 반환한다")
        void persist_NewReview_ReturnsAssignedId() {
            // given
            Long assignedId = 42L;
            given(commandPort.persist(mockReview)).willReturn(assignedId);

            // when
            Long result = sut.persist(mockReview);

            // then
            assertThat(result).isEqualTo(assignedId);
            then(commandPort).should().persist(mockReview);
        }
    }
}
