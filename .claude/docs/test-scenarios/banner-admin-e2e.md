# 배너 그룹 Admin E2E 통합 테스트 시나리오

**분석 일시**: 2026-03-15
**대상 도메인**: BannerGroup (배너 그룹)
**Base Path**: `/api/v2/admin/banner-groups`
**테스트 태그**: `TestTags.BANNER`, `TestTags.E2E`, `TestTags.ADMIN`

---

## 1. 입력 분석 결과

### 1.1 엔드포인트 목록

| HTTP Method | Path | 분류 | 응답 코드 | 설명 |
|-------------|------|------|-----------|------|
| POST | `/api/v2/admin/banner-groups` | Command | 201 | 배너 그룹 + 슬라이드 등록 |
| PUT | `/api/v2/admin/banner-groups/{bannerGroupId}` | Command | 204 | 배너 그룹 + 슬라이드 수정 (Diff 패턴) |
| PATCH | `/api/v2/admin/banner-groups/{bannerGroupId}/active-status` | Command | 204 | 노출 상태 변경 |
| PATCH | `/api/v2/admin/banner-groups/{bannerGroupId}/remove` | Command | 204 | 소프트 삭제 |

**총 4개** (Command 4개, Query 없음)

### 1.2 Validation 규칙 분석

#### RegisterBannerGroupApiRequest (POST)

| 필드 | 타입 | 제약 | 오류 메시지 |
|------|------|------|-------------|
| title | String | @NotBlank | "배너 그룹명은 필수입니다" |
| bannerType | String | @NotBlank | "배너 타입은 필수입니다" |
| displayStartAt | Instant | @NotNull | "노출 시작 시각은 필수입니다" |
| displayEndAt | Instant | @NotNull | "노출 종료 시각은 필수입니다" |
| active | boolean | - | 기본값 false |
| slides | List | @NotNull + @Valid | "슬라이드 목록은 필수입니다" |

#### RegisterBannerSlideApiRequest (POST slides 내부, @Valid 중첩)

| 필드 | 타입 | 제약 | 오류 메시지 |
|------|------|------|-------------|
| title | String | @NotBlank | "슬라이드 제목은 필수입니다" |
| imageUrl | String | @NotBlank | "이미지 URL은 필수입니다" |
| linkUrl | String | @NotBlank | "링크 URL은 필수입니다" |
| displayOrder | int | - | - |
| displayStartAt | Instant | @NotNull | "노출 시작 시각은 필수입니다" |
| displayEndAt | Instant | @NotNull | "노출 종료 시각은 필수입니다" |
| active | boolean | - | - |

#### UpdateBannerGroupApiRequest (PUT)

- RegisterBannerGroupApiRequest와 동일한 그룹 필드 제약
- slides 타입이 `List<UpdateBannerSlideApiRequest>`로 변경

#### UpdateBannerSlideApiRequest (PUT slides 내부)

| 필드 | 타입 | 제약 | 비고 |
|------|------|------|------|
| slideId | Long | nullable | null이면 신규 생성, 값 있으면 기존 수정 |
| title | String | @NotBlank | - |
| imageUrl | String | @NotBlank | - |
| linkUrl | String | @NotBlank | - |
| displayOrder | int | - | - |
| displayStartAt | Instant | @NotNull | - |
| displayEndAt | Instant | @NotNull | - |
| active | boolean | - | - |

#### ChangeBannerGroupStatusApiRequest (PATCH /active-status)

| 필드 | 타입 | 제약 |
|------|------|------|
| active | boolean | - |

### 1.3 Domain 핵심 로직 분석

#### BannerGroup.update() - Diff 계산 규칙

`BannerGroup.update(BannerGroupUpdateData)` 메서드가 `BannerSlideDiff`를 반환함.

| 조건 | 분류 | DB 동작 |
|------|------|---------|
| slideId == null | added | INSERT (신규 생성) |
| slideId != null AND 기존 슬라이드에 존재 | retained | UPDATE (속성 갱신) |
| 기존 슬라이드 ID가 요청에 미포함 | removed | soft delete (deletedAt 설정) |

#### BannerGroup.changeDisplayStatus() - 상태 변경

- `active` 필드 토글
- `updatedAt` 갱신
- `active = true` → `false`, `false` → `true` 모두 가능

#### BannerGroup.remove() - 소프트 삭제

