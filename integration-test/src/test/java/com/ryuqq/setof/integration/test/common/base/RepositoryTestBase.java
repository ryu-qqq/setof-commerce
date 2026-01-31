package com.ryuqq.setof.integration.test.common.base;

import com.ryuqq.setof.integration.test.common.config.RepositoryTestConfig;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Repository 통합 테스트 베이스 클래스
 *
 * <p>JPA/Repository 레이어만 테스트합니다. E2E보다 가볍고 빠르며, DB 연동을 검증합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * class SellerRepositoryTest extends RepositoryTestBase {
 *
 *     @Autowired
 *     private SellerJpaRepository sellerRepository;
 *
 *     @Nested
 *     @DisplayName("판매자 저장")
 *     class SaveTest {
 *
 *         @Test
 *         void shouldSaveSeller() {
 *             // given
 *             var entity = SellerJpaEntityFixtures.createDefault();
 *
 *             // when
 *             var saved = sellerRepository.save(entity);
 *             flushAndClear();
 *
 *             // then
 *             assertThat(sellerRepository.findById(saved.getId())).isPresent();
 *         }
 *     }
 * }
 * }</pre>
 *
 * <p>실행:
 *
 * <pre>
 * ./gradlew :integration-test:repositoryTest
 * </pre>
 *
 * @see TestTags#REPOSITORY
 */
@Tag(TestTags.REPOSITORY)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(RepositoryTestConfig.class)
public abstract class RepositoryTestBase {

    @Autowired protected TestEntityManager testEntityManager;

    @Autowired protected EntityManager entityManager;

    /**
     * 영속성 컨텍스트를 flush하고 clear합니다.
     *
     * <p>저장 후 실제 DB에서 조회하는 것을 검증할 때 사용합니다.
     *
     * <pre>{@code
     * var saved = repository.save(entity);
     * flushAndClear();  // DB에 반영 후 캐시 초기화
     * var found = repository.findById(saved.getId());  // 실제 DB에서 조회
     * }</pre>
     */
    protected void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    /**
     * 엔티티를 영속화하고 반환합니다.
     *
     * @param entity 영속화할 엔티티
     * @param <T> 엔티티 타입
     * @return 영속화된 엔티티
     */
    protected <T> T persist(T entity) {
        return testEntityManager.persist(entity);
    }

    /**
     * 엔티티를 영속화하고 flush합니다.
     *
     * @param entity 영속화할 엔티티
     * @param <T> 엔티티 타입
     * @return 영속화된 엔티티
     */
    protected <T> T persistAndFlush(T entity) {
        return testEntityManager.persistAndFlush(entity);
    }

    /**
     * ID로 엔티티를 조회합니다.
     *
     * @param entityClass 엔티티 클래스
     * @param id 엔티티 ID
     * @param <T> 엔티티 타입
     * @return 조회된 엔티티 (없으면 null)
     */
    protected <T> T find(Class<T> entityClass, Object id) {
        return testEntityManager.find(entityClass, id);
    }

    /**
     * 네이티브 SQL을 실행합니다.
     *
     * <p>테스트 데이터 초기화 등에 사용합니다.
     *
     * @param sql 실행할 SQL
     */
    protected void executeNativeSql(String sql) {
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
