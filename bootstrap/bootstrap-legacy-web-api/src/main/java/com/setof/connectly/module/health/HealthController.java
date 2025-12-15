package com.setof.connectly.module.health;

import com.setof.connectly.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Object>> fetchHealth() {
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