- `deletionStatus = DeletionStatus.deletedAt(now)`
- `active = false` 강제 설정
- `updatedAt` 갱신

#### BannerGroupNotFoundException (404 발생 조건)

- 존재하지 않는 bannerGroupId로 PUT/PATCH 요청 시

### 1.4 Repository 및 JPA 엔티티

**테이블**: `banner_group`, `banner_slide`

**SoftDeletableEntity 구조**: `created_at`, `updated_at`, `deleted_at`

---

## 2. Command 엔드포인트 시나리오

### 2.1 POST /api/v2/admin/banner-groups - 배너 그룹 등록

#### 성공 시나리오

##### TC-C1-S01: 유효한 요청으로 배너 그룹 + 슬라이드 등록 성공
- **Priority**: P0
- **Pre-Data**: 없음
- **Request**: `POST /api/v2/admin/banner-groups`
  ```json
  {
    "title": "메인 배너 그룹",
    "bannerType": "RECOMMEND",
    "displayStartAt": "2026-01-01T00:00:00Z",
    "displayEndAt": "2026-12-31T23:59:59Z",
    "active": true,
    "slides": [
      {
        "title": "슬라이드 1",
        "imageUrl": "https://cdn.example.com/slide1.jpg",
        "linkUrl": "/event/summer",
        "displayOrder": 1,
        "displayStartAt": "2026-01-01T00:00:00Z",
        "displayEndAt": "2026-12-31T23:59:59Z",
        "active": true
      }
    ]
  }
  ```
- **Expected**:
  - HTTP Status: 201 Created
  - Response Body: `{ "data": { "bannerGroupId": N } }` (N > 0)
- **DB Verify**:
  - `banner_group` 테이블에 1건 존재 (title="메인 배너 그룹", active=true, deleted_at=null)
  - `banner_slide` 테이블에 1건 존재 (banner_group_id=N, title="슬라이드 1")

##### TC-C1-S02: 슬라이드 다건 등록 성공 (3개 슬라이드)
- **Priority**: P0
- **Pre-Data**: 없음
- **Request**: POST + slides 3개 포함
- **Expected**:
  - HTTP Status: 201 Created
  - Response Body: `data.bannerGroupId > 0`
- **DB Verify**:
  - `banner_slide` 테이블에 3건 존재 (모두 동일 banner_group_id)

##### TC-C1-S03: active=false로 비활성 상태 등록
- **Priority**: P1
- **Pre-Data**: 없음
- **Request**: POST + active=false
- **Expected**:
  - HTTP Status: 201 Created
- **DB Verify**:
  - `banner_group.active = false`

##### TC-C1-S04: slides 빈 목록으로 등록 (슬라이드 없는 그룹)
- **Priority**: P1
- **Pre-Data**: 없음
- **Request**: POST + `slides: []`
- **Expected**:
  - HTTP Status: 201 Created
- **DB Verify**:
  - `banner_group` 1건 생성
  - `banner_slide` 0건 (해당 그룹의 슬라이드 없음)
- **Note**: slides는 @NotNull이지만 빈 배열은 허용. 빈 배열 허용 여부는 실제 테스트로 확인 필요.

#### 실패 시나리오 - Validation

##### TC-C1-F01: title 누락 시 400
- **Priority**: P0
- **Request**: POST + `title: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "배너 그룹명은 필수입니다"

##### TC-C1-F02: title 빈 문자열 시 400
- **Priority**: P0
- **Request**: POST + `title: ""`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "배너 그룹명은 필수입니다"

##### TC-C1-F03: bannerType 누락 시 400
- **Priority**: P0
- **Request**: POST + `bannerType: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "배너 타입은 필수입니다"

##### TC-C1-F04: displayStartAt 누락 시 400
- **Priority**: P0
- **Request**: POST + `displayStartAt: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "노출 시작 시각은 필수입니다"

##### TC-C1-F05: displayEndAt 누락 시 400
- **Priority**: P0
- **Request**: POST + `displayEndAt: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "노출 종료 시각은 필수입니다"

##### TC-C1-F06: slides 필드 자체가 null 시 400
- **Priority**: P0
- **Request**: POST + `slides: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "슬라이드 목록은 필수입니다"

##### TC-C1-F07: slides 내부 슬라이드의 title 누락 시 400 (@Valid 중첩)
- **Priority**: P0
- **Request**: POST + slides 포함, 슬라이드 내 `title: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "슬라이드 제목은 필수입니다"

