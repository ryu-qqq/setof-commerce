package com.ryuqq.setof.application.review.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.port.out.command.ReviewImageCommandPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
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
@DisplayName("ReviewImageCommandManager 단위 테스트")
class ReviewImageCommandManagerTest {

    @InjectMocks private ReviewImageCommandManager sut;

    @Mock private ReviewImageCommandPort commandPort;

    @Mock private ReviewImage mockImage;

    @Nested
    @DisplayName("persist() - 단건 리뷰 이미지 저장")
    class PersistTest {

        @Test
        @DisplayName("리뷰 이미지를 저장하고 생성된 ID를 반환한다")
        void persist_ValidImage_ReturnsImageId() {
            // given
            Long expectedId = 10L;
            given(commandPort.persist(mockImage)).willReturn(expectedId);

            // when
            Long result = sut.persist(mockImage);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(mockImage);
        }
    }

    @Nested
    @DisplayName("persistAll() - 다건 리뷰 이미지 저장")
    class PersistAllTest {

        @Test
        @DisplayName("리뷰 이미지 목록을 저장한다")
        void persistAll_ValidImages_CallsCommandPort() {
            // given
            List<ReviewImage> images = List.of(mockImage);

            // when
            sut.persistAll(images);

            // then
            then(commandPort).should().persistAll(images);
        }

        @Test
        @DisplayName("빈 목록으로 persistAll을 호출하면 commandPort에 위임한다")
        void persistAll_EmptyList_CallsCommandPort() {
            // given
            List<ReviewImage> emptyImages = List.of();

            // when
            sut.persistAll(emptyImages);

            // then
            then(commandPort).should().persistAll(emptyImages);
        }
    }

    @Nested
    @DisplayName("deleteByReviewId() - reviewId로 이미지 삭제")
    class DeleteByReviewIdTest {

        @Test
        @DisplayName("reviewId로 이미지를 삭제한다")
        void deleteByReviewId_ValidId_CallsCommandPort() {
            // given
            long reviewId = 1L;

            // when
            sut.deleteByReviewId(reviewId);

            // then
            then(commandPort).should().deleteByReviewId(reviewId);
        }
    }
}
