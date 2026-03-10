# API Flow: MileageController.fetchMileageHistories

## 1. 기본 정보

- **HTTP**: GET /api/v1/mileage/my-page/mileage-histories
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 로그인된 일반 회원만 접근 가능
- **Controller**: `MileageController`
- **Service**: `UserMileageFindService` → `UserMileageFindServiceImpl`
- **Repository 1**: `MileageHistoryFindRepository` → `MileageHistoryFindRepositoryImpl`
- **Repository 2**: `UserMileageFindRepository` → `UserMileageFindRepositoryImpl`
- **Mapper**: `UserMileageMapper`

---

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| lastDomainId | Long | Query (@ModelAttribute) | N | 커서 기반 페이징 마지막 ID (실제 쿼리 미사용) |
| reasons | List\<Reason\> | Query (@ModelAttribute) | N | 마일리지 사유 필터 (미설정 시 전체 조회) |
| page | Integer | Query (Pageable) | N | 페이지 번호 (0부터 시작) |
| size | Integer | Query (Pageable) | N | 페이지 사이즈 |
| sort | String | Query (Pageable) | N | 정렬 기준 |

**Reason Enum 값**:

| 값 | 설명 |
|----|------|
| SAVE | 마일리지 적립 |
| USE | 마일리지 사용 |
| REFUND | 마일리지 환불 |
| EXPIRED | 마일리지 만료 |

### URL 예시

```
GET /api/v1/mileage/my-page/mileage-histories?reasons=SAVE&reasons=USE&size=10&page=0
```

---

## 3. Response

### DTO 구조

```
MileagePage<UserMileageHistoryDto>
├── userMileage: UserMileageDto              # 현재 마일리지 요약
│   ├── userId: long
│   ├── currentMileage: double               # 실제 사용 가능 마일리지
│   ├── expectedSaveMileage: double          # 적립 예정 마일리지 (현재 항상 0.0)
│   └── expectedExpireMileage: double        # 만료 예정 마일리지
├── content: List<UserMileageHistoryDto>     # 마일리지 내역 목록
│   ├── (* @JsonIgnore) mileageHistoryId: long
│   ├── mileageId: long
│   ├── title: String                        # Mapper에서 사유별 동적 설정
│   ├── paymentId: long
│   ├── orderId: long
│   ├── changeAmount: double
│   ├── reason: Reason
│   ├── usedDate: LocalDateTime              # yyyy-MM-dd HH:mm:ss
│   └── expirationDate: LocalDateTime        # yyyy-MM-dd HH:mm:ss
├── last: boolean
├── first: boolean
├── totalPages: int
├── totalElements: long
├── number: int
├── size: int
├── numberOfElements: int
├── empty: boolean
├── lastDomainId: Long
└── (* @JsonIgnore) originalPage: Page<T>
```

### JSON 예시

```json
{
  "success": true,
  "data": {
    "userMileage": {
      "userId": 12345,
      "currentMileage": 15000.0,
      "expectedSaveMileage": 0.0,
      "expectedExpireMileage": 3000.0
    },
    "content": [
      {
        "mileageId": 1001,
        "title": "회원가입 축하 적립금",
        "paymentId": 0,
        "orderId": 0,
        "changeAmount": 5000.0,
        "reason": "SAVE",
        "usedDate": "2024-01-15 10:30:00",
        "expirationDate": "2025-01-15 23:59:59"
      },
      {
        "mileageId": 1002,
        "title": "주문 결제 시 사용",
        "paymentId": 789,
        "orderId": 101,
        "changeAmount": -2000.0,
        "reason": "USE",
        "usedDate": "2024-01-20 14:22:15",
        "expirationDate": "2025-01-10 23:59:59"
      }
    ],
    "totalElements": 25,
    "totalPages": 3,
    "size": 10,
    "number": 0,
    "first": true,
    "last": false,
    "empty": false,
    "lastDomainId": null
  }
}
```

---

## 4. 호출 흐름

