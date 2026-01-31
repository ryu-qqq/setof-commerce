 AuthHub SDK 2.0.0 사용법 (멀티모듈)

  1. 루트 build.gradle 또는 settings.gradle에 JitPack 저장소 추가

  // settings.gradle.kts
  dependencyResolutionManagement {
      repositories {
          mavenCentral()
          maven { url = uri("<https://jitpack.io>") }
      }
  }

  또는 루트 build.gradle:

  // build.gradle (root)
  allprojects {
      repositories {
          mavenCentral()
          maven { url '<https://jitpack.io>' }
      }
  }

  ---

  1. 의존성 추가 (필요한 모듈에만)

  Spring Boot 프로젝트라면 보통 bootstrap 또는 adapter-in 모듈에 추가:

  // bootstrap/bootstrap-web-api/build.gradle
  dependencies {
      implementation 'com.github.ryu-qqq.AuthHub:authhub-sdk-spring-boot-starter:v2.0.0'
  }

  또는 순수 Java 모듈이라면:

  // 특정 모듈/build.gradle
  dependencies {
      implementation 'com.github.ryu-qqq.AuthHub:authhub-sdk-core:v2.0.0'
  }

  ---

  1. 멀티모듈 구조 예시

  your-project/
  ├── settings.gradle.kts       # JitPack repository 설정
  ├── domain/                   # SDK 불필요
  ├── application/              # SDK 불필요 (또는 interface만 정의)
  ├── adapter-in/
  │   └── rest-api/
  │       └── build.gradle      # ❌ SDK 불필요
  ├── adapter-out/
  │   └── client/
  │       └── authhub-client/   # ✅ SDK 사용 (AuthHub 호출하는 모듈)
  │           └── build.gradle
  └── bootstrap/
      └── bootstrap-web-api/
          └── build.gradle      # ✅ SDK starter 의존성

  ---

  1. 권장 구성 (Hexagonal 기준)

  방법 A: Bootstrap에만 추가 (간단)

  // bootstrap/bootstrap-web-api/build.gradle
  dependencies {
      implementation 'com.github.ryu-qqq.AuthHub:authhub-sdk-spring-boot-starter:v2.0.0'
  }

  → AutoConfiguration으로 AuthHubClient Bean 자동 등록

  방법 B: Adapter-Out에 클라이언트 모듈 생성 (권장 - 의존성 격리)

  // adapter-out/client/authhub-client/build.gradle
  dependencies {
      implementation 'com.github.ryu-qqq.AuthHub:authhub-sdk-core:v2.0.0'
      implementation project(':application')  // Port interface 구현
  }

  // bootstrap/bootstrap-web-api/build.gradle
  dependencies {
      implementation project(':adapter-out:client:authhub-client')
      implementation 'com.github.ryu-qqq.AuthHub:authhub-sdk-spring-boot-starter:v2.0.0'
  }

  ---

  1. 설정 (application.yml)

  authhub:
    base-url: <https://auth.your-domain.com>
    service-token: ${AUTHHUB_SERVICE_TOKEN}
    timeout:
      connect: 5s
      read: 30s

  ---

  1. 사용 예시

  @Service
  @RequiredArgsConstructor
  public class AuthService {

      private final AuthHubClient authHubClient;

      public void createTenant(String name) {
          var response = authHubClient.tenants()
              .create(new CreateTenantRequest(name));

          log.info("Created tenant: {}", response.data().tenantId());
      }
  }

  ---
  요약
  ┌────────────────────┬────────────────────────────────────────┬───────────────────┐
  │        위치        │                 의존성                 │       용도        │
  ├────────────────────┼────────────────────────────────────────┼───────────────────┤
  │ settings.gradle    │ JitPack repository                     │ 저장소 설정       │
  ├────────────────────┼────────────────────────────────────────┼───────────────────┤
  │ bootstrap          │ authhub-sdk-spring-boot-starter:v2.0.0 │ AutoConfig + Bean │
  ├────────────────────┼────────────────────────────────────────┼───────────────────┤
  │ adapter-out (선택) │ authhub-sdk-core:v2.0.0                │ 순수 SDK 사용     │
  └────────────────────┴────────────────────────────────────────┴───────────────────┘
