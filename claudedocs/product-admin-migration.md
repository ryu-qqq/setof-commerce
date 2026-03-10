# 상품 그룹 Admin API 마이그레이션 계획

> MarketPlace(OMS) → setof-commerce(자사몰) rest-api-admin v2 상품 관련 엔드포인트 구축

## 참고 프로젝트
- **소스**: `/Users/sangwon-ryu/MarketPlace` (adapter-in/rest-api, application, adapter-out/persistence-mysql)
- **타겟**: `/Users/sangwon-ryu/setof-commerce` (rest-api-admin v2, application, persistence-mysql)

## 자사몰 특성 (MarketPlace 대비 제거/단순화)
- ❌ MarketAccessChecker / @PreAuthorize / @RequirePermission 제거 (내부 서버간 호출)
- ❌ 이미지 업로드 세션 (Fileflow) 불필요 → URL 직접 저장
- ❌ 배치 등록/엑셀 등록 제외
- ❌ Publish/CDN 관련 로직 제외 (Description publishStatus 등)
- ❌ Outbox 관련 composite 쿼리 제외
- ✅ canonicalOptionGroupId 유지 (OMS 연동 필요)

## 엔드포인트 목록

### ProductGroup Command (rest-api-admin v2)
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/v2/admin/product-groups | 상품그룹 등록 (registerProductGroup) |
| PUT | /api/v2/admin/product-groups/{id} | 전체 수정 (updateProductGroupFull) |
| PATCH | /api/v2/admin/product-groups/{id}/basic-info | 기본정보 수정 (updateBasicInfo) |

### Product Command (rest-api-admin v2)
| Method | Path | 설명 |
|--------|------|------|
| PATCH | /api/v2/admin/products/{id}/price | 가격 수정 (updatePrice) |
| PATCH | /api/v2/admin/products/{id}/stock | 재고 수정 (updateStock) |
| PATCH | /api/v2/admin/products/product-groups/{productGroupId} | 상품 일괄 수정 (updateProducts) |

### ProductGroupDescription Command
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/v2/admin/product-groups/{id}/description | 상세설명 등록 |
| PUT | /api/v2/admin/product-groups/{id}/description | 상세설명 수정 |

### ProductGroupImage Command
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/v2/admin/product-groups/{id}/images | 이미지 등록 |
| PUT | /api/v2/admin/product-groups/{id}/images | 이미지 수정 |

### ProductNotice Command
| Method | Path | 설명 |
|--------|------|------|
| POST | /api/v2/admin/product-groups/{id}/notice | 고시정보 등록 |
| PUT | /api/v2/admin/product-groups/{id}/notice | 고시정보 수정 |

---

## Phase 1: persistence-mysql (신규 DB)

### 1.1 productgroup (MarketPlace 참고)
| 파일 | 상태 | 비고 |
|------|------|------|
| entity/ProductGroupJpaEntity.java | ✅ | @Table("product_groups"), extends BaseAuditEntity, +price 필드 |
| entity/SellerOptionGroupJpaEntity.java | ✅ | @Table("seller_option_groups"), canonicalOptionGroupId 유지 |
| entity/SellerOptionValueJpaEntity.java | ✅ | @Table("seller_option_values"), canonicalOptionValueId 유지 |
| mapper/ProductGroupJpaEntityMapper.java | ✅ | setof-commerce domain reconstitute() 사용 |
| repository/ProductGroupJpaRepository.java | ✅ | JPA write only |
| repository/ProductGroupQueryDslRepository.java | ✅ | QueryDSL read only |
| repository/SellerOptionGroupJpaRepository.java | ✅ | |
| repository/SellerOptionGroupQueryDslRepository.java | ✅ | |
| repository/SellerOptionValueJpaRepository.java | ✅ | |
| condition/ProductGroupConditionBuilder.java | ✅ | 동적 쿼리 조건 |
| adapter/ProductGroupCommandAdapter.java | ✅ | ProductGroupCommandPort 구현 |
| adapter/ProductGroupQueryAdapter.java | ✅ | ProductGroupQueryPort 구현 |
| adapter/SellerOptionGroupCommandAdapter.java | ✅ | |
| adapter/SellerOptionGroupQueryAdapter.java | ✅ | |
| adapter/SellerOptionValueCommandAdapter.java | ✅ | |

