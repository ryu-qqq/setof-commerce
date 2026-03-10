package com.ryuqq.setof.domain.qna.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyAnsweredException;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyClosedException;
import com.ryuqq.setof.domain.qna.exception.QnaErrorCode;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaTitle;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Qna Aggregate 단위 테스트")
class QnaTest {

    @Nested
    @DisplayName("forNew() - 신규 Q&A 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 상품 Q&A를 생성한다")
        void createNewProductQnaWithRequiredFields() {
            // given
            LegacyMemberId legacyMemberId = QnaFixtures.defaultLegacyMemberId();
            QnaTitle title = QnaFixtures.defaultQnaTitle();
            QnaContent content = QnaFixtures.defaultQnaContent();
            Instant now = CommonVoFixtures.now();

            // when
            Qna qna = Qna.forNew(
                    legacyMemberId,
                    QnaFixtures.defaultMemberId(),
                    1L,
                    QnaType.PRODUCT,
                    QnaDetailType.SHIPMENT,
                    title,
                    content,
                    false,
                    now);

            // then
            assertThat(qna.isNew()).isTrue();
            assertThat(qna.legacyMemberId()).isEqualTo(legacyMemberId);
            assertThat(qna.title()).isEqualTo(title);
            assertThat(qna.content()).isEqualTo(content);
            assertThat(qna.status()).isEqualTo(QnaStatus.PENDING);
            assertThat(qna.isSecret()).isFalse();
            assertThat(qna.hasAnswer()).isFalse();
            assertThat(qna.isDeleted()).isFalse();
            assertThat(qna.isRoot()).isTrue();
            assertThat(qna.isProductQna()).isTrue();
            assertThat(qna.createdAt()).isEqualTo(now);
            assertThat(qna.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비밀글 Q&A를 생성한다")
        void createNewSecretQna() {
            // when
            Qna qna = Qna.forNew(
                    QnaFixtures.defaultLegacyMemberId(),
                    QnaFixtures.defaultMemberId(),
                    1L,
                    QnaType.PRODUCT,
                    QnaDetailType.SIZE,
                    QnaFixtures.defaultQnaTitle(),
                    QnaFixtures.defaultQnaContent(),
                    true,
                    CommonVoFixtures.now());

            // then
            assertThat(qna.isSecret()).isTrue();
            assertThat(qna.status()).isEqualTo(QnaStatus.PENDING);
        }

        @Test
        @DisplayName("주문 Q&A를 생성한다")
        void createNewOrderQna() {
            // when
            Qna qna = Qna.forNew(
                    QnaFixtures.defaultLegacyMemberId(),
                    QnaFixtures.defaultMemberId(),
                    1L,
                    QnaType.ORDER,
                    QnaDetailType.ORDER_PAYMENT,
                    QnaFixtures.defaultQnaTitle(),
                    QnaFixtures.defaultQnaContent(),
                    false,
                    CommonVoFixtures.now());

            // then
            assertThat(qna.isOrderQna()).isTrue();
            assertThat(qna.isProductQna()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 PENDING 상태의 Q&A를 복원한다")
        void reconstitutePendingQna() {
            // when
            Qna qna = QnaFixtures.pendingQna();

            // then
            assertThat(qna.isNew()).isFalse();
            assertThat(qna.isPending()).isTrue();
            assertThat(qna.isDeleted()).isFalse();
            assertThat(qna.hasAnswer()).isFalse();
        }

        @Test
        @DisplayName("영속성에서 ANSWERED 상태의 Q&A를 복원한다")
        void reconstituteAnsweredQna() {
            // when
            Qna qna = QnaFixtures.answeredQna();

            // then
            assertThat(qna.isAnswered()).isTrue();
            assertThat(qna.hasAnswer()).isTrue();
            assertThat(qna.answer()).isNotNull();
        }

        @Test
        @DisplayName("영속성에서 CLOSED 상태의 Q&A를 복원한다")
        void reconstituteClosedQna() {
            // when
            Qna qna = QnaFixtures.closedQna();

            // then
            assertThat(qna.isClosed()).isTrue();
        }

        @Test
        @DisplayName("영속성에서 삭제된 Q&A를 복원한다")
        void reconstituteDeletedQna() {
            // when
            Qna qna = QnaFixtures.deletedQna();

            // then
            assertThat(qna.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("영속성에서 대댓글 Q&A를 복원한다")
        void reconstituteFollowUpQna() {
            // when
            Qna qna = QnaFixtures.followUpQna();

            // then
            assertThat(qna.isFollowUp()).isTrue();
            assertThat(qna.isRoot()).isFalse();
            assertThat(qna.parentId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("createFollowUp() - 후속 질문 생성")
    class CreateFollowUpTest {

        @Test
        @DisplayName("원본 Q&A로부터 후속 질문을 생성한다")
        void createFollowUpFromRootQna() {
            // given
            Qna rootQna = QnaFixtures.pendingQna();
            LegacyQnaId parentId = rootQna.legacyId();
            QnaTitle followUpTitle = QnaTitle.of("추가 질문입니다");
            QnaContent followUpContent = QnaContent.of("추가로 여쭤보고 싶습니다.");
            Instant now = CommonVoFixtures.now();

            // when
            Qna followUp = rootQna.createFollowUp(
                    parentId,
                    QnaFixtures.defaultLegacyMemberId(),
                    QnaFixtures.defaultMemberId(),
                    followUpTitle,
                    followUpContent,
                    now);

            // then
            assertThat(followUp.isFollowUp()).isTrue();
            assertThat(followUp.parentId()).isEqualTo(parentId);
            assertThat(followUp.sellerId()).isEqualTo(rootQna.sellerId());
            assertThat(followUp.qnaType()).isEqualTo(rootQna.qnaType());
            assertThat(followUp.status()).isEqualTo(QnaStatus.PENDING);
            assertThat(followUp.title()).isEqualTo(followUpTitle);
            assertThat(followUp.content()).isEqualTo(followUpContent);
        }
    }

    @Nested
    @DisplayName("update() - Q&A 질문 수정")
    class UpdateTest {

        @Test
        @DisplayName("Q&A 질문 내용을 수정한다")
        void updateQnaContent() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            QnaUpdateData updateData = QnaFixtures.defaultQnaUpdateData();
            Instant now = CommonVoFixtures.now();

            // when
            qna.update(updateData, now);

            // then
            assertThat(qna.title()).isEqualTo(updateData.title());
            assertThat(qna.content()).isEqualTo(updateData.content());
            assertThat(qna.isSecret()).isEqualTo(updateData.secret());
            assertThat(qna.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비밀글로 수정한다")
        void updateQnaToSecret() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            QnaUpdateData updateData = QnaFixtures.secretQnaUpdateData();
            Instant now = CommonVoFixtures.now();

            // when
            qna.update(updateData, now);

            // then
            assertThat(qna.isSecret()).isTrue();
        }
    }

    @Nested
    @DisplayName("registerAnswer() - 답변 등록")
    class RegisterAnswerTest {

        @Test
        @DisplayName("PENDING 상태 Q&A에 답변을 등록한다")
        void registerAnswerToPendingQna() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            QnaContent answerContent = QnaFixtures.defaultAnswerContent();
            Instant now = CommonVoFixtures.now();

            // when
            qna.registerAnswer(answerContent, now);

            // then
            assertThat(qna.isAnswered()).isTrue();
            assertThat(qna.hasAnswer()).isTrue();
            assertThat(qna.answer()).isNotNull();
            assertThat(qna.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("이미 답변이 있는 Q&A에 답변을 등록하면 예외가 발생한다")
        void registerAnswerToAlreadyAnsweredQna() {
            // given
            Qna qna = QnaFixtures.answeredQna();

            // when & then
            assertThatThrownBy(() -> qna.registerAnswer(QnaFixtures.defaultAnswerContent(), CommonVoFixtures.now()))
                    .isInstanceOf(QnaAlreadyAnsweredException.class)
                    .satisfies(e -> {
                        QnaAlreadyAnsweredException ex = (QnaAlreadyAnsweredException) e;
                        assertThat(ex.getErrorCode()).isEqualTo(QnaErrorCode.QNA_ALREADY_ANSWERED);
                    });
        }

        @Test
        @DisplayName("종료된 Q&A에 답변을 등록하면 예외가 발생한다")
        void registerAnswerToClosedQna() {
            // given
            Qna qna = QnaFixtures.closedQna();

            // when & then
            assertThatThrownBy(() -> qna.registerAnswer(QnaFixtures.defaultAnswerContent(), CommonVoFixtures.now()))
                    .isInstanceOf(QnaAlreadyClosedException.class)
                    .satisfies(e -> {
                        QnaAlreadyClosedException ex = (QnaAlreadyClosedException) e;
                        assertThat(ex.getErrorCode()).isEqualTo(QnaErrorCode.QNA_ALREADY_CLOSED);
                    });
        }
    }

    @Nested
    @DisplayName("editAnswer() - 답변 수정")
    class EditAnswerTest {

        @Test
        @DisplayName("답변이 있는 Q&A의 답변을 수정한다")
        void editAnswerOfAnsweredQna() {
            // given
            Qna qna = QnaFixtures.answeredQna();
            QnaContent newContent = QnaContent.of("수정된 답변입니다.");
            Instant now = CommonVoFixtures.now();

            // when
            qna.editAnswer(newContent, now);

            // then
            assertThat(qna.answer().contentValue()).isEqualTo("수정된 답변입니다.");
            assertThat(qna.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("답변이 없는 Q&A의 답변을 수정하면 예외가 발생한다")
        void editAnswerOfQnaWithoutAnswer() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when & then
            assertThatThrownBy(() -> qna.editAnswer(QnaFixtures.defaultAnswerContent(), CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("답변이 존재하지 않아 수정할 수 없습니다");
        }

        @Test
        @DisplayName("종료된 Q&A의 답변을 수정하면 예외가 발생한다")
        void editAnswerOfClosedQna() {
            // given
            Qna qna = QnaFixtures.closedQna();

            // when & then
            assertThatThrownBy(() -> qna.editAnswer(QnaFixtures.defaultAnswerContent(), CommonVoFixtures.now()))
                    .isInstanceOf(QnaAlreadyClosedException.class);
        }
    }

    @Nested
    @DisplayName("deleteAnswer() - 답변 삭제")
    class DeleteAnswerTest {

        @Test
        @DisplayName("답변을 소프트 삭제하면 상태가 PENDING으로 변경된다")
        void deleteAnswerChangesStatusToPending() {
            // given
            Qna qna = QnaFixtures.answeredQna();
            Instant now = CommonVoFixtures.now();

            // when
            qna.deleteAnswer(now);

            // then
            assertThat(qna.hasAnswer()).isFalse();
            assertThat(qna.isPending()).isTrue();
            assertThat(qna.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("답변이 없는 Q&A의 답변을 삭제하면 예외가 발생한다")
        void deleteAnswerOfQnaWithoutAnswer() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when & then
            assertThatThrownBy(() -> qna.deleteAnswer(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("답변이 존재하지 않아 삭제할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("close() - Q&A 종료")
    class CloseTest {

        @Test
        @DisplayName("Q&A를 종료한다")
        void closeQna() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            Instant now = CommonVoFixtures.now();

            // when
            qna.close(now);

            // then
            assertThat(qna.isClosed()).isTrue();
            assertThat(qna.status()).isEqualTo(QnaStatus.CLOSED);
            assertThat(qna.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() - Q&A 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("Q&A를 소프트 삭제한다")
        void deleteQna() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            Instant now = CommonVoFixtures.now();

            // when
            qna.delete(now);

            // then
            assertThat(qna.isDeleted()).isTrue();
            assertThat(qna.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("needsMasking() - 비밀글 마스킹 여부 확인")
    class MaskingTest {

        @Test
        @DisplayName("비밀글이 아닌 Q&A는 마스킹이 불필요하다")
        void nonSecretQnaDoesNotNeedMasking() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            LegacyMemberId anyMemberId = LegacyMemberId.of(999L);

            // when & then
            assertThat(qna.needsMasking(anyMemberId)).isFalse();
        }

        @Test
        @DisplayName("비밀글 작성자 본인은 마스킹이 불필요하다")
        void secretQnaOwnerDoesNotNeedMasking() {
            // given
            Qna qna = QnaFixtures.secretQna();
            LegacyMemberId ownerMemberId = QnaFixtures.defaultLegacyMemberId();

            // when & then
            assertThat(qna.needsMasking(ownerMemberId)).isFalse();
        }

        @Test
        @DisplayName("비밀글 타인은 마스킹이 필요하다")
        void secretQnaNonOwnerNeedsMasking() {
            // given
            Qna qna = QnaFixtures.secretQna();
            LegacyMemberId otherMemberId = LegacyMemberId.of(999L);

            // when & then
            assertThat(qna.needsMasking(otherMemberId)).isTrue();
        }

        @Test
        @DisplayName("비밀글에서 작성자는 원본 제목을 볼 수 있다")
        void secretQnaOwnerCanSeeOriginalTitle() {
            // given
            Qna qna = QnaFixtures.secretQna();

            // when
            String title = qna.displayTitle(true, false);

            // then
            assertThat(title).isEqualTo(qna.titleValue());
        }

        @Test
        @DisplayName("비밀글에서 타인은 마스킹된 제목을 본다")
        void secretQnaNonOwnerSeeMaskedTitle() {
            // given
            Qna qna = QnaFixtures.secretQna();

            // when
            String title = qna.displayTitle(false, false);

            // then
            assertThat(title).isEqualTo("비밀글입니다");
        }

        @Test
        @DisplayName("비밀글에서 판매자는 원본 내용을 볼 수 있다")
        void secretQnaSellerCanSeeOriginalContent() {
            // given
            Qna qna = QnaFixtures.secretQna();

            // when
            String content = qna.displayContent(false, true);

            // then
            assertThat(content).isEqualTo(qna.contentValue());
        }
    }

    @Nested
    @DisplayName("isOwner() - 작성자 확인")
    class IsOwnerTest {

        @Test
        @DisplayName("작성자 본인이면 true를 반환한다")
        void ownerReturnsTrue() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            LegacyMemberId ownerMemberId = QnaFixtures.defaultLegacyMemberId();

            // when & then
            assertThat(qna.isOwner(ownerMemberId)).isTrue();
        }

        @Test
        @DisplayName("타인이면 false를 반환한다")
        void nonOwnerReturnsFalse() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            LegacyMemberId otherMemberId = LegacyMemberId.of(999L);

            // when & then
            assertThat(qna.isOwner(otherMemberId)).isFalse();
        }

        @Test
        @DisplayName("null viewerMemberId이면 false를 반환한다")
        void nullViewerMemberIdReturnsFalse() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when & then
            assertThat(qna.isOwner(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 QnaId 값을 반환한다")
        void idValueReturnsStringValue() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when
            String idValue = qna.idValue();

            // then
            assertThat(idValue).isEqualTo("qna-uuid-0001");
        }

        @Test
        @DisplayName("legacyIdValue()는 레거시 Q&A ID 값을 반환한다")
        void legacyIdValueReturnsLongValue() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when
            Long legacyIdValue = qna.legacyIdValue();

            // then
            assertThat(legacyIdValue).isEqualTo(1001L);
        }

        @Test
        @DisplayName("sellerId()는 판매자 ID를 반환한다")
        void sellerIdReturnsValue() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when & then
            assertThat(qna.sellerId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("titleValue()는 제목 문자열을 반환한다")
        void titleValueReturnsString() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when
            String titleValue = qna.titleValue();

            // then
            assertThat(titleValue).isEqualTo("배송은 언제 오나요?");
        }

        @Test
        @DisplayName("contentValue()는 내용 문자열을 반환한다")
        void contentValueReturnsString() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when
            String contentValue = qna.contentValue();

            // then
            assertThat(contentValue).isEqualTo("주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.");
        }

        @Test
        @DisplayName("신규 Q&A의 parentIdValue()는 null을 반환한다")
        void parentIdValueReturnsNullForRootQna() {
            // given
            Qna qna = QnaFixtures.pendingQna();

            // when & then
            assertThat(qna.parentIdValue()).isNull();
        }

        @Test
        @DisplayName("대댓글 Q&A의 parentIdValue()는 부모 ID를 반환한다")
        void parentIdValueReturnsParentIdForFollowUp() {
            // given
            Qna qna = QnaFixtures.followUpQna();

            // when & then
            assertThat(qna.parentIdValue()).isNotNull();
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("isProductQna()는 PRODUCT 타입이면 true를 반환한다")
        void isProductQnaReturnsTrueForProductType() {
            // given
            Qna qna = QnaFixtures.newProductQna();

            // then
            assertThat(qna.isProductQna()).isTrue();
            assertThat(qna.isOrderQna()).isFalse();
        }

        @Test
        @DisplayName("isOrderQna()는 ORDER 타입이면 true를 반환한다")
        void isOrderQnaReturnsTrueForOrderType() {
            // given
            Qna qna = QnaFixtures.newOrderQna();

            // then
            assertThat(qna.isOrderQna()).isTrue();
            assertThat(qna.isProductQna()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 Q&A는 isPending()이 true이다")
        void newQnaIsPending() {
            // given
            Qna qna = QnaFixtures.newProductQna();

            // then
            assertThat(qna.isPending()).isTrue();
            assertThat(qna.isAnswered()).isFalse();
            assertThat(qna.isClosed()).isFalse();
        }
    }
}
