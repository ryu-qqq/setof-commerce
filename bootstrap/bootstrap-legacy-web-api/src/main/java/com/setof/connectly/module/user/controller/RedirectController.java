package com.setof.connectly.module.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RedirectController {

    @GetMapping("/redirect")
    public ResponseEntity<String> redirectHandler(@RequestParam("token") String token) {
        log.info("Redirected to this URL with token: {}", token);
        return ResponseEntity.ok("Redirected successfully with token: " + token);
    }
}