##### TC-C1-F08: slides 내부 슬라이드의 imageUrl 누락 시 400
- **Priority**: P0
- **Request**: POST + 슬라이드 내 `imageUrl: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "이미지 URL은 필수입니다"

##### TC-C1-F09: slides 내부 슬라이드의 linkUrl 누락 시 400
- **Priority**: P0
- **Request**: POST + 슬라이드 내 `linkUrl: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "링크 URL은 필수입니다"

---

### 2.2 PUT /api/v2/admin/banner-groups/{bannerGroupId} - 배너 그룹 수정 (Diff 패턴 핵심)

#### 성공 시나리오 - 그룹 정보 수정

##### TC-C2-S01: 배너 그룹 기본 정보 수정 성공
- **Priority**: P0
- **Pre-Data**:
  - 배너 그룹 1건 등록 (title="원본 제목", bannerType="RECOMMEND", active=true)
  - 슬라이드 1건 포함 (slideId = A)
- **Request**: `PUT /api/v2/admin/banner-groups/{id}`
  ```json
  {
    "title": "수정된 제목",
    "bannerType": "MAIN_BANNER",
    "displayStartAt": "2026-03-01T00:00:00Z",
    "displayEndAt": "2026-09-30T23:59:59Z",
    "active": true,
    "slides": [
      {
        "slideId": A,
        "title": "기존 슬라이드 유지",
        "imageUrl": "https://cdn.example.com/slide.jpg",
        "linkUrl": "/event",
        "displayOrder": 1,
        "displayStartAt": "2026-03-01T00:00:00Z",
        "displayEndAt": "2026-09-30T23:59:59Z",
        "active": true
      }
    ]
  }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.title = "수정된 제목"`
  - `banner_group.banner_type = "MAIN_BANNER"`
  - `banner_group.updated_at` 갱신됨

#### 성공 시나리오 - Diff 패턴 (핵심)

##### TC-C2-S02: slideId 있는 슬라이드 수정 (retained - 기존 슬라이드 업데이트)
- **Priority**: P0
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 1건 등록 (slideId = A, title="원본 슬라이드")
- **Request**: PUT + slides[0].slideId = A, slides[0].title = "수정된 슬라이드"
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_slide.id = A` → `title = "수정된 슬라이드"` (업데이트됨)
  - `banner_slide.deleted_at = null` (삭제되지 않음)
  - `banner_slide` 총 1건 (새 레코드 미생성)

##### TC-C2-S03: slideId null인 슬라이드 신규 추가 (added - 새 슬라이드 INSERT)
- **Priority**: P0
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 1건 등록 (slideId = A)
- **Request**: PUT + slides에 `slideId=A` (기존) + `slideId=null` (신규) 2개 전송
  ```json
  {
    "slides": [
      { "slideId": A, "title": "기존 슬라이드", ... },
      { "slideId": null, "title": "새 슬라이드", ... }
    ]
  }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_slide` 테이블에 bannerGroupId 기준 활성(deleted_at=null) 슬라이드 2건 존재
  - 기존 slideId=A 레코드는 유지
  - 새 레코드 1건 추가 (slideId=A와 다른 새 ID)

##### TC-C2-S04: 요청에서 제외된 슬라이드 소프트 삭제 (removed - deleted_at 설정)
- **Priority**: P0
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 2건 등록 (slideId = A, slideId = B)
- **Request**: PUT + slides에 slideId=A만 전송 (slideId=B 제외)
  ```json
  {
    "slides": [
      { "slideId": A, "title": "유지되는 슬라이드", ... }
    ]
  }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_slide.id = A` → `deleted_at = null` (유지)
  - `banner_slide.id = B` → `deleted_at != null` (소프트 삭제)
  - 활성 슬라이드(deleted_at=null) 수: 1건

##### TC-C2-S05: 혼합 Diff - 신규 추가 + 기존 수정 + 기존 삭제 동시 처리
- **Priority**: P0
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 3건 등록 (slideId = A, B, C)
- **Request**: PUT + slides에 A(수정), null(신규), B(유지) 전송 - C는 제외
  ```json
  {
    "slides": [
      { "slideId": A, "title": "A 수정됨", ... },
      { "slideId": null, "title": "신규 D", ... },
      { "slideId": B, "title": "B 유지됨", ... }
    ]
  }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - slideId=A: `title = "A 수정됨"`, `deleted_at = null`
  - slideId=B: `title = "B 유지됨"`, `deleted_at = null`
  - slideId=C: `deleted_at != null` (소프트 삭제)
  - 새 레코드 D: `title = "신규 D"`, `deleted_at = null`
  - 활성 슬라이드 총 3건 (A, B, D)