### 1.2 product
| 파일 | 상태 | 비고 |
|------|------|------|
| entity/ProductJpaEntity.java | ✅ | @Table("products"), extends BaseAuditEntity |
| entity/ProductOptionMappingJpaEntity.java | ✅ | @Table("product_option_mappings") |
| mapper/ProductJpaEntityMapper.java | ✅ | DeletionStatus.reconstitute() 사용 |
| repository/ProductJpaRepository.java | ✅ | |
| repository/ProductQueryDslRepository.java | ✅ | 7개 쿼리 메서드 |
| repository/ProductOptionMappingJpaRepository.java | ✅ | deleteByProductId |
| condition/ProductConditionBuilder.java | ✅ | |
| adapter/ProductCommandAdapter.java | ✅ | |
| adapter/ProductQueryAdapter.java | ✅ | batch loading 패턴 |
| adapter/ProductOptionMappingCommandAdapter.java | ✅ | |

### 1.3 productgroupdescription
| 파일 | 상태 | 비고 |
|------|------|------|
| entity/ProductGroupDescriptionJpaEntity.java | ✅ | @Table("product_group_descriptions"), no publishStatus |
| entity/DescriptionImageJpaEntity.java | ✅ | @Table("description_images"), single imageUrl |
| mapper/ProductGroupDescriptionJpaEntityMapper.java | ✅ | |
| repository/ProductGroupDescriptionJpaRepository.java | ✅ | |
| repository/ProductGroupDescriptionQueryDslRepository.java | ✅ | |
| repository/DescriptionImageJpaRepository.java | ✅ | deleteByProductGroupDescriptionId |
| repository/DescriptionImageQueryDslRepository.java | ✅ | |
| condition/ProductGroupDescriptionConditionBuilder.java | ✅ | |
| adapter/ProductGroupDescriptionCommandAdapter.java | ✅ | |
| adapter/ProductGroupDescriptionQueryAdapter.java | ✅ | assemble 패턴 |
| adapter/DescriptionImageCommandAdapter.java | ✅ | |

### 1.4 productgroupimage
| 파일 | 상태 | 비고 |
|------|------|------|
| entity/ProductGroupImageJpaEntity.java | ✅ | @Table("product_group_images"), single imageUrl |
| mapper/ProductGroupImageJpaEntityMapper.java | ✅ | toEntity에 productGroupId 파라미터 |
| repository/ProductGroupImageJpaRepository.java | ✅ | deleteByProductGroupId |
| repository/ProductGroupImageQueryDslRepository.java | ✅ | |
| adapter/ProductGroupImageCommandAdapter.java | ✅ | |
| adapter/ProductGroupImageQueryAdapter.java | ✅ | |

### 1.5 productnotice
| 파일 | 상태 | 비고 |
|------|------|------|
| entity/ProductNoticeJpaEntity.java | ✅ | @Table("product_notices"), no noticeCategoryId |
| entity/ProductNoticeEntryJpaEntity.java | ✅ | @Table("product_notice_entries"), +fieldName, +sortOrder |
| mapper/ProductNoticeJpaEntityMapper.java | ✅ | NoticeFieldValue.of(fieldName, fieldValue) |
| repository/ProductNoticeJpaRepository.java | ✅ | |
| repository/ProductNoticeEntryJpaRepository.java | ✅ | deleteByProductNoticeId |
| repository/ProductNoticeQueryDslRepository.java | ✅ | |
| condition/ProductNoticeConditionBuilder.java | ✅ | |
| adapter/ProductNoticeCommandAdapter.java | ✅ | |
| adapter/ProductNoticeQueryAdapter.java | ✅ | assemble 패턴 |
| adapter/ProductNoticeEntryCommandAdapter.java | ✅ | |

---

## Phase 2: application (Command UseCase/Service/Port/Manager)

