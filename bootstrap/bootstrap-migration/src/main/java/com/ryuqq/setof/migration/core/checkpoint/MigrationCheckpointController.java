package com.ryuqq.setof.migration.core.checkpoint;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 마이그레이션 체크포인트 조회/관리 API
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/migration/checkpoints")
public class MigrationCheckpointController {

    private final MigrationCheckpointRepository repository;

    public MigrationCheckpointController(MigrationCheckpointRepository repository) {
        this.repository = repository;
    }

    /** 전체 체크포인트 조회 */
    @GetMapping
    public ResponseEntity<List<MigrationCheckpoint>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    /** 특정 도메인 체크포인트 조회 */
    @GetMapping("/{domainName}")
    public ResponseEntity<MigrationCheckpoint> findByDomainName(@PathVariable String domainName) {
        return repository
                .findByDomainName(domainName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** 마이그레이션 가능한 체크포인트 조회 */
    @GetMapping("/migratable")
    public ResponseEntity<List<MigrationCheckpoint>> findMigratable() {
        return ResponseEntity.ok(repository.findMigratable());
    }

    /** 체크포인트 리셋 (처음부터 다시 시작) */
    @PostMapping("/{domainName}/reset")
    public ResponseEntity<Void> reset(@PathVariable String domainName) {
        repository.resetCheckpoint(domainName);
        return ResponseEntity.ok().build();
    }

    /** 마이그레이션 일시 중지 */
    @PostMapping("/{domainName}/pause")
    public ResponseEntity<Void> pause(@PathVariable String domainName) {
        repository.pauseMigration(domainName);
        return ResponseEntity.ok().build();
    }
}
