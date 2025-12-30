package com.connectly.partnerAdmin.module.external.repository.product;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.core.BaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.core.QBaseInterlockingProduct;
import com.connectly.partnerAdmin.module.external.dto.product.*;
import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import com.connectly.partnerAdmin.module.external.filter.ExternalProductFilter;
import com.connectly.partnerAdmin.module.product.dto.image.QProductImageDto;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.brand.entity.QBrandMapping.brandMapping;
import static com.connectly.partnerAdmin.module.category.entity.QCategory.category;
import static com.connectly.partnerAdmin.module.category.entity.QCategoryMapping.categoryMapping;
import static com.connectly.partnerAdmin.module.crawl.entity.QCrawlProduct.crawlProduct;
import static com.connectly.partnerAdmin.module.external.entity.QExternalProduct.externalProduct;
import static com.connectly.partnerAdmin.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.connectly.partnerAdmin.module.product.entity.notice.QProductNotice.productNotice;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;


@Repository
@RequiredArgsConstructor
public class ExternalProductFetchRepositoryImpl implements ExternalProductFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExternalProduct> fetchExternalProductEntities(long productGroupId) {
        return queryFactory
                .selectFrom(externalProduct)
                .where(productGroupIdEq(productGroupId), externalIdxNotNull())
                .distinct()
                .fetch();
    }

    @Override
    public List<ExternalProduct> fetchExternalProductEntities(long siteId, List<Long> productGroupIds) {
        return queryFactory
                .selectFrom(externalProduct)
                .where(productGroupIdIn(productGroupIds), siteIdEq(siteId))
                .distinct()
                .fetch();
    }

    @Override
    public List<ExternalProductSiteDto> fetchHasSyncExternalProducts(List<Long> productGroupIds, List<Long> siteIds){
        return queryFactory
                .select(
                       new QExternalProductSiteDto(
                               externalProduct.productGroupId,
                               externalProduct.siteId
                       )
                )
                .from(externalProduct)
                .where(productGroupIdIn(productGroupIds), siteIdIn(siteIds))
                .distinct()
                .fetch();

    }

    @Override
    public List<ExternalProductMappingDto> fetchHasExistingExternalProducts(List<String> externalIds, long siteId){
        return queryFactory
                .select(
                        new QExternalProductMappingDto(
                                externalProduct.productGroupId,
                                externalProduct.externalIdx
                        )
                )
                .from(externalProduct)
                .where(externalIdxIn(externalIds), siteIdEq(siteId))
                .distinct()
                .fetch();

    }

    @Override
    public List<BaseInterlockingProduct> fetchProductMappings(long sellerId) {
        return  queryFactory
                .select(
                        new QBaseInterlockingProduct(
                                productGroup.id,
                                productGroup.productGroupDetails.sellerId
                        )
                )
                .from(productGroup)
                .where(sellerIdEq(sellerId), onStock())
                .distinct()
                .fetch();
    }

    @Override
    public Optional<ExternalProduct> fetchHasExternalProductEntity(long productGroupId, long siteId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(externalProduct)
                        .where(productGroupIdEq(productGroupId), siteIdEq(siteId))
                        .distinct()
                        .fetchOne()
        );
    }

    @Override
    public List<ExternalProductInfoDto> fetchExternalProductInfos(ExternalProductFilter filter, Pageable pageable) {
        //List<Long> productGroupIds = fetchExternalProductGroupIds(filter, pageable);

        List<Long> productGroupIds = List.of(501016L,
                501006L,
                500997L,
                500996L,
                500995L,
                500994L,
                500993L,
                500992L,
                500991L,
                500974L,
                500973L,
                500972L,
                500971L,
                500970L,
                500910L,
                500909L,
                500887L,
                500886L,
                500880L,
                500833L,
                500832L,
                500831L,
                500830L,
                500820L,
                500814L,
                500797L,
                500776L,
                500775L,
                500774L,
                500773L,
                500765L,
                500764L,
                500763L,
                500730L,
                500729L,
                500665L,
                500517L,
                500516L,
                500515L,
                500514L,
                500513L,
                500481L,
                500480L,
                500479L,
                500478L,
                500477L,
                500476L,
                500475L,
                500474L,
                500473L,
                500472L,
                500377L,
                500376L,
                500375L,
                500374L,
                500373L,
                500183L,
                500092L,
                500091L,
                500090L,
                500089L,
                500038L,
                500025L,
                500011L,
                500010L,
                500009L,
                499983L,
                499982L,
                499945L,
                499944L,
                499956L,
                499923L,
                499922L,
                499921L,
                499920L,
                499919L,
                499918L,
                499917L,
                499916L,
                499915L,
                499889L,
                499851L,
                499850L,
                499849L,
                499828L,
                499790L,
                499738L,
                499737L,
                499650L,
                499649L,
                499648L,
                499647L,
                499643L,
                499636L,
                499617L,
                499616L,
                499615L,
                499585L,
                499581L,
                499399L,
                499362L,
                499344L,
                499343L,
                499334L,
                499328L,
                499327L,
                499326L,
                499325L,
                499324L,
                499323L,
                499322L,
                499260L,
                499241L,
                499239L,
                499238L,
                499237L,
                499233L,
                499213L,
                499185L,
                499166L,
                499151L,
                499150L,
                499149L,
                499019L,
                499013L,
                499012L,
                498954L,
                498953L,
                498952L,
                498951L,
                498950L,
                498949L,
                498948L,
                498947L,
                498946L,
                498945L,
                498944L,
                498943L,
                498936L,
                498935L,
                498934L,
                498907L,
                498899L,
                498897L,
                498889L,
                498888L,
                498816L,
                498794L,
                498775L,
                498768L,
                498708L,
                498707L,
                498706L,
                498705L,
                498704L,
                498703L,
                498702L,
                498701L,
                498692L,
                498681L,
                498639L,
                498633L,
                498632L,
                498631L,
                498630L,
                498628L,
                498627L,
                498626L,
                498625L,
                498624L,
                498506L,
                498504L,
                498468L,
                498455L,
                498451L,
                498372L,
                498371L,
                498370L,
                498369L,
                498355L,
                498353L,
                498324L,
                498244L,
                498243L,
                498242L,
                498240L,
                498180L,
                498178L,
                498067L,
                497959L,
                497944L,
                497938L,
                497923L,
                497908L,
                497881L,
                497880L,
                497879L,
                497863L,
                497855L,
                497706L,
                497673L,
                497561L,
                497560L,
                497559L,
                497558L,
                497557L,
                497556L,
                497555L,
                497554L,
                497553L,
                497552L,
                497551L,
                497550L,
                497549L,
                497548L,
                497547L,
                497546L,
                497545L,
                497544L,
                497543L,
                497542L,
                497541L,
                497540L,
                497539L,
                497529L,
                497527L,
                497526L,
                497524L,
                497522L,
                497515L,
                497513L,
                497510L,
                497508L,
                497484L,
                497483L,
                497482L,
                497481L,
                497480L,
                497479L,
                497478L,
                497477L,
                497476L,
                497475L,
                497474L,
                497473L,
                497472L,
                497471L,
                497470L,
                497469L,
                497468L,
                497467L,
                497465L,
                497464L,
                497463L,
                497462L,
                497461L,
                497460L,
                497459L,
                497458L,
                497457L,
                497456L,
                497455L,
                497454L,
                497453L,
                497452L,
                497451L,
                497450L,
                497449L,
                497448L,
                497447L,
                497446L,
                497445L,
                497444L,
                497443L,
                497442L,
                497441L,
                497440L,
                497439L,
                497438L,
                497349L,
                497342L,
                497341L,
                497313L,
                497237L,
                497236L,
                497234L,
                497146L,
                497145L,
                497144L,
                497143L,
                497087L,
                497083L,
                497079L,
                497077L,
                497076L,
                497060L,
                497056L,
                497055L,
                497054L,
                497053L,
                497052L,
                497051L,
                497050L,
                497048L,
                497025L,
                496925L,
                496924L,
                496923L,
                496922L,
                496921L,
                496917L,
                496901L,
                496854L,
                496817L,
                496816L,
                496815L,
                496814L,
                496813L,
                496812L,
                496809L,
                496808L,
                496804L,
                496802L,
                495545L,
                495534L,
                495533L,
                495532L,
                495531L,
                495530L,
                495529L,
                494525L,
                493166L,
                493108L,
                493097L,
                493096L,
                493095L,
                492772L,
                492770L,
                492773L,
                492765L,
                492776L,
                492769L,
                492767L,
                492766L,
                492768L,
                492774L,
                492775L,
                492764L,
                492777L,
                492771L,
                492757L,
                492761L,
                492760L,
                492756L,
                492759L,
                492758L,
                492763L,
                492762L,
                492755L,
                492751L,
                492744L,
                492748L,
                492742L,
                492743L,
                492753L,
                492754L,
                492740L,
                492752L,
                492750L,
                492749L,
                492738L,
                492747L,
                492746L,
                492725L,
                492739L,
                492726L,
                492732L,
                492727L,
                492730L,
                492734L,
                492731L,
                492736L,
                492741L,
                492745L,
                492735L,
                492728L,
                492729L,
                492737L,
                492733L,
                492719L,
                492718L,
                492722L,
                492720L,
                492724L,
                492723L,
                492715L,
                492721L,
                492710L,
                492708L,
                492662L,
                492668L,
                492659L,
                492650L,
                492644L,
                492654L,
                492631L,
                492656L,
                492632L,
                492652L,
                492658L,
                492657L,
                492634L,
                492639L,
                492638L,
                492647L,
                492637L,
                492633L,
                492630L,
                492641L,
                492653L,
                492651L,
                492645L,
                492635L,
                492580L,
                492590L,
                492508L,
                492507L,
                492509L,
                492512L,
                492400L,
                492398L,
                492397L,
                492393L,
                492395L,
                492394L,
                492357L,
                492342L,
                492341L,
                492340L,
                492338L,
                492339L,
                492316L,
                492183L,
                492173L,
                492149L,
                492144L,
                492143L,
                492131L,
                492125L,
                492124L,
                492126L,
                492123L,
                492122L,
                492116L,
                492114L,
                491907L,
                491849L,
                491759L,
                491750L,
                491749L,
                491743L,
                491645L,
                491644L,
                491643L,
                491642L,
                491641L,
                491637L,
                491636L,
                491633L,
                491635L,
                491630L,
                491631L,
                491629L,
                491609L,
                491608L,
                491539L,
                491541L,
                491538L,
                491530L,
                491434L,
                491015L,
                491029L,
                491027L,
                491025L,
                491019L,
                491020L,
                491017L,
                491014L,
                490972L,
                490968L,
                490908L
        );

        if(productGroupIds.isEmpty()) return new ArrayList<>();

        return queryFactory
                .from(productGroup)
                .leftJoin(brandMapping)
                    .on(brandMapping.brandId.eq(productGroup.productGroupDetails.brandId))
                    .on(brandMapping.siteId.eq(filter.getSiteId()))
                .leftJoin(categoryMapping)
                    .on(categoryMapping.categoryId.eq(productGroup.productGroupDetails.categoryId))
                    .on(categoryMapping.siteId.eq(filter.getSiteId()))
                .innerJoin(category).on(category.id.eq(productGroup.productGroupDetails.categoryId))
                .innerJoin(brand).on(brand.id.eq(productGroup.productGroupDetails.brandId))

                .innerJoin(seller).on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .innerJoin(productGroup.productDelivery, productDelivery)
                .innerJoin(productGroup.productNotice, productNotice)
                .innerJoin(productGroup.images, productGroupImage)
                    .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(productGroup.detailDescription, productGroupDetailDescription)
                .leftJoin(crawlProduct).on(crawlProduct.productGroupId.eq(productGroup.id))
                .leftJoin(externalProduct)
                    .on(externalProduct.productGroupId.eq(productGroup.id))
                    .on(externalProduct.siteId.eq(filter.getSiteId()))
                .where(productGroup.id.in(productGroupIds))
                .transform(GroupBy.groupBy(productGroup.id).list(
                        new QExternalProductInfoDto(
                                externalProduct.id,
                                productGroup.id,
                                externalProduct.siteId,
                                externalProduct.mappingStatus,
                                externalProduct.externalIdx.coalesce(""),
                                productGroup.productGroupDetails.productGroupName,
                                seller.id,
                                seller.sellerName,
                                productGroup.productGroupDetails.brandId,
                                brand.brandName,
                                brandMapping.mappingBrandId.coalesce(""),
                                productGroup.productGroupDetails.categoryId,
                                category.path.coalesce(""),
                                categoryMapping.mappingCategoryId.coalesce(""),
                                categoryMapping.description.coalesce(""),
                                category.categoryType,
                                category.targetGroup,
                                productGroup.productGroupDetails.optionType,
                                productGroup.productGroupDetails.managementType,
                                productGroup.productGroupDetails.price,
                                productGroup.productGroupDetails.clothesDetailInfo,
                                productDelivery.deliveryNotice,
                                productDelivery.refundNotice,
                                productNotice.noticeDetail,
                                productGroup.productGroupDetails.productStatus,
                                crawlProduct.colorCode.coalesce(""),
                                externalProduct.fixedPrice.coalesce(BigDecimal.ZERO),
                                GroupBy.set(
                                        new QProductImageDto(
                                                productGroupImage.imageDetail.productGroupImageType,
                                                productGroupImage.imageDetail.imageUrl
                                        )
                                ),
                                productGroupDetailDescription.imageDetail.imageUrl
                        )));
    }


    private List<Long> fetchExternalProductGroupIds(ExternalProductFilter filter, Pageable pageable) {
        return queryFactory
                .select(externalProduct.productGroupId)
                .from(externalProduct)
                .innerJoin(productGroup)
                    .on(productGroup.id.eq(externalProduct.productGroupId))
                .where(
                        siteIdEq(filter.getSiteId()), mappingStatusEq(filter.getMappingStatus()),
                        externalIdxIsNull(filter.getExternalIdIsNull()), isExternalIdLt(filter)
                )
                .orderBy(externalProduct.updateDate.desc(), externalProduct.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression isExternalIdLt(ExternalProductFilter filter){
        if(filter.getLastDomainId() !=null) return externalProduct.id.lt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression productGroupIdEq(long productGroupId){
        return externalProduct.productGroupId.eq(productGroupId);
    }

    private BooleanExpression externalIdxNotNull(){
        return externalProduct.externalIdx.isNotNull();
    }

    private BooleanExpression productGroupIdIn(List<Long> productGroupIds){
        if(!productGroupIds.isEmpty()) return externalProduct.productGroupId.in(productGroupIds);
        return null;
    }

    private BooleanExpression externalIdxIn(List<String> externalIdx){
        if(!externalIdx.isEmpty()) return externalProduct.externalIdx.in(externalIdx);
        return null;
    }

    private BooleanExpression mappingStatusEq(MappingStatus mappingStatus){
        return externalProduct.mappingStatus.eq(mappingStatus);
    }

    private BooleanExpression externalIdxIsNull(Yn externalIdxIsNull){
        if(externalIdxIsNull.isYes()) return externalProduct.externalIdx.isNull();
        else return externalIdxNotNull();
    }


    private BooleanExpression siteIdEq(long siteId){
        return externalProduct.siteId.eq(siteId);
    }

    private BooleanExpression siteIdIn(List<Long> siteIds){
        return externalProduct.siteId.in(siteIds);
    }

    private BooleanExpression sellerIdEq(long sellerId){
        return productGroup.productGroupDetails.sellerId.eq(sellerId);
    }

    private BooleanExpression onStock(){
        return productGroup.productGroupDetails.productStatus.soldOutYn.eq(Yn.N)
                .and(productGroup.productGroupDetails.productStatus.displayYn.eq(Yn.Y));
    }

}