### 2.1 productgroup command
| 파일 | 상태 | 비고 |
|------|------|------|
| port/in/command/RegisterProductGroupFullUseCase.java | ✅ | |
| port/in/command/UpdateProductGroupFullUseCase.java | ✅ | |
| port/in/command/UpdateProductGroupBasicInfoUseCase.java | ✅ | |
| port/out/command/ProductGroupCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/command/SellerOptionGroupCommandPort.java | ✅ | selleroption 패키지 |
| port/out/command/SellerOptionValueCommandPort.java | ✅ | selleroption 패키지 |
| port/out/query/ProductGroupQueryPort.java | ✅ | Phase 1에서 생성 |
| port/out/query/SellerOptionGroupQueryPort.java | ✅ | selleroption 패키지 |
| dto/command/RegisterProductGroupCommand.java | ✅ | 중첩 record |
| dto/command/UpdateProductGroupFullCommand.java | ✅ | ID 필드 추가 |
| dto/command/UpdateProductGroupBasicInfoCommand.java | ✅ | |
| dto/bundle/ProductGroupRegistrationBundle.java | ✅ | |
| dto/bundle/ProductGroupUpdateBundle.java | ✅ | |
| service/command/RegisterProductGroupFullService.java | ✅ | BundleFactory + Coordinator |
| service/command/UpdateProductGroupFullService.java | ✅ | BundleFactory + Coordinator |
| service/command/UpdateProductGroupBasicInfoService.java | ✅ | ReadManager + CommandFactory |
| manager/ProductGroupCommandManager.java | ✅ | @Component @Transactional |
| manager/ProductGroupReadManager.java | ✅ | @Transactional(readOnly=true) |
| internal/FullProductGroupRegistrationCoordinator.java | ✅ | PersistFacade 위임 |
| internal/FullProductGroupUpdateCoordinator.java | ✅ | PersistFacade 위임 |
| internal/ProductGroupPersistFacade.java | ✅ | 전체 하위도메인 조율 |
| internal/ProductGroupCommandCoordinator.java | ✅ | 기본정보 수정 조율 |
| factory/ProductGroupCommandFactory.java | ✅ | ProductGroupUpdateData 생성 |
| factory/ProductGroupBundleFactory.java | ✅ | Registration/Update 번들 |
| selleroption/manager/SellerOptionGroupCommandManager.java | ✅ | |
| selleroption/manager/SellerOptionValueCommandManager.java | ✅ | |
| selleroption/manager/SellerOptionGroupReadManager.java | ✅ | 기존 존재 |

### 2.2 product command
| 파일 | 상태 | 비고 |
|------|------|------|
| port/in/command/UpdateProductPriceUseCase.java | ✅ | |
| port/in/command/UpdateProductStockUseCase.java | ✅ | |
| port/in/command/UpdateProductsUseCase.java | ✅ | |
| port/out/command/ProductCommandPort.java | ✅ | |
| port/out/command/ProductOptionMappingCommandPort.java | ✅ | |
| port/out/query/ProductQueryPort.java | ✅ | |
| dto/command/UpdateProductPriceCommand.java | ✅ | |
| dto/command/UpdateProductStockCommand.java | ✅ | |
| dto/command/UpdateProductsCommand.java | ✅ | 내부 record 3개 |
| dto/command/SelectedOption.java | ✅ | |
| dto/command/RegisterProductsCommand.java | ✅ | |
| service/command/UpdateProductPriceService.java | ✅ | |
| service/command/UpdateProductStockService.java | ✅ | |
| service/command/UpdateProductsService.java | ✅ | Products VO diff 기반 |
| manager/ProductCommandManager.java | ✅ | |
| manager/ProductOptionMappingCommandManager.java | ✅ | |
| manager/ProductReadManager.java | ✅ | ProductNotFoundException |
| internal/ProductCommandCoordinator.java | ✅ | Products.update() → ProductDiff |
| factory/ProductCommandFactory.java | ✅ | 옵션 이름 resolve |

