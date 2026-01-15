---
paths:
  - "adapter-in/web/**"
  - "**/controller/**"
  - "**/*Controller*"
  - "**/*ApiRequest*"
  - "**/*ApiResponse*"
---

# REST API Layer 규칙 (자동 주입)

이 규칙은 REST API 관련 파일을 작업할 때 **자동으로 적용**됩니다.

## Zero-Tolerance 규칙 (절대 위반 금지)

### 테스트 규칙
- **MockMvc 절대 금지**: `@WebMvcTest`, `MockMvc` 사용 금지
- **TestRestTemplate 필수**: `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate` 사용

### Controller 규칙
- **CTR-005**: Controller에서 `@Transactional` 절대 금지
- **@Valid 필수**: Request DTO에 반드시 `@Valid` 적용
- **ApiPaths 사용**: 경로 상수는 `ApiPaths` 클래스 사용

### DTO 규칙
- Request/Response DTO는 `adapter-in/web` 모듈에 위치
- Validation 어노테이션은 API Request DTO에만 적용

## Controller 패턴

```java
@RestController
@RequestMapping(ApiPaths.Orders.BASE)
public class OrderCommandController {
    private final CancelOrderUseCase cancelOrderUseCase;
    private final OrderCommandApiMapper mapper;

    // @Transactional 금지!
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderApiRequest request) {  // @Valid 필수

        CancelOrderCommand command = mapper.toCommand(orderId, request);
        cancelOrderUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
```

## API Request DTO 패턴

```java
// Validation 어노테이션은 API Request에만!
public record CancelOrderApiRequest(
    @NotBlank(message = "취소 사유는 필수입니다")
    @Size(max = 500, message = "취소 사유는 500자 이하입니다")
    String reason
) {}
```

## 통합 테스트 패턴

```java
// MockMvc 금지! TestRestTemplate 사용!
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void cancelOrder_ShouldReturn200() {
        // given
        Long orderId = createTestOrder();
        CancelOrderApiRequest request = new CancelOrderApiRequest("고객 요청");

        // when
        ResponseEntity<ApiResponse<Void>> response = restTemplate.postForEntity(
            "/api/v1/orders/{orderId}/cancel",
            request,
            new ParameterizedTypeReference<>() {},
            orderId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void cancelOrder_WithoutReason_ShouldReturn400() {
        // given
        Long orderId = createTestOrder();
        CancelOrderApiRequest request = new CancelOrderApiRequest("");  // 빈 사유

        // when
        ResponseEntity<ApiResponse<Void>> response = restTemplate.postForEntity(
            "/api/v1/orders/{orderId}/cancel",
            request,
            new ParameterizedTypeReference<>() {},
            orderId
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
```

## API 경로 상수 패턴

```java
public final class ApiPaths {
    private ApiPaths() {}

    public static final String API_V1 = "/api/v1";

    public static final class Orders {
        public static final String BASE = API_V1 + "/orders";
        public static final String CANCEL = "/{orderId}/cancel";
    }
}
```

## 상세 규칙 참조

더 자세한 규칙은 다음 파일을 참조하세요:
- `.claude/knowledge/rules/rest-api-rules.md` (58개 규칙)
- `.claude/knowledge/templates/rest-api-templates.md`
- `.claude/knowledge/examples/rest-api-examples.md`
