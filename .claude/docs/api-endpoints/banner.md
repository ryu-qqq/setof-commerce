# Banner API Endpoints

## 요약

| 분류 | 개수 |
|------|------|
| Query (조회) | 1개 |
| Command (명령) | 0개 |
| **합계** | **1개** |

---

## Query Endpoints

| # | Method | Path | Controller | Method | UseCase |
|---|--------|------|------------|--------|---------|
| 1 | GET | /api/v1/content/banner | BannerQueryV1Controller | getBanners | BannerSlideQueryUseCase |

---

### Q1. getBanners - 배너 슬라이드 목록 조회

- **Path**: `GET /api/v1/content/banner`
- **Controller**: `BannerQueryV1Controller`
- **Request**: `bannerType` (@RequestParam, Query String)
- **Response**: `V1ApiResponse<List<BannerSlideV1ApiResponse>>`
- **UseCase**: `BannerSlideQueryUseCase`

#### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| bannerType | String | ✅ | 배너 타입 (CATEGORY, MY_PAGE, CART, PRODUCT_DETAIL_DESCRIPTION, RECOMMEND, LOGIN) | `bannerType=CATEGORY` |

#### Response Fields

**BannerSlideV1ApiResponse**

| 필드 | 타입 | 설명 |
|------|------|------|
| bannerItemId | long | 배너 아이템 ID |
| title | String | 배너 제목 |
| imageUrl | String | 이미지 URL |
| linkUrl | String | 링크 URL |

#### Response Structure

```json
{
  "data": [
    {
      "bannerItemId": 1,
      "title": "신규 회원 이벤트",
      "imageUrl": "https://cdn.example.com/banner/1.jpg",
      "linkUrl": "/event/new-member"
    }
  ],
  "success": true,
  "message": null
}
```

#### 처리 흐름

```
1. BannerQueryV1Controller.getBanners()
   ↓
2. BannerType.valueOf(bannerType)
   - String → BannerType enum 변환
   ↓
3. BannerSlideQueryUseCase.fetchDisplayBannerSlides(BannerType)
   - 배너 타입별 전시 중인 슬라이드 목록 조회
   ↓
4. List<BannerSlide> 반환
   - 빈 리스트인 경우 BannerGroupNotFoundException 발생
   ↓
5. BannerV1ApiMapper.toListResponse()
   - List<BannerSlide> → List<BannerSlideV1ApiResponse> 변환
   ↓
6. ResponseEntity<V1ApiResponse<List<BannerSlideV1ApiResponse>>>
```

#### 특이사항

- **레거시 경로 호환**: 기존 `ContentController`의 `/api/v1/content/banner` 경로와 호환
- **bannerType 필수값**: `@NotBlank` 검증으로 빈 값 요청 시 400 반환
- **빈 결과 예외 처리**: 슬라이드가 없으면 `BannerGroupNotFoundException` 발생 (404 예상)
- **Domain enum 변환**: 컨트롤러 계층에서 `BannerType.valueOf()` 직접 호출 — 잘못된 타입 문자열 입력 시 `IllegalArgumentException` 발생

---

## Command Endpoints

현재 Banner 도메인에는 Command 엔드포인트가 구현되어 있지 않습니다.

---

## 아키텍처 매핑

### Hexagonal Architecture Layer 흐름

```
[Adapter-In] BannerQueryV1Controller
    ↓
[Adapter-In] BannerV1ApiMapper (Domain → Response DTO 변환)
    ↓
[Application] BannerSlideQueryUseCase (Port-In)
    ↓
[Domain] BannerSlide (Domain 객체)
```

### CQRS 패턴 적용

- **Query Side**: `BannerQueryV1Controller` - 조회 전용
- **Command Side**: 미구현
- **레거시 호환**: `/api/v1/content/banner` 경로는 기존 ContentController 경로와 동일하게 유지

---

## 문서 생성 정보

- **분석 일시**: 2026-03-14
- **대상 모듈**: `adapter-in/rest-api` (v1)
- **대상 패키지**: `com.ryuqq.setof.adapter.in.rest.v1.banner`
- **컨트롤러 파일**: `BannerQueryV1Controller.java`
- **엔드포인트 Base**: `/api/v1/content/banner`
