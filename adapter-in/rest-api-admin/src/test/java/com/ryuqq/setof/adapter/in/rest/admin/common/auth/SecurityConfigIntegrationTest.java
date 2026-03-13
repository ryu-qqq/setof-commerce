package com.ryuqq.setof.adapter.in.rest.admin.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ServiceTokenAuthenticationFilter MockMvc 통합 테스트.
 *
 * <p>필터가 서블릿 체인에서 올바르게 동작하는지 검증합니다.
 *
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ServiceTokenAuthenticationFilter MockMvc 통합 테스트")
class SecurityConfigIntegrationTest {

    private static final String SECRET = "test-secret";
    private MockMvc mockMvc;

    @RestController
    static class TestController {

        @GetMapping("/api/v2/test/protected")
        public String protectedEndpoint() {
            return "ok";
        }
    }

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        ServiceTokenProperties properties = new ServiceTokenProperties();
        properties.setEnabled(true);
        properties.setSecret(SECRET);

        ServiceTokenAuthenticationFilter filter = new ServiceTokenAuthenticationFilter(properties);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController()).addFilter(filter).build();
    }

    @Nested
    @DisplayName("유효한 토큰으로 요청 시")
    class WithValidToken {

        @Test
        @DisplayName("200을 반환하고 SecurityContext에 인증이 설정된다")
        void returns200AndSetsAuthentication() throws Exception {
            mockMvc.perform(
                            get("/api/v2/test/protected")
                                    .header("X-Service-Token", SECRET)
                                    .header("X-Service-Name", "admin-frontend"))
                    .andExpect(status().isOk());

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getPrincipal()).isEqualTo("admin-frontend");
            assertThat(auth.getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_SERVICE");
        }
    }

    @Nested
    @DisplayName("토큰 없이 요청 시")
    class WithoutToken {

        @Test
        @DisplayName("요청은 통과하지만 SecurityContext에 인증이 설정되지 않는다")
        void requestPassesButNoAuthentication() throws Exception {
            mockMvc.perform(get("/api/v2/test/protected")).andExpect(status().isOk());

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNull();
        }
    }

    @Nested
    @DisplayName("잘못된 토큰으로 요청 시")
    class WithInvalidToken {

        @Test
        @DisplayName("요청은 통과하지만 SecurityContext에 인증이 설정되지 않는다")
        void requestPassesButNoAuthentication() throws Exception {
            mockMvc.perform(get("/api/v2/test/protected").header("X-Service-Token", "wrong-token"))
                    .andExpect(status().isOk());

            var auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNull();
        }
    }
}
