package com.ryuqq.setof.application.productnotice.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import com.ryuqq.setof.application.productnotice.ProductNoticeCommandFixtures;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.factory.ProductNoticeCommandFactory;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeCommandManager;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeReadManager;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.exception.ProductNoticeNotFoundException;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductNoticeCommandCoordinator 단위 테스트")
class ProductNoticeCommandCoordinatorTest {

    @InjectMocks private ProductNoticeCommandCoordinator sut;

    @Mock private ProductNoticeCommandFactory noticeCommandFactory;
    @Mock private ProductNoticeCommandManager noticeCommandManager;
    @Mock private ProductNoticeReadManager noticeReadManager;

    @Nested
    @DisplayName("register(RegisterProductNoticeCommand) - Command로 Notice 등록")
    class RegisterByCommandTest {

        @Test
        @DisplayName("Command로 ProductNotice를 생성하고 저장한다")
        void register_ValidCommand_ReturnsNoticeId() {
            // given
            RegisterProductNoticeCommand command = ProductNoticeCommandFixtures.registerCommand();
            ProductNotice notice = ProductNoticeFixtures.newNotice();
            Long expectedId = 1L;

            given(noticeCommandFactory.create(command)).willReturn(notice);
            given(noticeCommandManager.persist(notice)).willReturn(expectedId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(noticeCommandFactory).should().create(command);
            then(noticeCommandManager).should().persist(notice);
        }
    }

    @Nested
    @DisplayName("register(ProductNotice) - 도메인 객체로 Notice 등록")
    class RegisterByDomainTest {

        @Test
        @DisplayName("ProductNotice 도메인 객체를 저장하고 ID를 반환한다")
        void register_ProductNotice_ReturnsNoticeId() {
            // given
            ProductNotice notice = ProductNoticeFixtures.newNotice();
            Long expectedId = 1L;

            given(noticeCommandManager.persist(notice)).willReturn(expectedId);

            // when
            Long result = sut.register(notice);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(noticeCommandManager).should().persist(notice);
            then(noticeCommandFactory).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("update() - Command로 Notice 수정")
    class UpdateTest {

        @Test
        @DisplayName("기존 Notice를 조회하여 수정 데이터를 적용하고 저장한다")
        void update_ValidCommand_UpdatesAndPersistsNotice() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand();
            ProductGroupId pgId = ProductGroupId.of(command.productGroupId());
            ProductNotice existing = ProductNoticeFixtures.activeNotice();
            ProductNoticeUpdateData updateData = ProductNoticeFixtures.defaultUpdateData();

            given(noticeReadManager.getByProductGroupId(pgId)).willReturn(existing);
            given(noticeCommandFactory.createUpdateData(command)).willReturn(updateData);

            // when
            sut.update(command);

            // then
            then(noticeReadManager).should().getByProductGroupId(pgId);
            then(noticeCommandFactory).should().createUpdateData(command);
            then(noticeCommandManager).should().persist(existing);
        }

        @Test
        @DisplayName("Notice가 존재하지 않으면 ProductNoticeNotFoundException이 발생하고 저장되지 않는다")
        void update_NoticeNotFound_ThrowsExceptionAndDoesNotPersist() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand(999L);
            ProductGroupId pgId = ProductGroupId.of(999L);

            willThrow(new ProductNoticeNotFoundException(pgId))
                    .given(noticeReadManager)
                    .getByProductGroupId(pgId);

            // when & then
            assertThatThrownBy(() -> sut.update(command))
                    .isInstanceOf(ProductNoticeNotFoundException.class);

            then(noticeCommandFactory).should(never()).createUpdateData(command);
            then(noticeCommandManager).should(never()).persist(org.mockito.ArgumentMatchers.any());
        }
    }

    @Nested
    @DisplayName("persist() - ProductNotice 직접 저장")
    class PersistTest {

        @Test
        @DisplayName("ProductNotice를 직접 저장하고 ID를 반환한다")
        void persist_ValidNotice_ReturnsNoticeId() {
            // given
            ProductNotice notice = ProductNoticeFixtures.newNotice();
            Long expectedId = 1L;

            given(noticeCommandManager.persist(notice)).willReturn(expectedId);

            // when
            Long result = sut.persist(notice);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(noticeCommandManager).should().persist(notice);
            then(noticeCommandFactory).shouldHaveNoInteractions();
            then(noticeReadManager).shouldHaveNoInteractions();
        }
    }
}