### 2.3 productdescription command
| 파일 | 상태 | 비고 |
|------|------|------|
| port/in/command/RegisterProductGroupDescriptionUseCase.java | ✅ | |
| port/in/command/UpdateProductGroupDescriptionUseCase.java | ✅ | |
| port/out/command/ProductGroupDescriptionCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/command/DescriptionImageCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/query/ProductGroupDescriptionQueryPort.java | ✅ | Phase 1에서 생성 |
| dto/command/RegisterProductGroupDescriptionCommand.java | ✅ | imageUrl (단일) |
| dto/command/UpdateProductGroupDescriptionCommand.java | ✅ | |
| service/command/RegisterProductGroupDescriptionService.java | ✅ | |
| service/command/UpdateProductGroupDescriptionService.java | ✅ | |
| manager/ProductGroupDescriptionCommandManager.java | ✅ | |
| manager/ProductGroupDescriptionReadManager.java | ✅ | |
| manager/DescriptionImageCommandManager.java | ✅ | |
| internal/DescriptionCommandCoordinator.java | ✅ | |
| internal/DescriptionCommandFacade.java | ✅ | |
| factory/ProductGroupDescriptionCommandFactory.java | ✅ | cdnPath=null |

### 2.4 productgroupimage command
| 파일 | 상태 | 비고 |
|------|------|------|
| port/in/command/RegisterProductGroupImagesUseCase.java | ✅ | |
| port/in/command/UpdateProductGroupImagesUseCase.java | ✅ | |
| port/out/command/ProductGroupImageCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/query/ProductGroupImageQueryPort.java | ✅ | Phase 1에서 생성 |
| dto/command/RegisterProductGroupImagesCommand.java | ✅ | imageUrl (단일) |
| dto/command/UpdateProductGroupImagesCommand.java | ✅ | |
| service/command/RegisterProductGroupImagesService.java | ✅ | |
| service/command/UpdateProductGroupImagesService.java | ✅ | |
| manager/ProductGroupImageCommandManager.java | ✅ | |
| manager/ProductGroupImageReadManager.java | ✅ | |
| internal/ImageCommandCoordinator.java | ✅ | |
| factory/ProductGroupImageFactory.java | ✅ | |

### 2.5 productnotice command
| 파일 | 상태 | 비고 |
|------|------|------|
| port/in/command/RegisterProductNoticeUseCase.java | ✅ | |
| port/in/command/UpdateProductNoticeUseCase.java | ✅ | |
| port/out/command/ProductNoticeCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/command/ProductNoticeEntryCommandPort.java | ✅ | Phase 1에서 생성 |
| port/out/query/ProductNoticeQueryPort.java | ✅ | Phase 1에서 생성 |
| dto/command/RegisterProductNoticeCommand.java | ✅ | fieldName + fieldValue |
| dto/command/UpdateProductNoticeCommand.java | ✅ | |
| service/command/RegisterProductNoticeService.java | ✅ | |
| service/command/UpdateProductNoticeService.java | ✅ | |
| manager/ProductNoticeCommandManager.java | ✅ | |
| manager/ProductNoticeEntryCommandManager.java | ✅ | |
| manager/ProductNoticeReadManager.java | ✅ | |
| internal/ProductNoticeCommandCoordinator.java | ✅ | |
| internal/ProductNoticeCommandFacade.java | ✅ | assignId 전파 |
| factory/ProductNoticeCommandFactory.java | ✅ | NoticeFieldValue.of |

---

## Phase 3: rest-api-admin v2 (Controller/DTO/Mapper/Endpoints)

### 3.1 productgroup
| 파일 | 상태 | 비고 |
|------|------|------|
| ProductGroupAdminEndpoints.java | ✅ | /api/v2/admin/product-groups |
| controller/ProductGroupCommandController.java | ✅ | register(201), updateFull(204), updateBasicInfo(204) |
| dto/command/RegisterProductGroupApiRequest.java | ✅ | 중첩 record 9개, price 포함 |
| dto/command/UpdateProductGroupFullApiRequest.java | ✅ | optional ID 필드 |
| dto/command/UpdateProductGroupBasicInfoApiRequest.java | ✅ | |
| dto/response/ProductGroupIdApiResponse.java | ✅ | |
| mapper/ProductGroupCommandApiMapper.java | ✅ | optionType 자동 추론 |
| error/ProductGroupErrorMapper.java | ✅ | |

