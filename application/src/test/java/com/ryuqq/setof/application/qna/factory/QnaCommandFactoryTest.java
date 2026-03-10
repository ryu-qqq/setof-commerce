package com.ryuqq.setof.application.qna.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaCommandFactory 단위 테스트")
class QnaCommandFactoryTest {

    private QnaCommandFactory sut;

    @BeforeEach
    void setUp() {
        sut = new QnaCommandFactory();
    }

    @Nested
    @DisplayName("createProductBundle() - 상품 Q&A 번들 생성")
    class CreateProductBundleTest {

        @Test
        @DisplayName("상품 Q&A 커맨드를 ProductQnaBundle로 변환한다")
        void createProductBundle_ValidCommand_ReturnsProductQnaBundle() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();

            // when
            ProductQnaBundle bundle = sut.createProductBundle(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.qna()).isNotNull();
            assertThat(bundle.qnaProduct()).isNotNull();
        }

        @Test
        @DisplayName("생성된 Qna의 qnaType이 PRODUCT이다")
        void createProductBundle_QnaType_IsProduct() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();

            // when
            ProductQnaBundle bundle = sut.createProductBundle(command);

            // then
            assertThat(bundle.qna().qnaType()).isEqualTo(QnaType.PRODUCT);
        }

        @Test
        @DisplayName("비밀글 커맨드로 생성 시 Qna의 secret이 true이다")
        void createProductBundle_SecretCommand_QnaIsSecret() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommandSecret();

            // when
            ProductQnaBundle bundle = sut.createProductBundle(command);

            // then
            assertThat(bundle.qna().isSecret()).isTrue();
        }
    }

    @Nested
    @DisplayName("createOrderBundle() - 주문 Q&A 번들 생성")
    class CreateOrderBundleTest {

        @Test
        @DisplayName("주문 Q&A 커맨드를 OrderQnaBundle로 변환한다")
        void createOrderBundle_ValidCommand_ReturnsOrderQnaBundle() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();

            // when
            OrderQnaBundle bundle = sut.createOrderBundle(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.qna()).isNotNull();
            assertThat(bundle.qnaOrder()).isNotNull();
            assertThat(bundle.images()).isNotNull();
        }

        @Test
        @DisplayName("생성된 Qna의 qnaType이 ORDER이다")
        void createOrderBundle_QnaType_IsOrder() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();

            // when
            OrderQnaBundle bundle = sut.createOrderBundle(command);

            // then
            assertThat(bundle.qna().qnaType()).isEqualTo(QnaType.ORDER);
        }

        @Test
        @DisplayName("이미지 없는 주문 Q&A 커맨드로 생성 시 images가 비어있다")
        void createOrderBundle_WithoutImages_BundleHasEmptyImages() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();

            // when
            OrderQnaBundle bundle = sut.createOrderBundle(command);

            // then
            assertThat(bundle.images().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("이미지 있는 주문 Q&A 커맨드로 생성 시 images가 포함된다")
        void createOrderBundle_WithImages_BundleHasImages() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommandWithImages();

            // when
            OrderQnaBundle bundle = sut.createOrderBundle(command);

            // then
            assertThat(bundle.images().isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("createUpdateContext() - Q&A 수정 UpdateContext 생성")
    class CreateUpdateContextTest {

        @Test
        @DisplayName("ModifyQnaCommand를 UpdateContext로 변환한다")
        void createUpdateContext_ValidCommand_ReturnsUpdateContext() {
            // given
            ModifyQnaCommand command = QnaCommandFixtures.modifyQnaCommand();

            // when
            UpdateContext<Long, QnaUpdateData> context = sut.createUpdateContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id()).isEqualTo(command.qnaId());
            assertThat(context.changedAt()).isNotNull();
        }

        @Test
        @DisplayName("UpdateContext의 updateData에 커맨드 정보가 반영된다")
        void createUpdateContext_UpdateData_ReflectsCommand() {
            // given
            ModifyQnaCommand command = QnaCommandFixtures.modifyQnaCommand();

            // when
            UpdateContext<Long, QnaUpdateData> context = sut.createUpdateContext(command);

            // then
            assertThat(context.updateData()).isNotNull();
            assertThat(context.updateData().secret()).isEqualTo(command.secret());
        }
    }

    @Nested
    @DisplayName("createImages() - QnaImages 생성")
    class CreateImagesTest {

        @Test
        @DisplayName("이미지 URL 목록으로 QnaImages를 생성한다")
        void createImages_ValidUrls_ReturnsQnaImages() {
            // given
            List<String> imageUrls = List.of(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg");

            // when
            QnaImages images = sut.createImages(imageUrls);

            // then
            assertThat(images).isNotNull();
            assertThat(images.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("빈 URL 목록으로 empty QnaImages를 생성한다")
        void createImages_EmptyUrls_ReturnsEmptyQnaImages() {
            // given
            List<String> imageUrls = List.of();

            // when
            QnaImages images = sut.createImages(imageUrls);

            // then
            assertThat(images.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("null URL 목록으로 empty QnaImages를 생성한다")
        void createImages_NullUrls_ReturnsEmptyQnaImages() {
            // when
            QnaImages images = sut.createImages(null);

            // then
            assertThat(images.isEmpty()).isTrue();
        }
    }
}
