package com.ryuqq.setof.storage.mysql.member.adapter;

import com.ryuqq.setof.application.member.port.out.command.MemberPersistencePort;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.storage.mysql.member.entity.MemberJpaEntity;
import com.ryuqq.setof.storage.mysql.member.mapper.MemberJpaEntityMapper;
import com.ryuqq.setof.storage.mysql.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Component;

/**
 * MemberCommandAdapter - Member Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Member Domain을 영속화하는 Adapter입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Domain → Entity 변환
 *   <li>JpaRepository.save() 호출
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>❌ 비즈니스 로직 금지 (Domain 메서드에서 처리)
 *   <li>❌ 조회 로직 금지 (QueryAdapter로 분리)
 *   <li>❌ @Transactional 금지 (Application Layer에서 처리)
 * </ul>
 *
 * <p><strong>Hexagonal Architecture:</strong>
 *
 * <ul>
 *   <li>MemberPersistencePort 인터페이스 구현
 *   <li>Application Layer의 Port를 Persistence Layer에서 구현
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class MemberCommandAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberJpaEntityMapper memberJpaEntityMapper;

    public MemberCommandAdapter(
            MemberJpaRepository memberJpaRepository, MemberJpaEntityMapper memberJpaEntityMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberJpaEntityMapper = memberJpaEntityMapper;
    }

    /**
     * Member 저장 (신규 생성 또는 수정)
     *
     * <p><strong>신규 생성 (Entity ID 없음):</strong>
     *
     * <ul>
     *   <li>JPA가 AUTO_INCREMENT로 ID 자동 할당 (INSERT)
     * </ul>
     *
     * <p><strong>기존 수정 (Entity ID 있음):</strong>
     *
     * <ul>
     *   <li>더티체킹으로 자동 UPDATE
     * </ul>
     *
     * <p><strong>참고:</strong>
     *
     * <ul>
     *   <li>Member는 UUID v7 ID를 이미 갖고 있으므로 ID 반환 불필요
     *   <li>Domain에서 UUID v7으로 ID를 생성하여 전달받음
     * </ul>
     *
     * @param member 저장할 Member (Domain Aggregate, UUID v7 ID 포함)
     */
    @Override
    public void persist(Member member) {
        // 1. Domain → Entity 변환
        MemberJpaEntity entity = memberJpaEntityMapper.toEntity(member);

        // 2. JPA 저장 (신규/수정 JPA가 자동 판단)
        memberJpaRepository.save(entity);
    }
}
