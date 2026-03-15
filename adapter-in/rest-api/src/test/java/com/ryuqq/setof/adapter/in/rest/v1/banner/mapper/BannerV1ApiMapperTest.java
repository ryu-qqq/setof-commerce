package com.ryuqq.setof.adapter.in.rest.v1.banner.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.banner.BannerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response.BannerSlideV1ApiResponse;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BannerV1ApiMapper 단위 테스트.
 *
 * <p>배너 Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerV1ApiMapper 단위 테스트")
class BannerV1ApiMapperTest {

    private BannerV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BannerV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("BannerSlide를 BannerSlideV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            BannerSlide slide = BannerApiFixtures.bannerSlide(1L);

            // when
            BannerSlideV1ApiResponse response = mapper.toResponse(slide);

            // then
            assertThat(response.bannerItemId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("신규 회원 이벤트");
            assertThat(response.imageUrl()).isEqualTo("https://cdn.example.com/banner/1.jpg");
            assertThat(response.linkUrl()).isEqualTo("/event/new-member");
        }

        @Test
        @DisplayName("커스텀 필드를 가진 BannerSlide를 정확히 변환한다")
        void toResponse_WithCustomFields() {
            // given
            BannerSlide slide =
                    BannerApiFixtures.bannerSlide(
                            2L, "여름 세일", "https://cdn.example.com/banner/2.jpg", "/sale/summer");

            // when
            BannerSlideV1ApiResponse response = mapper.toResponse(slide);

            // then
            assertThat(response.bannerItemId()).isEqualTo(2L);
            assertThat(response.title()).isEqualTo("여름 세일");
            assertThat(response.imageUrl()).isEqualTo("https://cdn.example.com/banner/2.jpg");
            assertThat(response.linkUrl()).isEqualTo("/sale/summer");
        }
    }

    @Nested
    @DisplayName("toListResponse")
    class ToListResponseTest {

        @Test
        @DisplayName("BannerSlide 목록을 BannerSlideV1ApiResponse 목록으로 변환한다")
        void toListResponse_Success() {
            // given
            List<BannerSlide> slides = BannerApiFixtures.bannerSlideList();

            // when
            List<BannerSlideV1ApiResponse> response = mapper.toListResponse(slides);

            // then
            assertThat(response).hasSize(2);
            assertThat(response.get(0).bannerItemId()).isEqualTo(1L);
            assertThat(response.get(0).title()).isEqualTo("신규 회원 이벤트");
            assertThat(response.get(1).bannerItemId()).isEqualTo(2L);
            assertThat(response.get(1).title()).isEqualTo("여름 세일");
        }

        @Test
        @DisplayName("빈 목록을 빈 응답으로 변환한다")
        void toListResponse_Empty() {
            // given
            List<BannerSlide> slides = List.of();

            // when
            List<BannerSlideV1ApiResponse> response = mapper.toListResponse(slides);

            // then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("단일 슬라이드 목록을 변환한다")
        void toListResponse_SingleItem() {
            // given
            List<BannerSlide> slides = List.of(BannerApiFixtures.bannerSlide(1L));

            // when
            List<BannerSlideV1ApiResponse> response = mapper.toListResponse(slides);

            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).bannerItemId()).isEqualTo(1L);
        }
    }
}