##### TC-C2-S06: 모든 슬라이드 교체 (기존 전체 삭제 + 신규 전체 추가)
- **Priority**: P1
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 2건 (slideId = A, B)
- **Request**: PUT + slides에 `slideId=null` 2개만 전송
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - slideId=A, B: 모두 `deleted_at != null` (소프트 삭제)
  - 신규 슬라이드 2건 추가 (새 ID)
  - 활성 슬라이드 2건 (모두 새 레코드)

##### TC-C2-S07: 슬라이드 전체 제거 (빈 slides 전송)
- **Priority**: P1
- **Pre-Data**:
  - 배너 그룹 1건 + 슬라이드 2건 (slideId = A, B)
- **Request**: PUT + `slides: []`
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - slideId=A, B: 모두 `deleted_at != null` (소프트 삭제)
  - 활성 슬라이드: 0건

#### 실패 시나리오

##### TC-C2-F01: 존재하지 않는 bannerGroupId로 수정 시 404
- **Priority**: P0
- **Pre-Data**: 없음 (또는 다른 ID만 존재)
- **Request**: `PUT /api/v2/admin/banner-groups/99999` + 유효한 body
- **Expected**:
  - HTTP Status: 404 Not Found

##### TC-C2-F02: title 누락 시 400
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건 + 슬라이드 1건
- **Request**: PUT + `title: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "배너 그룹명은 필수입니다"

##### TC-C2-F03: slides 필드 null 시 400
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건
- **Request**: PUT + `slides: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "슬라이드 목록은 필수입니다"

##### TC-C2-F04: 슬라이드 내부 title 누락 시 400 (@Valid 중첩)
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건 + 슬라이드 1건
- **Request**: PUT + 슬라이드 내 `title: null`
- **Expected**:
  - HTTP Status: 400 Bad Request
  - 오류 메시지: "슬라이드 제목은 필수입니다"

---

### 2.3 PATCH /api/v2/admin/banner-groups/{bannerGroupId}/active-status - 노출 상태 변경

#### 성공 시나리오

##### TC-C3-S01: active true → false 변경 성공
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건 등록 (active=true)
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/active-status`
  ```json
  { "active": false }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.active = false`
  - `banner_group.updated_at` 갱신됨

##### TC-C3-S02: active false → true 변경 성공
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건 등록 (active=false)
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/active-status`
  ```json
  { "active": true }
  ```
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.active = true`
  - `banner_group.updated_at` 갱신됨

##### TC-C3-S03: 동일 상태로 변경 (true → true) 성공 - 멱등성 확인
- **Priority**: P1
- **Pre-Data**: 배너 그룹 1건 (active=true)
- **Request**: `PATCH active-status` + `active: true`
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.active = true` (변경 없음, 정상 처리)

#### 실패 시나리오

##### TC-C3-F01: 존재하지 않는 bannerGroupId 상태 변경 시 404
- **Priority**: P0
- **Pre-Data**: 없음
- **Request**: `PATCH /api/v2/admin/banner-groups/99999/active-status`
  ```json
  { "active": false }
  ```
- **Expected**:
  - HTTP Status: 404 Not Found

##### TC-C3-F02: 소프트 삭제된 배너 그룹 상태 변경 시 404
- **Priority**: P1
- **Pre-Data**: 배너 그룹 1건 등록 후 `/remove` PATCH로 소프트 삭제
- **Request**: `PATCH {id}/active-status` + `active: true`
- **Expected**:
  - HTTP Status: 404 Not Found
  - 삭제된 리소스는 조회 불가

---

### 2.4 PATCH /api/v2/admin/banner-groups/{bannerGroupId}/remove - 배너 그룹 소프트 삭제

#### 성공 시나리오

