# Navigation API Endpoints

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
| 1 | GET | /api/v1/content/gnbs | NavigationQueryV1Controller | getGnbs | NavigationMenuQueryUseCase |

---

### Q1. getGnbs - GNB 메뉴 목록 조회

- **Path**: `GET /api/v1/content/gnbs`
- **Controller**: `NavigationQueryV1Controller`
- **Request**: 없음 (파라미터 없음)
- **Response**: `V1ApiResponse<List<NavigationMenuV1ApiResponse>>`
- **UseCase**: `NavigationMenuQueryUseCase`

#### Request Parameters

없음. 파라미터 없이 전시 중인 전체 GNB 메뉴 목록을 반환한다.

#### Response Fields

**NavigationMenuV1ApiResponse**

| 필드 | 타입 | 설명 |
|------|------|------|
| gnbId | long | GNB 식별자 |
| title | String | 메뉴 타이틀 |
| linkUrl | String | 이동 링크 URL |

> **비고**: `displayOrder`, `displayPeriod` 는 Redis TTL 산출 및 정렬 전용 필드로 응답에서 제외된다.

#### Response Structure

```json
{
  "data": [
    {
      "gnbId": 1,
      "title": "신상품",
      "linkUrl": "/new-arrivals"
    },
    {
      "gnbId": 2,
      "title": "베스트",
      "linkUrl": "/best"
    }
  ],
  "success": true,
  "message": null
}
```

#### 처리 흐름

```
1. NavigationQueryV1Controller.getGnbs()
   ↓
2. NavigationMenuQueryUseCase.fetchNavigationMenus()
   - 전시 중인 GNB 메뉴 목록 조회
   ↓
3. List<NavigationMenu> 반환
   ↓
4. NavigationV1ApiMapper.toListResponse()
   - List<NavigationMenu> → List<NavigationMenuV1ApiResponse> 변환
   - displayOrder, displayPeriod 필드 제외
   ↓
5. ResponseEntity<V1ApiResponse<List<NavigationMenuV1ApiResponse>>>
```

#### 특이사항

- **레거시 경로 호환**: 기존 `ContentController`의 `/api/v1/content/gnbs` 경로와 호환
- **무파라미터 전체 조회**: 별도 필터 없이 전시 중인 모든 GNB 메뉴를 반환
- **응답 필드 축소**: Domain 객체의 `displayOrder`, `displayPeriod`는 응답에서 의도적으로 제외 (내부 처리용)

---

## Command Endpoints

현재 Navigation 도메인에는 Command 엔드포인트가 구현되어 있지 않습니다.

---

## 아키텍처 매핑

### Hexagonal Architecture Layer 흐름

```
[Adapter-In] NavigationQueryV1Controller
    ↓
[Adapter-In] NavigationV1ApiMapper (Domain → Response DTO 변환)
    ↓
[Application] NavigationMenuQueryUseCase (Port-In)
    ↓
[Domain] NavigationMenu (Domain Aggregate)
```

### CQRS 패턴 적용

- **Query Side**: `NavigationQueryV1Controller` - 조회 전용
- **Command Side**: 미구현
- **레거시 호환**: `/api/v1/content/gnbs` 경로는 기존 ContentController 경로와 동일하게 유지

---

## 문서 생성 정보

- **분석 일시**: 2026-03-14
- **대상 모듈**: `adapter-in/rest-api` (v1)
- **대상 패키지**: `com.ryuqq.setof.adapter.in.rest.v1.navigation`
- **컨트롤러 파일**: `NavigationQueryV1Controller.java`
- **엔드포인트 Base**: `/api/v1/content/gnbs`