### 3.2 product
| 파일 | 상태 | 비고 |
|------|------|------|
| ProductAdminEndpoints.java | ✅ | /api/v2/admin/products |
| controller/ProductCommandController.java | ✅ | PATCH 3개, 204 반환 |
| dto/command/UpdateProductPriceApiRequest.java | ✅ | |
| dto/command/UpdateProductStockApiRequest.java | ✅ | |
| dto/command/UpdateProductsApiRequest.java | ✅ | 중첩 record 4개 |
| mapper/ProductCommandApiMapper.java | ✅ | |
| error/ProductErrorMapper.java | ✅ | |

### 3.3 productgroupdescription
| 파일 | 상태 | 비고 |
|------|------|------|
| ProductGroupDescriptionAdminEndpoints.java | ✅ | /{productGroupId}/description |
| controller/ProductGroupDescriptionCommandController.java | ✅ | POST(201), PUT(200) |
| dto/command/RegisterProductGroupDescriptionApiRequest.java | ✅ | imageUrl (단일) |
| dto/command/UpdateProductGroupDescriptionApiRequest.java | ✅ | |
| mapper/ProductGroupDescriptionCommandApiMapper.java | ✅ | |
| error/ProductGroupDescriptionErrorMapper.java | ✅ | |

### 3.4 productgroupimage
| 파일 | 상태 | 비고 |
|------|------|------|
| ProductGroupImageAdminEndpoints.java | ✅ | /{productGroupId}/images |
| controller/ProductGroupImageCommandController.java | ✅ | POST(201), PUT(200) |
| dto/command/RegisterProductGroupImagesApiRequest.java | ✅ | imageUrl (단일) |
| dto/command/UpdateProductGroupImagesApiRequest.java | ✅ | |
| mapper/ProductGroupImageCommandApiMapper.java | ✅ | |
| error/ProductGroupImageErrorMapper.java | ✅ | |

### 3.5 productnotice
| 파일 | 상태 | 비고 |
|------|------|------|
| ProductNoticeAdminEndpoints.java | ✅ | /{productGroupId}/notice |
| controller/ProductNoticeCommandController.java | ✅ | POST(201), PUT(200) |
| dto/command/RegisterProductNoticeApiRequest.java | ✅ | fieldName+fieldValue, no noticeCategoryId |
| dto/command/UpdateProductNoticeApiRequest.java | ✅ | |
| mapper/ProductNoticeCommandApiMapper.java | ✅ | |
| error/ProductNoticeErrorMapper.java | ✅ | |

---

## 패턴 규칙 (setof-commerce 컨벤션)

### Entity
- extends SoftDeletableEntity (BaseAuditEntity > createdAt, updatedAt + deletedAt)
- Lombok 금지, getter 수동 작성
- JPA 관계 어노테이션 금지 (@OneToMany 등)
- static factory method: create()
- protected 기본 생성자

### Repository
- JpaRepository: write only (save, saveAll)
- QueryDslRepository: read only (JPAQueryFactory + ConditionBuilder)
- notDeleted() 조건 항상 포함

### Adapter
- CommandAdapter: JpaRepository + Mapper, implements CommandPort
- QueryAdapter: QueryDslRepository + Mapper, implements QueryPort
- @Transactional 금지

### Mapper
- @Component, toEntity() / toDomain() 양방향
- Domain.reconstitute() 사용하여 복원

### ConditionBuilder
- @Component, BooleanExpression 반환 (null → 동적 무시)

---

## 패키지 구조

```
# persistence-mysql
com.ryuqq.setof.adapter.out.persistence.{domain}
  ├── entity/
  ├── repository/
  ├── mapper/
  ├── adapter/
  └── condition/

# application
com.ryuqq.setof.application.{domain}
  ├── port/in/command/
  ├── port/out/command/
  ├── port/out/query/
  ├── dto/command/
  ├── dto/bundle/
  ├── service/command/
  ├── manager/
  ├── internal/
  ├── factory/
  └── validator/

# rest-api-admin
com.ryuqq.setof.adapter.in.rest.admin.v2.{domain}
  ├── controller/
  ├── dto/command/
  ├── dto/response/
  ├── mapper/
  └── error/
```