##### TC-C4-S01: 배너 그룹 정상 소프트 삭제 성공
- **Priority**: P0
- **Pre-Data**: 배너 그룹 1건 등록 (active=true)
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/remove`
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.deleted_at != null` (소프트 삭제 처리)
  - `banner_group.active = false` (BannerGroup.remove() 로직: active 강제 false)
  - `banner_group.updated_at` 갱신됨

##### TC-C4-S02: 슬라이드가 있는 배너 그룹 소프트 삭제 - 그룹만 삭제, 슬라이드는 별도 처리
- **Priority**: P1
- **Pre-Data**: 배너 그룹 1건 + 슬라이드 2건
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/remove`
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.deleted_at != null`
  - `banner_slide` 레코드: 삭제 처리 여부는 비즈니스 로직 확인 필요 (현재 BannerGroup.remove()는 슬라이드를 별도로 소프트 삭제하지 않음)

##### TC-C4-S03: 비활성 배너 그룹 소프트 삭제 성공
- **Priority**: P1
- **Pre-Data**: 배너 그룹 1건 (active=false)
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/remove`
- **Expected**:
  - HTTP Status: 204 No Content
- **DB Verify**:
  - `banner_group.deleted_at != null`

#### 실패 시나리오

##### TC-C4-F01: 존재하지 않는 bannerGroupId 삭제 시 404
- **Priority**: P0
- **Pre-Data**: 없음
- **Request**: `PATCH /api/v2/admin/banner-groups/99999/remove`
- **Expected**:
  - HTTP Status: 404 Not Found

##### TC-C4-F02: 이미 소프트 삭제된 배너 그룹 재삭제 시도
- **Priority**: P1
- **Pre-Data**: 배너 그룹 1건 + 이미 소프트 삭제 완료 상태
- **Request**: `PATCH /api/v2/admin/banner-groups/{id}/remove`
- **Expected**:
  - HTTP Status: 404 Not Found (삭제된 리소스는 조회 불가)

---

## 3. 전체 플로우 시나리오

### 3.1 등록 → 상세 조회 불가 (배너 Query 없으므로 DB 검증으로 대체)

#### TC-FLOW-01: 등록 → 수정 → 상태 변경 → 삭제 전체 CRUD 플로우
- **Priority**: P0
- **Pre-Data**: 없음
- **Steps**:

  1. **POST /api/v2/admin/banner-groups** → 배너 그룹 + 슬라이드 2건 등록
     - 기대: 201, `data.bannerGroupId > 0`
     - 저장: bannerGroupId = X

  2. **DB 확인**: `banner_group.id = X` 존재, `banner_slide` 2건 존재

  3. **PUT /api/v2/admin/banner-groups/X** → 제목 수정 + 슬라이드 Diff (1건 수정 + 1건 신규)
     - slides: [slideId=기존1(수정), slideId=null(신규)]
     - 기대: 204

  4. **DB 확인**: 제목 수정됨, 기존 slide1 업데이트됨, 신규 slide 1건 추가됨, 총 활성 슬라이드 2건

  5. **PATCH /api/v2/admin/banner-groups/X/active-status** → `active: false`
     - 기대: 204

  6. **DB 확인**: `banner_group.active = false`

  7. **PATCH /api/v2/admin/banner-groups/X/active-status** → `active: true`
     - 기대: 204

  8. **DB 확인**: `banner_group.active = true`

  9. **PATCH /api/v2/admin/banner-groups/X/remove** → 소프트 삭제
     - 기대: 204

  10. **DB 확인**: `banner_group.deleted_at != null`, `banner_group.active = false`

  11. **PATCH /api/v2/admin/banner-groups/X/remove** → 재삭제 시도
      - 기대: 404 (삭제된 리소스는 찾을 수 없음)

### 3.2 Diff 패턴 집중 플로우

#### TC-FLOW-02: Diff 패턴 완전 검증 - 3회 연속 수정으로 슬라이드 상태 추적
- **Priority**: P0
- **Pre-Data**: 없음
- **Steps**:

  1. **POST** → 슬라이드 3건(A, B, C) 등록 → bannerGroupId = X 저장

  2. **1차 PUT**: slides = [A(수정), null(신규D)] (B, C 제외)
     - 기대: A 유지(수정됨), B 소프트삭제, C 소프트삭제, D 신규 추가
     - 활성 슬라이드: A, D (2건)

  3. **DB 확인**: A(deleted_at=null), B(deleted_at!=null), C(deleted_at!=null), D(deleted_at=null)

  4. **2차 PUT**: slides = [A(유지), D(유지), null(신규E)]
     - 기대: A 유지, D 유지, E 신규 추가
     - 활성 슬라이드: A, D, E (3건)

  5. **DB 확인**: 활성 슬라이드 3건 (A, D, E)

  6. **3차 PUT**: slides = [] (전체 삭제)
     - 기대: A, D, E 모두 소프트 삭제
     - 활성 슬라이드: 0건

  7. **DB 확인**: 활성 슬라이드(deleted_at=null) 0건

### 3.3 상태 변경 → 삭제 플로우

#### TC-FLOW-03: 비활성 → 활성 → 삭제 상태 전이 플로우
- **Priority**: P0
- **Pre-Data**: 없음
- **Steps**:

  1. **POST** → active=false로 비활성 등록 → bannerGroupId = X

  2. **DB 확인**: `active=false`, `deleted_at=null`

  3. **PATCH /active-status** → `active: true`

  4. **DB 확인**: `active=true`, `deleted_at=null`

  5. **PATCH /remove** → 소프트 삭제

  6. **DB 확인**: `active=false` (삭제 시 강제 false), `deleted_at!=null`

  7. **PATCH /active-status** → `active: true` 시도
     - 기대: 404 (삭제된 리소스)

  8. **PUT /X** → 수정 시도
     - 기대: 404 (삭제된 리소스)

---

## 4. Fixture 설계

### 4.1 필요 Repository 목록

```java
// setUp에서 deleteAll() 순서 (FK 제약 고려: slide 먼저)
bannerSlideJpaRepository.deleteAll();
bannerGroupJpaRepository.deleteAll();
```

- **BannerGroupJpaRepository**: `adapter-out/persistence-mysql/.../banner/repository/BannerGroupJpaRepository`
- **BannerSlideJpaRepository**: `adapter-out/persistence-mysql/.../banner/repository/BannerSlideJpaRepository`

### 4.2 testFixtures 활용

```java
// adapter-out/persistence-mysql/src/testFixtures/.../banner/BannerJpaEntityFixtures
BannerJpaEntityFixtures.activeGroupEntity()     // 활성 배너 그룹
BannerJpaEntityFixtures.inactiveGroupEntity()   // 비활성 배너 그룹
BannerJpaEntityFixtures.deletedGroupEntity()    // 소프트 삭제 배너 그룹
BannerJpaEntityFixtures.activeSlideEntity()     // 활성 슬라이드
BannerJpaEntityFixtures.activeSlideEntity(id, bannerGroupId)  // ID 지정 슬라이드
BannerJpaEntityFixtures.newSlideEntity(bannerGroupId)         // ID=null 슬라이드
```

### 4.3 setUp 패턴

```java
@Autowired private BannerGroupJpaRepository bannerGroupJpaRepository;
@Autowired private BannerSlideJpaRepository bannerSlideJpaRepository;

