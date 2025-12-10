package com.ryuqq.setof.adapter.in.rest.auth.paths;

/**
 * API 경로 상수 정의
 *
 * <p>
 * 모든 REST API 엔드포인트 경로를 상수로 관리합니다.
 *
 * <p>
 * 장점:
 *
 * <ul>
 * <li>컴파일 타임 검증 - 오타 방지
 * <li>IDE 자동완성/리팩토링 지원
 * <li>Controller와 Security 경로 동기화 보장
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class ApiPaths {

    public static final String API_V1 = "/api/v1";

    private ApiPaths() {
        // 인스턴스화 방지
    }

    // =====================================================
    // User BC (User Bounded Context)
    // =====================================================

    /** User 도메인 경로 */
    public static final class User {
        public static final String BASE = API_V1 + "/user";

        /**
         * [GET] 로그인 사용자 정보 조회 - Response:
         * {@link com.setof.connectly.module.sample.user.UserResponse}
         */
        public static final String ME = BASE;

        /**
         * [GET] 사용자 존재 여부 확인 - Response:
         * {@link com.setof.connectly.module.sample.user.UserExistsResponse}
         */
        public static final String EXISTS = BASE + "/exists";

        /**
         * [POST] 회원가입 - Request: {@link com.setof.connectly.module.sample.user.CreateUserRequest} -
         * Response: {@link com.setof.connectly.module.sample.user.CreateUserResponse}
         */
        public static final String JOIN = BASE + "/join";

        /**
         * [POST] 로그인 - Request: {@link com.setof.connectly.module.sample.user.LoginRequest} -
         * Response: {@link com.setof.connectly.module.sample.user.LoginResponse}
         */
        public static final String LOGIN = BASE + "/login";

        /** [POST] 로그아웃 */
        public static final String LOGOUT = BASE + "/logout";

        /**
         * [POST] 회원 탈퇴 - Request: {@link com.setof.connectly.module.sample.user.WithdrawalRequest}
         */
        public static final String WITHDRAWAL = BASE + "/withdrawl";

        /** [PATCH] 비밀번호 변경 */
        public static final String PASSWORD = BASE + "/password";

        /** 배송지 관련 */
        public static final class AddressBook {
            public static final String BASE = User.BASE + "/address-book";

            /**
             * [GET] 배송지 목록 조회 - Response:
             * {@link com.setof.connectly.module.sample.user.AddressBookListResponse}
             */
            public static final String LIST = BASE;

            /**
             * [GET] 배송지 단건 조회 - Response:
             * {@link com.setof.connectly.module.sample.user.AddressBookResponse}
             */
            public static final String DETAIL = BASE + "/{shippingAddressId}";

            /**
             * [POST] 배송지 등록 - Request:
             * {@link com.setof.connectly.module.sample.user.UserShippingInfoRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.AddressBookResponse}
             */
            public static final String CREATE = BASE;

            /**
             * [PUT] 배송지 수정 - Request:
             * {@link com.setof.connectly.module.sample.user.UserShippingInfoRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.AddressBookResponse}
             */
            public static final String UPDATE = BASE + "/{shippingAddressId}";

            /** [DELETE] 배송지 삭제 */
            public static final String DELETE = BASE + "/{shippingAddressId}";

            private AddressBook() {}
        }

        /** 환불계좌 관련 */
        public static final class RefundAccount {
            public static final String BASE = User.BASE + "/refund-account";

            /**
             * [GET] 환불계좌 조회 - Response:
             * {@link com.setof.connectly.module.sample.user.RefundAccountResponse}
             */
            public static final String GET = BASE;

            /**
             * [POST] 환불계좌 등록 - Request:
             * {@link com.setof.connectly.module.sample.user.CreateRefundAccountRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.RefundAccountResponse}
             */
            public static final String CREATE = BASE;

            /**
             * [PUT] 환불계좌 수정 - Request:
             * {@link com.setof.connectly.module.sample.user.CreateRefundAccountRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.RefundAccountResponse}
             */
            public static final String UPDATE = BASE + "/{refundAccountId}";

            /** [DELETE] 환불계좌 삭제 */
            public static final String DELETE = BASE + "/{refundAccountId}";

            private RefundAccount() {}
        }

        /** 마이페이지 관련 */
        public static final class MyPage {
            /**
             * [GET] 마이페이지 메인 - Response:
             * {@link com.setof.connectly.module.sample.user.MyPageResponse}
             */
            public static final String MAIN = User.BASE + "/my-page";

            private MyPage() {}
        }

        /** 찜(즐겨찾기) 관련 */
        public static final class Favorite {
            /**
             * [GET] 찜 목록 조회 - Filter:
             * {@link com.setof.connectly.module.sample.user.MyFavoriteFilterRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.FavoriteListResponse}
             */
            public static final String LIST = User.BASE + "/my-favorites";

            /**
             * [POST] 찜 추가 - Request:
             * {@link com.setof.connectly.module.sample.user.CreateUserFavoriteRequest} - Response:
             * {@link com.setof.connectly.module.sample.user.FavoriteResponse}
             */
            public static final String CREATE = User.BASE + "/my-favorite";

            /** [DELETE] 찜 삭제 */
            public static final String DELETE = User.BASE + "/my-favorite/{productGroupId}";

            private Favorite() {}
        }

        private User() {}
    }

    // =====================================================
    // Order BC (Order Bounded Context)
    // =====================================================

    /** Order 도메인 경로 */
    public static final class Order {
        public static final String BASE = API_V1;

        /**
         * [GET] 주문 목록 조회 - Filter:
         * {@link com.setof.connectly.module.sample.order.OrderFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.order.OrderListResponse}
         */
        public static final String LIST = BASE + "/orders";

        /**
         * [PUT] 주문 수정 (취소/환불 요청 등) - 주문 생성:
         * {@link com.setof.connectly.module.sample.order.CreateOrderRequest} - 장바구니 주문:
         * {@link com.setof.connectly.module.sample.order.CreateOrderInCartRequest} - 주문 수정:
         * {@link com.setof.connectly.module.sample.order.UpdateOrderRequest} - 클레임 요청:
         * {@link com.setof.connectly.module.sample.order.ClaimOrderRequest} - 환불 요청:
         * {@link com.setof.connectly.module.sample.order.RefundOrderRequest} - Response:
         * {@link com.setof.connectly.module.sample.order.OrderResponse}
         */
        public static final String UPDATE = BASE + "/order";

        /**
         * [GET] 주문 히스토리 조회 - Response:
         * {@link com.setof.connectly.module.sample.order.OrderHistoryResponse}
         */
        public static final String HISTORY = BASE + "/order/history/{orderId}";

        /**
         * [GET] 주문 상태별 개수 조회 - Response:
         * {@link com.setof.connectly.module.sample.order.OrderStatusCountsResponse}
         */
        public static final String STATUS_COUNTS = BASE + "/order/status-counts";

        private Order() {}
    }

    // =====================================================
    // Product BC (Product Bounded Context)
    // =====================================================

    /** Product 도메인 경로 */
    public static final class Product {
        public static final String BASE = API_V1;

        /**
         * [GET] 상품그룹 목록 조회 - Filter:
         * {@link com.setof.connectly.module.sample.product.ProductFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.product.ProductGroupListResponse}
         */
        public static final String GROUP_LIST = BASE + "/products/group";

        /**
         * [GET] 상품그룹 상세 조회 - Response:
         * {@link com.setof.connectly.module.sample.product.ProductGroupResponse}
         */
        public static final String GROUP_DETAIL = BASE + "/product/group/{productGroupId}";

        /**
         * [GET] 최근 본 상품그룹 목록 - Response:
         * {@link com.setof.connectly.module.sample.product.ProductGroupListResponse}
         */
        public static final String GROUP_RECENT = BASE + "/products/group/recent";

        /**
         * [GET] 브랜드별 상품그룹 목록 - Response:
         * {@link com.setof.connectly.module.sample.product.ProductGroupListResponse}
         */
        public static final String GROUP_BY_BRAND = BASE + "/product/group/brand/{brandId}";

        /**
         * [GET] 셀러별 상품그룹 목록 - Response:
         * {@link com.setof.connectly.module.sample.product.ProductGroupListResponse}
         */
        public static final String GROUP_BY_SELLER = BASE + "/product/group/seller/{sellerId}";

        private Product() {}
    }

    // =====================================================
    // Cart BC (Cart Bounded Context)
    // =====================================================

    /** Cart 도메인 경로 */
    public static final class Cart {
        public static final String BASE = API_V1;

        /**
         * [GET] 장바구니 개수 조회 - Filter:
         * {@link com.setof.connectly.module.sample.cart.CartFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.cart.CartCountResponse}
         */
        public static final String COUNT = BASE + "/cart-count";

        /**
         * [GET] 장바구니 목록 조회 - Filter:
         * {@link com.setof.connectly.module.sample.cart.CartFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.cart.CartListResponse}
         */
        public static final String LIST = BASE + "/carts";

        /**
         * [POST] 장바구니 추가 - Request: {@link com.setof.connectly.module.sample.cart.AddCartRequest} -
         * Response: {@link com.setof.connectly.module.sample.cart.CartResponse}
         */
        public static final String CREATE = BASE + "/cart";

        /**
         * [PUT] 장바구니 수정 - Request: {@link com.setof.connectly.module.sample.cart.AddCartRequest} -
         * Response: {@link com.setof.connectly.module.sample.cart.CartResponse}
         */
        public static final String UPDATE = BASE + "/cart/{cartId}";

        /**
         * [DELETE] 장바구니 삭제 - Request:
         * {@link com.setof.connectly.module.sample.cart.CartDeleteRequest}
         */
        public static final String DELETE = BASE + "/carts/{cartId}";

        private Cart() {}
    }

    // =====================================================
    // Payment BC (Payment Bounded Context)
    // =====================================================

    /** Payment 도메인 경로 */
    public static final class Payment {
        public static final String BASE = API_V1;

        /**
         * [GET] 결제 목록 조회 - Filter:
         * {@link com.setof.connectly.module.sample.payment.PaymentFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentListResponse}
         */
        public static final String LIST = BASE + "/payments";

        /**
         * [GET] 결제 상세 조회 - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentResponse}
         */
        public static final String DETAIL = BASE + "/payment/{paymentId}";

        /**
         * [GET] 결제 상태 조회 - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentStatusResponse}
         */
        public static final String STATUS = BASE + "/payment/{paymentId}/status";

        /**
         * [POST] 일반 결제 요청 - Request:
         * {@link com.setof.connectly.module.sample.payment.CreatePaymentRequest} - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentResponse}
         */
        public static final String PAY = BASE + "/payment";

        /**
         * [POST] 장바구니 결제 요청 - Request:
         * {@link com.setof.connectly.module.sample.payment.CreatePaymentInCartRequest} - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentResponse}
         */
        public static final String PAY_CART = BASE + "/payment/cart";

        /**
         * [POST] 마일리지 전액 결제 - Request:
         * {@link com.setof.connectly.module.sample.payment.CreatePaymentInCartRequest} - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentResponse}
         */
        public static final String PAY_MILEAGE = BASE + "/payment/mileage";

        /**
         * [POST] 결제 실패 처리 - Request:
         * {@link com.setof.connectly.module.sample.payment.FailPaymentRequest}
         */
        public static final String FAILURE = BASE + "/payment/failure";

        /**
         * [GET] 결제수단 목록 조회 - Response:
         * {@link com.setof.connectly.module.sample.payment.PaymentMethodListResponse}
         */
        public static final String METHODS = BASE + "/payment/payment-method";

        /**
         * [GET] 가상계좌 은행 목록 - Response:
         * {@link com.setof.connectly.module.sample.payment.BankListResponse}
         */
        public static final String VBANK = BASE + "/payment/vbank";

        /**
         * [GET] 환불계좌 은행 목록 - Response:
         * {@link com.setof.connectly.module.sample.payment.BankListResponse}
         */
        public static final String REFUND_BANK = BASE + "/payment/refund-bank";

        private Payment() {}
    }

    // =====================================================
    // Seller BC (Seller Bounded Context)
    // =====================================================

    /** Seller 도메인 경로 */
    public static final class Seller {
        public static final String BASE = API_V1 + "/seller";

        /** [GET] 셀러 상세 조회 - Response: {@link com.setof.connectly.module.sample.SellerResponse} */
        public static final String DETAIL = BASE + "/{sellerId}";

        private Seller() {}
    }

    // =====================================================
    // Category BC (Category Bounded Context)
    // =====================================================

    /** Category 도메인 경로 */
    public static final class Category {
        public static final String BASE = API_V1 + "/category";

        /**
         * [GET] 카테고리 트리 조회 - Response:
         * {@link com.setof.connectly.module.sample.CategoryTreeResponse}
         */
        public static final String LIST = BASE;

        private Category() {}
    }

    // =====================================================
    // Brand BC (Brand Bounded Context)
    // =====================================================

    /** Brand 도메인 경로 */
    public static final class Brand {
        public static final String BASE = API_V1;

        /**
         * [GET] 브랜드 목록 조회 - Filter:
         * {@link com.setof.connectly.module.sample.brand.BrandFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.brand.BrandListResponse}
         */
        public static final String LIST = BASE + "/brand";

        /**
         * [GET] 브랜드 상세 조회 - Response: {@link com.setof.connectly.module.sample.brand.BrandResponse}
         */
        public static final String DETAIL = BASE + "/brand/{brandId}";

        private Brand() {}
    }

    // =====================================================
    // Review BC (Review Bounded Context)
    // =====================================================

    /** Review 도메인 경로 */
    public static final class Review {
        public static final String BASE = API_V1;

        /**
         * [GET] 리뷰 목록 조회 - Response:
         * {@link com.setof.connectly.module.sample.review.ReviewListResponse}
         */
        public static final String LIST = BASE + "/reviews";

        /**
         * [POST] 리뷰 작성 - Request:
         * {@link com.setof.connectly.module.sample.review.CreateReviewRequest} - Response:
         * {@link com.setof.connectly.module.sample.review.ReviewResponse}
         */
        public static final String CREATE = BASE + "/review";

        /** [DELETE] 리뷰 삭제 */
        public static final String DELETE = BASE + "/review/{reviewId}";

        /**
         * [GET] 작성 가능한 리뷰 목록 - Response:
         * {@link com.setof.connectly.module.sample.review.AvailableReviewListResponse}
         */
        public static final String AVAILABLE = BASE + "/reviews/my-page/available";

        /**
         * [GET] 내가 작성한 리뷰 목록 - Response:
         * {@link com.setof.connectly.module.sample.review.ReviewListResponse}
         */
        public static final String WRITTEN = BASE + "/reviews/my-page/written";

        private Review() {}
    }

    // =====================================================
    // QnA BC (QnA Bounded Context)
    // =====================================================

    /** QnA 도메인 경로 */
    public static final class Qna {
        public static final String BASE = API_V1;

        /**
         * [GET] 상품별 QnA 목록 - Response:
         * {@link com.setof.connectly.module.sample.qna.QnaListResponse}
         */
        public static final String PRODUCT_LIST = BASE + "/qna/product/{productGroupId}";

        /**
         * [POST] QnA 질문 작성 - 상품 QnA:
         * {@link com.setof.connectly.module.sample.qna.CreateProductQnaRequest} - 주문 QnA:
         * {@link com.setof.connectly.module.sample.qna.CreateOrderQnaRequest} - Response:
         * {@link com.QnaV1ApiResponse.connectly.module.sample.qna.QnaResponse}
         */
        public static final String CREATE = BASE + "/qna";

        /**
         * [PUT] QnA 질문 수정 - 상품 QnA:
         * {@link com.setof.connectly.module.sample.qna.CreateProductQnaRequest} - 주문 QnA:
         * {@link com.setof.connectly.module.sample.qna.CreateOrderQnaRequest} - Response:
         * {@link com.QnaV1ApiResponse.connectly.module.sample.qna.QnaResponse}
         */
        public static final String UPDATE = BASE + "/qna/{qnaId}";

        /**
         * [POST] QnA 답변 작성 - Response:
         * {@link com.setof.connectly.module.sample.qna.QnaAnswerResponse}
         */
        public static final String REPLY = BASE + "/qna/{qnaId}/reply";

        /**
         * [PUT] QnA 답변 수정 - Response:
         * {@link com.setof.connectly.module.sample.qna.QnaAnswerResponse}
         */
        public static final String REPLY_UPDATE = BASE + "/qna/{qnaId}/reply/{qnaAnswerId}";

        /**
         * [GET] 내 QnA 목록 - Filter: {@link com.setof.connectly.module.sample.qna.QnaFilterRequest} -
         * Response: {@link com.setof.connectly.module.sample.qna.QnaListResponse}
         */
        public static final String MY_LIST = BASE + "/qna/my-page";

        private Qna() {}
    }

    // =====================================================
    // Search BC (Search Bounded Context)
    // =====================================================

    /** Search 도메인 경로 */
    public static final class Search {
        public static final String BASE = API_V1;

        /**
         * [GET] 통합 검색 - Filter:
         * {@link com.setof.connectly.module.sample.search.SearchFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.search.SearchResponse}
         */
        public static final String SEARCH = BASE + "/search";

        private Search() {}
    }

    // =====================================================
    // Image BC (Image Bounded Context)
    // =====================================================

    /** Image 도메인 경로 */
    public static final class Image {
        public static final String BASE = API_V1;

        /**
         * [POST] S3 Pre-signed URL 발급 - Request:
         * {@link com.setof.connectly.module.sample.image.PreSignedUrlRequest} - Response:
         * {@link com.setof.connectly.module.sample.image.PreSignedUrlResponse}
         */
        public static final String PRESIGNED = BASE + "/image/presigned";

        private Image() {}
    }

    // =====================================================
    // News BC (News/FAQ/Board Bounded Context)
    // =====================================================

    /** News 도메인 경로 */
    public static final class News {
        public static final String BASE = API_V1;

        /**
         * [GET] FAQ 목록 조회 - Filter: {@link com.setof.connectly.module.sample.news.FaqFilterRequest}
         * - Response: {@link com.setof.connectly.module.sample.news.FaqListResponse}
         */
        public static final String FAQ = BASE + "/faq";

        /**
         * [GET] 게시판 목록 조회 - Response:
         * {@link com.setof.connectly.module.sample.news.BoardListResponse}
         */
        public static final String BOARD = BASE + "/board";

        private News() {}
    }

    // =====================================================
    // Content (Display) BC (Content/Banner/GNB Bounded Context)
    // =====================================================

    /** Content (Display) 도메인 경로 */
    public static final class Content {
        public static final String BASE = API_V1 + "/content";

        /**
         * [GET] 현재 노출 중인 콘텐츠 - Response:
         * {@link com.setof.connectly.module.sample.display.ContentOnDisplayResponse}
         */
        public static final String ON_DISPLAY = BASE + "/on-display";

        /**
         * [GET] 콘텐츠 메타 정보 - Response:
         * {@link com.setof.connectly.module.sample.display.ContentMetaResponse}
         */
        public static final String META = BASE + "/meta/{contentId}";

        /**
         * [GET] 콘텐츠 상세 - Response:
         * {@link com.setof.connectly.module.sample.display.ContentResponse}
         */
        public static final String DETAIL = BASE + "/{contentId}";

        /**
         * [GET] 컴포넌트별 상품 목록 - Filter:
         * {@link com.setof.connectly.module.sample.display.ProductItemFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.display.ComponentProductListResponse}
         */
        public static final String COMPONENT_PRODUCTS = BASE + "/component/{componentId}/products";

        /**
         * [GET] 배너 목록 - Filter:
         * {@link com.setof.connectly.module.sample.display.BannerFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.display.BannerListResponse}
         */
        public static final String BANNER = BASE + "/banner";

        /**
         * [GET] GNB 목록 - Response:
         * {@link com.setof.connectly.module.sample.display.GnbListResponse}
         */
        public static final String GNBS = BASE + "/gnbs";

        private Content() {}
    }

    // =====================================================
    // Mileage BC (Mileage Bounded Context)
    // =====================================================

    /** Mileage 도메인 경로 */
    public static final class Mileage {
        public static final String BASE = API_V1 + "/mileage";

        /**
         * [GET] 마일리지 정보 조회 - Filter:
         * {@link com.setof.connectly.module.sample.mileage.MileageFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.mileage.MileageHistoryListResponse}
         */
        public static final String MY_PAGE = BASE + "/my-page";

        /**
         * [GET] 마일리지 내역 조회 - Filter:
         * {@link com.setof.connectly.module.sample.mileage.MileageFilterRequest} - Response:
         * {@link com.setof.connectly.module.sample.mileage.MileageHistoryListResponse}
         */
        public static final String HISTORIES = BASE + "/my-page/mileage-histories";

        private Mileage() {}
    }

    // =====================================================
    // PortOne (External Integration)
    // =====================================================

    /** PortOne 외부 연동 경로 */
    public static final class PortOne {
        public static final String BASE = API_V1;

        /**
         * [POST] 포트원 결제 웹훅 - Request:
         * {@link com.setof.connectly.module.sample.portone.PortOneWebHookRequest} - Response:
         * {@link com.setof.connectly.module.sample.portone.PortOneWebHookResponse}
         */
        public static final String WEBHOOK = BASE + "/portone/webhook";

        /**
         * 포트원 결제 확인용 Request/Response (클라이언트 사용) - Request:
         * {@link com.setof.connectly.module.sample.portone.PortOneConfirmRequest} - Response:
         * {@link com.setof.connectly.module.sample.portone.PortOneConfirmResponse}
         */
        private PortOne() {}
    }
}
