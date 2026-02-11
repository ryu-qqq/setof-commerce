# API Flow Documentation: NewsController.fetchFaq

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/faq` |
| Controller | `NewsController` |
| Service | `FaqFindService` → `FaqFindServiceImpl` |
| Repository | `FaqFindRepository` → `FaqFindRepositoryImpl` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| faqType | FaqType (enum) | ✅ | FAQ 유형 | @NotNull |

### FaqType Enum 값

| 값 | 설명 |
|----|------|
| MEMBER_LOGIN | 회원/로그인 |
| PRODUCT_SELLER | 상품/판매자 |
| SHIPPING | 배송 |
| ORDER_PAYMENT | 주문/결제 |
| CANCEL_REFUND | 취소/환불 |
| EXCHANGE_RETURN | 교환/반품 |
| TOP | 상단 고정 FAQ |

### Request DTO 구조

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FaqFilter {

    @NotNull(message = "FAQ 타입은 필수 입니다.")
    private FaqType faqType;
}
```

---

## 📤 Response

### Response DTO 구조

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqDto {
    private FaqType faqType;
    private String title;
    private String contents;

    @QueryProjection
    public FaqDto(FaqType faqType, String title, String contents) {
        this.faqType = faqType;
        this.title = title;
        this.contents = contents;
    }
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "faqType": "MEMBER_LOGIN",
      "title": "회원 가입은 어떻게 하나요?",
      "contents": "회원 가입 방법 안내..."
    },
    {
      "faqType": "MEMBER_LOGIN",
      "title": "비밀번호를 분실했습니다.",
      "contents": "비밀번호 재설정 방법..."
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   Controller                                                  │
│   NewsController.fetchFaq(@ModelAttribute @Validated FaqFilter)│
│   @GetMapping("/faq")                                         │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Service                                                     │
│   FaqFindServiceImpl.fetchFaqDto(FaqFilter)                  │
│   @Transactional(readOnly=true)                              │
│   @Cacheable(cacheNames="faq", key="#filter.faqType")        │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Repository                                                  │
│   FaqFindRepositoryImpl.fetchFaq(FaqFilter)                  │
│   QueryDSL                                                    │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Table: faq                                                  │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| faq | faq | FROM | - |

### Entity 구조

```java
@Table(name = "faq")
@Entity
public class Faq extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FaqType faqType;

    private String title;
    private String contents;
    private int displayOrder;
    private int topDisplayOrder;
}
```

### QueryDSL 코드

```java
public List<FaqDto> fetchFaq(FaqFilter filter) {
    List<OrderSpecifier<?>> orders = createOrderByFaqType(filter.getFaqType());
    return queryFactory
            .select(new QFaqDto(faq.faqType, faq.title, faq.contents))
            .from(faq)
            .where(faqTypeEq(filter.getFaqType()), hasTopDisplayOrder(filter.getFaqType()))
            .orderBy(orders.toArray(OrderSpecifier[]::new))
            .fetch();
}
```

### WHERE 조건

| 조건 메서드 | 적용 조건 | 설명 |
|------------|----------|------|
| `faqTypeEq` | faqType != TOP | `faq.faqType.eq(faqType)` |
| `hasTopDisplayOrder` | faqType == TOP | `faq.topDisplayOrder.isNotNull()` |

### ORDER BY 조건

| faqType | 정렬 |
|---------|------|
| TOP | `faq.topDisplayOrder ASC` |
| 그 외 | `faq.displayOrder ASC` |

---

## 🔧 특이사항

### 캐싱
- **Cache Name**: `faq`
- **Cache Key**: `faqType` 값
- FAQ 타입별로 캐시가 분리됨

### 비즈니스 로직
- `TOP` 타입: 상단 고정 FAQ만 조회 (topDisplayOrder가 있는 항목)
- 일반 타입: 해당 타입의 FAQ만 조회, displayOrder로 정렬

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:NewsController.fetchFaq

# Persistence Layer 생성
/legacy-query web:NewsController.fetchFaq
```