@BeforeEach
void setUp() {
    // 외래키 제약 순서에 맞게 역순으로 삭제
    bannerSlideJpaRepository.deleteAll();
    bannerGroupJpaRepository.deleteAll();
}
```

### 4.4 사전 데이터 생성 패턴

```java
// 배너 그룹만 사전 등록 (API를 통한 등록으로 ID 캡처)
private Long registerBannerGroup() {
    Map<String, Object> request = createRegisterRequest();
    Response response = givenAdmin().body(request).when()
        .post(BASE_PATH);
    response.then().statusCode(HttpStatus.CREATED.value());
    return response.jsonPath().getLong("data.bannerGroupId");
}

// 등록 후 슬라이드 ID는 DB에서 직접 조회
private List<Long> getSlideIds(Long bannerGroupId) {
    return bannerSlideJpaRepository.findAll().stream()
        .filter(s -> s.getBannerGroupId() == bannerGroupId)
        .filter(s -> s.isActive())
        .map(BannerSlideJpaEntity::getId)
        .toList();
}
```

### 4.5 Request Body 생성 헬퍼 설계

```java
// 기본 등록 요청 생성
private Map<String, Object> createRegisterRequest() {
    Map<String, Object> request = new HashMap<>();
    request.put("title", "E2E 테스트 배너 그룹");
    request.put("bannerType", "RECOMMEND");
    request.put("displayStartAt", "2026-01-01T00:00:00Z");
    request.put("displayEndAt", "2026-12-31T23:59:59Z");
    request.put("active", true);
    request.put("slides", List.of(createSlideRequest(null, "슬라이드 1", 1)));
    return request;
}

