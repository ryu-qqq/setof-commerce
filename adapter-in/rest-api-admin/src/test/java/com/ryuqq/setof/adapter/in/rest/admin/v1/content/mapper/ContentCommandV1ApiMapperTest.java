package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.in.rest.admin.content.ContentCommandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.ComponentDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.BlankComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.BrandComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.CategoryComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.ImageComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.ProductComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TabComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TextComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TitleComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.ViewExtensionCommand;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentCommandV1ApiMapper 단위 테스트.
 *
 * <p>v1 콘텐츠 Command API Mapper의 변환 로직을 검증합니다.
 *
 * <p>테스트 대상:
 *
 * <ul>
 *   <li>toRegisterCommand: displayYn → active, LocalDateTime → Instant, SubComponent 변환
 *   <li>toUpdateCommand: contentId 포함 변환
 *   <li>toChangeStatusCommand: contentId + displayYn 변환
 *   <li>SubComponent 다형성 타입별 specData JSON 생성
 *   <li>ComponentDetails → listType, orderType, badgeType, filterEnabled 변환
 *   <li>ViewExtensionDetails → ViewExtensionCommand 변환
 *   <li>null 컴포넌트 처리
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentCommandV1ApiMapper 단위 테스트")
class ContentCommandV1ApiMapperTest {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private ContentCommandV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ContentCommandV1ApiMapper(new ObjectMapper());
    }

    private Instant toKstInstant(LocalDateTime ldt) {
        return ldt.atZone(KST).toInstant();
    }

    // ===== toRegisterCommand =====

    @Nested
    @DisplayName("toRegisterCommand(CreateContentV1ApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("콘텐츠 기본 필드를 RegisterContentPageCommand로 변환한다")
        void toRegisterCommand_BasicFields_Success() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.title()).isEqualTo("메인 콘텐츠");
            assertThat(command.memo()).isEqualTo("테스트용 메모");
            assertThat(command.imageUrl()).isEqualTo("https://img.example.com/main.jpg");
        }

        @Test
        @DisplayName("displayYn 'Y'를 active=true로 변환한다")
        void toRegisterCommand_DisplayYnY_ActiveTrue() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("displayYn 'N'을 active=false로 변환한다")
        void toRegisterCommand_DisplayYnN_ActiveFalse() {
            // given
            CreateContentV1ApiRequest request =
                    ContentCommandV1ApiFixtures.createRequestWithDisplayYnN();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("LocalDateTime을 KST 기준 Instant로 변환한다")
        void toRegisterCommand_LocalDateTimeToKstInstant() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();
            Instant expectedStart = toKstInstant(ContentCommandV1ApiFixtures.DEFAULT_START);
            Instant expectedEnd = toKstInstant(ContentCommandV1ApiFixtures.DEFAULT_END);

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.displayStartAt()).isEqualTo(expectedStart);
            assertThat(command.displayEndAt()).isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("컴포넌트 목록이 비어있지 않으면 RegisterDisplayComponentCommand 목록이 생성된다")
        void toRegisterCommand_WithComponents_NotEmpty() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.components()).isNotEmpty();
        }

        @Test
        @DisplayName("컴포넌트 목록이 null이면 빈 리스트를 반환한다")
        void toRegisterCommand_NullComponents_EmptyList() {
            // given
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            null);

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.components()).isEmpty();
        }

        @Test
        @DisplayName("컴포넌트 목록이 빈 리스트이면 빈 리스트를 반환한다")
        void toRegisterCommand_EmptyComponents_EmptyList() {
            // given
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of());

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.components()).isEmpty();
        }
    }

    // ===== toUpdateCommand =====

    @Nested
    @DisplayName("toUpdateCommand(CreateContentV1ApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("contentId를 포함해 UpdateContentPageCommand로 변환한다")
        void toUpdateCommand_WithContentId_Success() {
            // given
            long contentId = 5L;
            CreateContentV1ApiRequest request =
                    ContentCommandV1ApiFixtures.updateRequest(contentId);

            // when
            UpdateContentPageCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.contentPageId()).isEqualTo(contentId);
            assertThat(command.title()).isEqualTo("수정된 콘텐츠");
            assertThat(command.memo()).isEqualTo("수정 메모");
        }

        @Test
        @DisplayName("displayYn 'N'을 active=false로 변환한다")
        void toUpdateCommand_DisplayYnN_ActiveFalse() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.updateRequest(10L);

            // when
            UpdateContentPageCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("LocalDateTime을 KST 기준 Instant로 변환한다")
        void toUpdateCommand_LocalDateTimeToKstInstant() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.updateRequest(3L);
            Instant expectedStart = toKstInstant(ContentCommandV1ApiFixtures.DEFAULT_START);
            Instant expectedEnd = toKstInstant(ContentCommandV1ApiFixtures.DEFAULT_END);

            // when
            UpdateContentPageCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.displayStartAt()).isEqualTo(expectedStart);
            assertThat(command.displayEndAt()).isEqualTo(expectedEnd);
        }
    }

    // ===== toChangeStatusCommand =====

    @Nested
    @DisplayName("toChangeStatusCommand(long, UpdateContentDisplayYnV1ApiRequest)")
    class ToChangeStatusCommandTest {

        @Test
        @DisplayName("contentId와 displayYn 'Y'를 ChangeContentPageStatusCommand(active=true)로 변환한다")
        void toChangeStatusCommand_DisplayYnY_ActiveTrue() {
            // given
            long contentId = 7L;
            UpdateContentDisplayYnV1ApiRequest request = ContentCommandV1ApiFixtures.displayYnY();

            // when
            ChangeContentPageStatusCommand command =
                    mapper.toChangeStatusCommand(contentId, request);

            // then
            assertThat(command.id()).isEqualTo(contentId);
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("contentId와 displayYn 'N'를 ChangeContentPageStatusCommand(active=false)로 변환한다")
        void toChangeStatusCommand_DisplayYnN_ActiveFalse() {
            // given
            long contentId = 9L;
            UpdateContentDisplayYnV1ApiRequest request = ContentCommandV1ApiFixtures.displayYnN();

            // when
            ChangeContentPageStatusCommand command =
                    mapper.toChangeStatusCommand(contentId, request);

            // then
            assertThat(command.id()).isEqualTo(contentId);
            assertThat(command.active()).isFalse();
        }
    }

    // ===== SubComponent specData 변환 =====

    @Nested
    @DisplayName("SubComponent 타입별 specData JSON 변환")
    class SpecDataTest {

        @Test
        @DisplayName("BrandComponent를 specData JSON으로 변환한다")
        void brandComponent_SpecData_ContainsBrandComponentId() {
            // given
            BrandComponentV1ApiRequest brand = ContentCommandV1ApiFixtures.brandComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(brand));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("brandComponentId");
            assertThat(specData).contains("categoryId");
            assertThat(specData).contains("brandList");
        }

        @Test
        @DisplayName("CategoryComponent를 specData JSON으로 변환한다")
        void categoryComponent_SpecData_ContainsCategoryComponentId() {
            // given
            CategoryComponentV1ApiRequest category =
                    ContentCommandV1ApiFixtures.categoryComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(category));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("categoryComponentId");
            assertThat(specData).contains("categoryId");
        }

        @Test
        @DisplayName("ProductComponent를 specData JSON으로 변환한다")
        void productComponent_SpecData_ContainsProductComponentId() {
            // given
            ProductComponentV1ApiRequest product = ContentCommandV1ApiFixtures.productComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(product));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("productComponentId");
        }

        @Test
        @DisplayName("TabComponent를 specData JSON으로 변환한다")
        void tabComponent_SpecData_ContainsTabComponentIdAndTabDetails() {
            // given
            TabComponentV1ApiRequest tab = ContentCommandV1ApiFixtures.tabComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(tab));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("tabComponentId");
            assertThat(specData).contains("tabDetails");
        }

        @Test
        @DisplayName("ImageComponent를 specData JSON으로 변환한다")
        void imageComponent_SpecData_ContainsImageTypeAndLinks() {
            // given
            ImageComponentV1ApiRequest image = ContentCommandV1ApiFixtures.imageComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(image));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("imageComponentId");
            assertThat(specData).contains("imageType");
            assertThat(specData).contains("imageComponentLinks");
        }

        @Test
        @DisplayName("TextComponent를 specData JSON으로 변환한다")
        void textComponent_SpecData_ContainsContent() {
            // given
            TextComponentV1ApiRequest text = ContentCommandV1ApiFixtures.textComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(text));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("textComponentId");
            assertThat(specData).contains("content");
        }

        @Test
        @DisplayName("TitleComponent를 specData JSON으로 변환한다")
        void titleComponent_SpecData_ContainsTitleFields() {
            // given
            TitleComponentV1ApiRequest title = ContentCommandV1ApiFixtures.titleComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(title));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("titleComponentId");
            assertThat(specData).contains("title1");
            assertThat(specData).contains("title2");
            assertThat(specData).contains("subTitle1");
            assertThat(specData).contains("subTitle2");
        }

        @Test
        @DisplayName("BlankComponent를 specData JSON으로 변환한다")
        void blankComponent_SpecData_ContainsHeightAndLineYn() {
            // given
            BlankComponentV1ApiRequest blank = ContentCommandV1ApiFixtures.blankComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(blank));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            String specData = command.components().get(0).specData();
            assertThat(specData).contains("blankComponentId");
            assertThat(specData).contains("height");
            assertThat(specData).contains("lineYn");
        }
    }

    // ===== ComponentDetails 변환 =====

    @Nested
    @DisplayName("ComponentDetails → RegisterDisplayComponentCommand 변환")
    class ComponentDetailsTest {

        @Test
        @DisplayName("ComponentDetails가 있으면 listType, orderType, badgeType을 매핑한다")
        void componentDetails_Mapped_Correctly() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);

            // then
            assertThat(comp.listType()).isEqualTo("GRID");
            assertThat(comp.orderType()).isEqualTo("POPULAR");
            assertThat(comp.badgeType()).isEqualTo("NEW");
        }

        @Test
        @DisplayName("ComponentDetails filterYn 'Y'를 filterEnabled=true로 변환한다")
        void componentDetails_FilterYnY_FilterEnabledTrue() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);

            // then
            assertThat(comp.filterEnabled()).isTrue();
        }

        @Test
        @DisplayName("ComponentDetails filterYn 'N'을 filterEnabled=false로 변환한다")
        void componentDetails_FilterYnN_FilterEnabledFalse() {
            // given
            ComponentDetailsV1ApiRequest detailsWithFilterN =
                    ContentCommandV1ApiFixtures.componentDetailsWithFilterN();
            BrandComponentV1ApiRequest brand =
                    new BrandComponentV1ApiRequest(
                            10L,
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "브랜드",
                            1,
                            "Y",
                            List.of(),
                            200L,
                            detailsWithFilterN,
                            5,
                            null,
                            null);
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(brand));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);

            // then
            assertThat(comp.filterEnabled()).isFalse();
        }

        @Test
        @DisplayName("ComponentDetails가 null이면 listType/orderType/badgeType이 null이다")
        void componentDetails_Null_NullFields() {
            // given
            TextComponentV1ApiRequest text = ContentCommandV1ApiFixtures.textComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(text));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);

            // then
            assertThat(comp.listType()).isNull();
            assertThat(comp.orderType()).isNull();
            assertThat(comp.badgeType()).isNull();
            assertThat(comp.filterEnabled()).isFalse();
        }
    }

    // ===== ViewExtensionDetails 변환 =====

    @Nested
    @DisplayName("ViewExtensionDetails → ViewExtensionCommand 변환")
    class ViewExtensionDetailsTest {

        @Test
        @DisplayName("ViewExtensionDetails가 있으면 ViewExtensionCommand로 변환한다")
        void viewExtensionDetails_Mapped_Correctly() {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);
            ViewExtensionCommand viewExt = comp.viewExtensionCommand();

            // then
            assertThat(viewExt).isNotNull();
            assertThat(viewExt.viewExtensionType()).isEqualTo("MORE_BUTTON");
            assertThat(viewExt.linkUrl()).isEqualTo("/products/more");
            assertThat(viewExt.buttonName()).isEqualTo("더보기");
            assertThat(viewExt.productCountPerClick()).isEqualTo(20);
            assertThat(viewExt.maxClickCount()).isEqualTo(5);
            assertThat(viewExt.afterMaxActionType()).isEqualTo("HIDE");
            assertThat(viewExt.afterMaxActionLinkUrl()).isEqualTo("/products/end");
        }

        @Test
        @DisplayName("ViewExtensionDetails가 null이면 ViewExtensionCommand가 null이다")
        void viewExtensionDetails_Null_NullCommand() {
            // given
            CategoryComponentV1ApiRequest category =
                    ContentCommandV1ApiFixtures.categoryComponent();
            CreateContentV1ApiRequest request =
                    new CreateContentV1ApiRequest(
                            null,
                            ContentCommandV1ApiFixtures.defaultDisplayPeriod(),
                            "제목",
                            null,
                            null,
                            "Y",
                            List.of(category));

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);
            RegisterDisplayComponentCommand comp = command.components().get(0);

            // then
            assertThat(comp.viewExtensionCommand()).isNull();
        }
    }

    // ===== 멀티 컴포넌트 =====

    @Nested
    @DisplayName("다수 컴포넌트 일괄 변환")
    class MultiComponentTest {

        @Test
        @DisplayName("8종 컴포넌트가 모두 변환되어 8개의 RegisterDisplayComponentCommand가 생성된다")
        void allComponentTypes_AllConverted() {
            // given
            CreateContentV1ApiRequest request =
                    ContentCommandV1ApiFixtures.createRequestWithMultipleComponents();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.components()).hasSize(8);
        }

        @Test
        @DisplayName("각 컴포넌트의 specData가 null이 아니다")
        void allComponentTypes_SpecDataNotNull() {
            // given
            CreateContentV1ApiRequest request =
                    ContentCommandV1ApiFixtures.createRequestWithMultipleComponents();

            // when
            RegisterContentPageCommand command = mapper.toRegisterCommand(request);

            // then
            command.components().forEach(comp -> assertThat(comp.specData()).isNotNull());
        }
    }
}