```
MileageController.fetchMileageHistories(MileageFilter, Pageable)
    │
    └── UserMileageFindService.fetchMyMileageHistories(filter, pageable)
            │
            ├── [1] SecurityUtils.currentUserId()
            │       └── Security Context에서 인증된 userId 추출
            │
            ├── [2] MileageHistoryFindRepository.fetchMileageHistories(filter, userId, pageable)
            │       └── MileageHistoryFindRepositoryImpl
            │               └── QueryDSL: mileage INNER JOIN mileage_history
            │                             LEFT JOIN order_snapshot_mileage_detail
            │                             LEFT JOIN order_snapshot_mileage
            │
            ├── [3] UserMileageMapper.setTitle(userMileageHistories)
            │       └── reason 값에 따라 title 필드 동적 설정
            │           SAVE    → DB title 그대로 사용
            │           USE     → "주문 결제 시 사용"
            │           EXPIRED → "적립금 유효기간 만료"
            │           REFUND  → "적립금 사용주문 취소"
            │
            ├── [4] MileageHistoryFindRepository.fetchMileageHistoryCount(filter, userId)
            │       └── MileageHistoryFindRepositoryImpl
            │               └── QueryDSL: COUNT from mileage_history
            │
            ├── [5] PageableExecutionUtils.getPage(list, pageable, countQuery)
            │       └── Page<UserMileageHistoryDto> 생성
            │
            ├── [6] UserMileageFindService.fetchUserMileage(userId)
            │       └── UserMileageFindRepository.fetchUserMileageQueryInMyPage(userId)
            │               └── UserMileageFindRepositoryImpl
            │                       └── QueryDSL: mileage WHERE userId AND 잔액>0 AND activeYn=Y
            │           → UserMileageMapper.toMyMileages(userId, queries, 0.0)
            │               └── 잔여 마일리지 합계 및 만료 예정 마일리지 계산
            │
            └── [7] UserMileageMapper.toMileagePage(userMileage, page)
                    └── MileagePage<UserMileageHistoryDto> 반환
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | JOIN 유형 | JOIN 조건 |
|--------|------|-----------|-----------|
| `mileage` | 마일리지 원장 | FROM | - |
| `mileage_history` | 마일리지 사용/적립 내역 | INNER JOIN | `mileage.mileage_id = mileage_history.mileage_id` |
| `order_snapshot_mileage_detail` | 주문 마일리지 상세 스냅샷 | LEFT JOIN | `order_snapshot_mileage_detail.mileage_id = mileage.mileage_id` |
| `order_snapshot_mileage` | 주문 마일리지 스냅샷 | LEFT JOIN | `order_snapshot_mileage.id = order_snapshot_mileage_detail.order_snapshot_mileage_id` AND `order_snapshot_mileage.order_id = mileage_history.target_id` |

### Query 1: 마일리지 내역 목록 조회

```java
queryFactory
    .select(
        new QUserMileageHistoryDto(
            mileageHistory.id,
            mileage.id,
            mileage.title,
            orderSnapShotMileage.paymentId,
            orderSnapShotMileage.orderId,
            mileageHistory.changeAmount,
            mileageHistory.reason,
            mileageHistory.insertDate,
            mileage.expirationDate))
    .from(mileage)
    .innerJoin(mileageHistory)
        .on(mileage.id.eq(mileageHistory.mileageId))
    .leftJoin(orderSnapShotMileageDetail)
        .on(orderSnapShotMileageDetail.mileageId.eq(mileage.id))
    .leftJoin(orderSnapShotMileage)
        .on(orderSnapShotMileage.id.eq(orderSnapShotMileageDetail.orderSnapShotMileageId))
        .on(orderSnapShotMileage.orderId.eq(mileageHistory.targetId))
    .where(
        mileage.userId.eq(userId),
        reasonEq(filter),                            // reasons != null && !empty 인 경우만 IN 조건
        mileageHistory.targetId.eq(0L)               // 비주문 마일리지 (targetId == 0)
            .or(
                orderSnapShotMileage.orderId.gt(0L).and(orderSnapShotMileage.paymentId.gt(0L))
                    .or(
                        orderSnapShotMileage.orderId.loe(0L).or(orderSnapShotMileage.paymentId.loe(0L))
                    )
            )
    )
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .distinct()
    .orderBy(mileageHistory.insertDate.desc())
    .groupBy(
        mileageHistory.id, mileage.id, mileage.title,
        orderSnapShotMileage.paymentId, orderSnapShotMileage.orderId,
        mileageHistory.changeAmount, mileageHistory.reason,
        mileageHistory.insertDate, mileage.expirationDate)
    .fetch()
```

### Query 2: 마일리지 내역 카운트

```java
queryFactory
    .select(mileageHistory.count())
    .from(mileageHistory)
    .where(
        mileageHistory.userId.eq(userId),
        reasonEq(filter)    // reasons != null && !empty 인 경우만 IN 조건
    )
    .distinct()
```

### Query 3: 현재 마일리지 조회

```java
queryFactory
    .from(mileage)
    .where(
        mileage.userId.eq(userId),
        mileage.mileageAmount.subtract(mileage.usedMileageAmount).gt(0),  // 잔액 > 0
        mileage.activeYn.eq(Yn.Y)                                          // 활성 상태
    )
    .transform(
        GroupBy.groupBy(mileage.id)
            .list(new QUserMileageQueryDto(
                mileage.userId,
                mileage.id,
                mileage.mileageAmount,
                mileage.usedMileageAmount,
                mileage.activeYn,
                mileage.issuedDate,
                mileage.expirationDate))
    )
```

---

## 6. Entity → 테이블 매핑

| Entity | 테이블 |
|--------|--------|
| `Mileage` | `mileage` |
| `MileageHistory` | `mileage_history` |
| `OrderSnapShotMileage` | `order_snapshot_mileage` |
| `OrderSnapShotMileageDetail` | `order_snapshot_mileage_detail` |

---

## 7. 특이사항

- **인증 필수**: `SecurityUtils.currentUserId()`로 JWT/Session에서 userId를 추출하므로 인증 토큰 없이 호출 불가
- **복합 응답**: 마일리지 요약(`UserMileageDto`)과 이력 목록을 단일 응답으로 합산. 내부적으로 쿼리를 2번 실행(내역 + 현재 마일리지)
- **title 후처리**: `UserMileageMapper.setTitle()`이 QueryDSL 조회 후 Java에서 reason별 title을 별도 가공. SAVE 사유는 DB의 title 그대로 사용
- **복합 JOIN 조건**: `order_snapshot_mileage` LEFT JOIN에 2개의 ON 조건이 연결되며, `targetId == 0` 조건과 OR로 결합되어 주문/비주문 내역 모두 포함
- **카운트 쿼리 최적화**: `PageableExecutionUtils.getPage()` 사용으로 마지막 페이지 등에서 count 쿼리 skip 가능
- **lastDomainId 미사용**: `MileageFilter.lastDomainId`는 필드로 존재하나 실제 Repository 쿼리에서 WHERE 조건으로 사용되지 않음. 표준 Pageable offset/limit 방식 적용
- **pendingMileage 주석 처리**: `fetchUserPendingMileages()` 호출 코드가 주석 처리되어 있어 `expectedSaveMileage`는 항상 `0.0` 반환