// 슬라이드 요청 생성 (slideId null=신규, 값 있으면=수정)
private Map<String, Object> createSlideRequest(Long slideId, String title, int displayOrder) {
    Map<String, Object> slide = new HashMap<>();
    slide.put("slideId", slideId);
    slide.put("title", title);
    slide.put("imageUrl", "https://cdn.example.com/banner.jpg");
    slide.put("linkUrl", "/event/test");
    slide.put("displayOrder", displayOrder);
    slide.put("displayStartAt", "2026-01-01T00:00:00Z");
    slide.put("displayEndAt", "2026-12-31T23:59:59Z");
    slide.put("active", true);
    return slide;
}

// 상태 변경 요청 생성
private Map<String, Object> createStatusRequest(boolean active) {
    return Map.of("active", active);
}
```

---

## 5. 테스트 클래스 설계

### 5.1 테스트 클래스 구조

```java
@Tag(TestTags.BANNER)
@DisplayName("배너 그룹 Admin API E2E 테스트")
class BannerGroupAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/banner-groups";

    @Autowired private BannerGroupJpaRepository bannerGroupJpaRepository;
    @Autowired private BannerSlideJpaRepository bannerSlideJpaRepository;

    @BeforeEach
    void setUp() {
        bannerSlideJpaRepository.deleteAll();
        bannerGroupJpaRepository.deleteAll();
    }

    @Nested @DisplayName("POST - 배너 그룹 등록") class RegisterTest { ... }
    @Nested @DisplayName("PUT - 배너 그룹 수정 (Diff 패턴)") class UpdateTest { ... }
    @Nested @DisplayName("PATCH /active-status - 노출 상태 변경") class ChangeStatusTest { ... }
    @Nested @DisplayName("PATCH /remove - 소프트 삭제") class RemoveTest { ... }
    @Nested @DisplayName("전체 플로우 시나리오") class FullFlowTest { ... }
}
```

### 5.2 실제 API 경로 주의사항

- `AdminE2ETestBase.setUpRestAssured()`에서 `RestAssured.basePath = "/api"` 설정됨
- 따라서 `BASE_PATH = "/v2/admin/banner-groups"` 로 선언 (앞의 `/api` 제외)
- 실제 호출 경로: `/api/v2/admin/banner-groups`
- 상태 변경: `BASE_PATH + "/" + id + "/active-status"`
- 소프트 삭제: `BASE_PATH + "/" + id + "/remove"`

---

## 6. 우선순위별 시나리오 요약

### P0 (필수 시나리오) - 18개

#### 등록 (TC-C1, 4개)
- TC-C1-S01: 유효한 요청 등록 성공
- TC-C1-S02: 슬라이드 다건 등록 성공
- TC-C1-F01: title 누락 400
- TC-C1-F06: slides null 400

#### 수정 - Diff 핵심 (TC-C2, 7개)
- TC-C2-S02: 기존 슬라이드 수정 (retained)
- TC-C2-S03: 신규 슬라이드 추가 (added)
- TC-C2-S04: 기존 슬라이드 소프트 삭제 (removed)
- TC-C2-S05: 혼합 Diff (추가+수정+삭제 동시)
- TC-C2-F01: 존재하지 않는 ID 수정 404
- TC-C2-F02: title 누락 400
- TC-C2-F04: 슬라이드 내부 필드 누락 400 (@Valid)

#### 상태 변경 (TC-C3, 3개)
- TC-C3-S01: true → false 변경
- TC-C3-S02: false → true 변경
- TC-C3-F01: 존재하지 않는 ID 404

#### 소프트 삭제 (TC-C4, 2개)
- TC-C4-S01: 소프트 삭제 성공
- TC-C4-F01: 존재하지 않는 ID 404

#### 전체 플로우 (TC-FLOW, 3개)
- TC-FLOW-01: CRUD 전체 플로우
- TC-FLOW-02: Diff 패턴 완전 검증 (3회 연속 수정)
- TC-FLOW-03: 상태 전이 플로우

**총 P0 시나리오**: 19개

### P1 (중요 시나리오) - 12개

- TC-C1-S03: active=false로 비활성 등록
- TC-C1-S04: slides 빈 배열로 등록
- TC-C1-F02: title 빈 문자열 400
- TC-C1-F03: bannerType 누락 400
- TC-C1-F04: displayStartAt 누락 400
- TC-C1-F05: displayEndAt 누락 400
- TC-C1-F07: 슬라이드 내부 title 누락 400
- TC-C1-F08: 슬라이드 내부 imageUrl 누락 400
- TC-C1-F09: 슬라이드 내부 linkUrl 누락 400
- TC-C2-S01: 그룹 기본 정보 수정 성공 (DB verify)
- TC-C2-S06: 전체 슬라이드 교체
- TC-C2-S07: 슬라이드 전체 제거 (빈 slides)
- TC-C2-F03: slides null 수정 시 400
- TC-C3-S03: 동일 상태 변경 (멱등성)
- TC-C3-F02: 소프트 삭제 후 상태 변경 404
- TC-C4-S02: 슬라이드 있는 그룹 삭제 (슬라이드 처리 확인)
- TC-C4-S03: 비활성 그룹 삭제
- TC-C4-F02: 이미 삭제된 그룹 재삭제 404

**총 P1 시나리오**: 18개

---

## 7. 총 시나리오 통계

| 분류 | P0 | P1 | 합계 |
|------|----|----|------|
| 등록 (POST) | 4개 | 9개 | 13개 |
| 수정 Diff (PUT) | 7개 | 4개 | 11개 |
| 상태 변경 (PATCH /active-status) | 3개 | 2개 | 5개 |
| 소프트 삭제 (PATCH /remove) | 2개 | 3개 | 5개 |
| 전체 플로우 | 3개 | 0개 | 3개 |
| **총계** | **19개** | **18개** | **37개** |

---

## 8. 구현 시 주의사항

### 8.1 Diff 패턴 테스트 핵심 원칙

- **slideId 추적 필수**: 등록 직후 `bannerSlideJpaRepository`에서 실제 생성된 slideId를 조회하여 PUT 요청에 사용
- **소프트 삭제 확인**: `deleted_at != null` 여부로 삭제 상태 판별 (`bannerSlideJpaRepository.findAll()`로 전체 조회 후 필터)
- **활성 슬라이드 카운트**: deleted_at=null인 레코드만 활성으로 계산

### 8.2 시간 필드 처리

- `displayStartAt`, `displayEndAt`는 ISO 8601 형식 (예: `"2026-01-01T00:00:00Z"`)
- RestAssured body에서 문자열로 전달 시 Jackson이 `Instant`로 자동 변환
- 과거 날짜/미래 날짜 모두 DB 저장 가능 (displayable 판단은 별도 비즈니스 로직)

### 8.3 소프트 삭제 후 404 보장

- `BannerGroupNotFoundException`이 발생하는 조건: `deleted_at != null` 인 그룹을 조회할 때
- 소프트 삭제 이후 PUT, PATCH 모두 404를 반환해야 함 → 플로우 테스트에서 반드시 검증

### 8.4 BannerGroup.remove() 부수 효과

- `remove()` 호출 시 `active = false`로 강제 설정됨
- TC-C4-S01 DB Verify에서 `active = false` 확인 필수

---

## 9. 다음 단계

### 9.1 테스트 구현 파일 위치

```
integration-test/src/test/java/com/ryuqq/setof/integration/test/e2e/admin/banner/
  BannerGroupAdminE2ETest.java
```

### 9.2 TestTags 활용

```java
@Tag(TestTags.BANNER)       // 도메인 태그
@Tag(TestTags.E2E)          // AdminE2ETestBase에서 자동 부여
@Tag(TestTags.ADMIN)        // AdminE2ETestBase에서 자동 부여
```

### 9.3 테스트 실행

```bash
# 배너 E2E 전체 실행
./gradlew :integration-test:test --tests "*BannerGroupAdminE2ETest"

# 전체 Admin E2E 실행
./gradlew :integration-test:e2eTest
```

---

**분석 완료**: 2026-03-15
**다음 작업**: `/test-e2e admin:banner` 명령으로 실제 테스트 코드 생성
