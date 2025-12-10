package com.ryuqq.setof.adapter.out.persistence.member.adapter;

import com.ryuqq.setof.adapter.out.persistence.member.mapper.MemberJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.member.repository.MemberQueryDslRepository;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * MemberQueryAdapter - Member Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Member 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>핸드폰 번호로 조회 (findByPhoneNumber)
 *   <li>소셜 ID로 조회 (findBySocialId)
 *   <li>핸드폰 번호 존재 여부 확인 (existsByPhoneNumber)
 *   <li>QueryDslRepository 호출
 *   <li>Mapper를 통한 Entity → Domain 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>❌ 비즈니스 로직 금지
 *   <li>❌ 저장/수정/삭제 금지 (CommandAdapter로 분리)
 *   <li>❌ JPAQueryFactory 직접 사용 금지 (QueryDslRepository에서 처리)
 *   <li>❌ DTO 반환 금지 (Domain 반환)
 * </ul>
 *
 * <p><strong>Hexagonal Architecture:</strong>
 *
 * <ul>
 *   <li>MemberQueryPort 인터페이스 구현
 *   <li>Application Layer의 Port를 Persistence Layer에서 구현
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class MemberQueryAdapter implements MemberQueryPort {

    private final MemberQueryDslRepository queryDslRepository;
    private final MemberJpaEntityMapper memberJpaEntityMapper;

    public MemberQueryAdapter(
            MemberQueryDslRepository queryDslRepository,
            MemberJpaEntityMapper memberJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.memberJpaEntityMapper = memberJpaEntityMapper;
    }

    /**
     * ID로 Member 단건 조회
     *
     * <p>MemberId(UUID v7)로 Member를 조회합니다.
     *
     * @param id Member ID (Value Object)
     * @return Member Domain (Optional)
     */
    @Override
    public Optional<Member> findById(MemberId id) {
        return queryDslRepository
                .findById(id.value().toString())
                .map(memberJpaEntityMapper::toDomain);
    }

    /**
     * 핸드폰 번호로 Member 조회
     *
     * <p>핸드폰 번호는 고유값이므로 단건 조회됩니다.
     *
     * @param phoneNumber 핸드폰 번호 (Value Object)
     * @return Member Domain (Optional)
     */
    @Override
    public Optional<Member> findByPhoneNumber(PhoneNumber phoneNumber) {
        return queryDslRepository
                .findByPhoneNumber(phoneNumber.value())
                .map(memberJpaEntityMapper::toDomain);
    }

    /**
     * 소셜 ID로 Member 조회
     *
     * <p>카카오 등 소셜 로그인 시 소셜 ID로 회원을 조회합니다.
     *
     * @param socialId 소셜 ID (Value Object)
     * @return Member Domain (Optional)
     */
    @Override
    public Optional<Member> findBySocialId(SocialId socialId) {
        return queryDslRepository
                .findBySocialId(socialId.value())
                .map(memberJpaEntityMapper::toDomain);
    }

    /**
     * 핸드폰 번호로 Member 존재 여부 확인
     *
     * <p>회원가입 시 중복 체크에 사용됩니다.
     *
     * @param phoneNumber 핸드폰 번호 (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return queryDslRepository.existsByPhoneNumber(phoneNumber.value());
    }
}
