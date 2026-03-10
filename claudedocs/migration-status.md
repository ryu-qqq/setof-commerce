# Web API Legacy Migration Status

> Shadow Traffic 기반 마이그레이션 진행 현황
> Last Updated: 2026-03-10

---

## 완료 도메인

| # | 도메인 | 테스트 수 | Shadow Traffic | 비고 |
|---|--------|----------|----------------|------|
| 1 | banner | 6 | PASS | display 모듈에서 분리 (ContentController.getBannerGroups) |
| 2 | brand | 8 | PASS | 데이터 타이밍 차이 무시 가능 |
| 3 | cart | 21 | PASS | validation 2건은 New가 올바름 (Legacy 버그) |
| 4 | category | 2 | PASS | |
| 5 | faq | 7 | PASS | news 모듈에서 분리 (NewsController.fetchFaq). TOP FAQ 개수 차이 무시 가능 |
| 6 | gnb | 1 | PASS | display 모듈에서 분리 (ContentController.getGnbs) |
| 7 | mypage | 2 | PASS | |
| 8 | refund-account | 12 | PASS | 에러 메시지 문구 차이 무시 가능 |
| 9 | review | 19 | PASS | delete 1건 New 미구현 (known_diff) |
| 10 | seller | 6 | PASS | |
| 11 | shipping-address | 6 | PASS | 에러 메시지 문구 차이 무시 가능 |
| 12 | user | 14 | PASS | withdrawal 1건 New 버그 (known_diff) |
| 13 | wishlist | 9 | PASS | 테스트 순서 문제 (단독 PASS). data 반환값 차이 무시 가능 |
| 14 | content | 22 | PASS | deduplication 적용. TAB/imageComponentLinks minor 차이 무시 가능 |
| 15 | productgroup | 34 | PASS | 6건 known_diff (존재하지 않는 상품: Legacy 200/500, New 400). New가 올바름 |
| 16 | mileage | 12 | PASS | 12/12 CLEAN PASS. Composite 패턴 4-table JOIN |

### 레거시 모듈 매핑 완료

| 레거시 모듈 | 마이그레이션 도메인 | 상태 |
|-----------|------------------|------|
| display | content + banner + gnb | 완료 |
| news | faq + board | faq 완료. board는 미사용 확인 필요 |
| product | productgroup | 완료 |
| mileage | mileage | 완료 |

---

## 미개발 도메인

없음 (모든 Web API 마이그레이션 완료)

---

## 주요 도메인 (별도 트래킹)

| # | 도메인 | 테스트 수 | known_diff | 상태 |
|---|--------|----------|------------|------|
| 1 | order | 12 | 10건 | 별도 분석 필요 |
| 2 | payment | 36 | 18+건 | 별도 분석 필요 |
| 3 | search | 18 | 15+건 | 별도 분석 필요 |
| 4 | qna | 36 | 7건 | 별도 분석 필요 |

---

## 전체 Shadow Traffic 결과

- **전체**: 283/283 PASS (100%)
- **실질 매칭**: ~232/283 (known_diff 제외)
- **known_diff**: ~51건 (문서화된 알려진 차이)
