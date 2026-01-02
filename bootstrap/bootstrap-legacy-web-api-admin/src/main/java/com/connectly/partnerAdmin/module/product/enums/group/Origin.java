package com.connectly.partnerAdmin.module.product.enums.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

@Getter
@RequiredArgsConstructor
public enum Origin implements EnumType {

    AE("United Arab Emirates", "아랍 에미리트"),
    AF("Afghanistan", "아프가니스탄"),
    AL("Albania", "알바니아"),
    AM("Armenia", "아르메니아"),
    AR("Argentina", "아르헨티나"),
    AT("Austria", "오스트리아"),
    AU("Australia", "호주"),
    BA("Bosnia and Herzegovina", "보스니아 헤르체고비나"),
    BD("Bangladesh", "방글라데시"),
    BE( "Belgium", "벨기에"),
    BG( "Bulgaria", "불가리아"),
    BO( "Bolivia", "볼리비아"),
    BR( "Brazil", "브라질"),
    BY( "Belarus", "벨라루스"),
    CA( "Canada", "캐나다"),
    CH( "Switzerland", "스위스"),
    CL("Chile", "칠레"),
    CM("Cameroon", "카메룬"),
    CN("China", "중국"),
    CO("Colombia", "콜롬비아"),
    CR("Costa Rica", "코스타리카"),
    CS("Montenegro", "몬테네그로"),
    CU("Cuba", "쿠바"),
    CY("Cyprus", "키프로스"),
    CZ("Czechia", "체코"),
    DE("Germany", "독일"),
    DK("Denmark", "덴마크"),
    DM("Dominica", "도미니카"),
    DO("Dominican Republic", "도미니카 공화국"),
    DZ("Algeria", "알제리"),
    EC("Ecuado아r", "에콰도르"),
    EE("Estonia", "에스토니아"),
    EG("Egypt", "이집트"),
    ES("Spain", "스페인"),
    FI("Finland", "핀란드"),
    FR("France", "프랑스"),
    GB("Great Britain", "영국"),
    GE("Georgia", "조지아"),
    GR("Greece", "그리스"),
    GU("Guam", "괌"),
    GT("Guatemala", "과테말라"),
    HK("Hong Kong", "홍콩"),
    HN("Honduras", "온두라스"),
    HR("Croatia", "크로아티아"),
    HT("Haiti", "아이티"),
    HU("Hungary", "헝가리"),
    ID("Indonesia", "인도네시아"),
    IE("Ireland", "아일랜드"),
    IL("Israel", "이스라엘"),
    IN("India", "인도"),
    IR("Iran", "이란"),
    IS("Iceland", "아이슬란드"),
    IT("Italy", "이탈리아"),
    JE("Jersey", "저지"),
    JM("Jamaica", "자메이카"),
    JO("Jordan", "요르단"),
    JP("Japan", "일본"),
    KE("Kenya", "케냐"),
    KG("Kyrgyzstan", "키르기스스탄"),
    KH("Cambodia", "캄보디아"),
    KM("Comoros", "코모로스"),
    KP("North Korea", "북한"),
    KR("South Korea", "대한민국"),
    KW("Kuwait", "쿠웨이트"),
    KZ("Kazakhstan", "카자흐스탄"),
    LA("Laos", "라오스"),
    LB("Lebanon", "레바논"),
    LK("Sri Lanka", "스리랑카"),
    LR("Liberia", "라이베리아"),
    LS("Lesotho", "레소토"),
    LT("Lithuania", "리투아니아"),
    LU("Luxembourg", "룩셈부르크"),
    LV("Latvia", "라트비아"),
    LY("Libya", "리비아"),
    MA("Morocco", "모로코"),
    MC("Monaco", "모나코"),
    MD("Moldova", "몰도바"),
    MF("Saint Martin", "세인트마틴"),
    MG("Madagascar", "마다가스카르"),
    MK("Macedonia", "마케도니아"),
    ML("Mali", "말리"),
    MM("Myanmar [Burma]", "미얀마"),
    MN("Mongolia", "몽골"),
    MO("Macao", "마카오"),
    MR("Mauritania", "모리타니"),
    MS("Montserrat", "몬트세라트"),
    MT("Malta", "몰타"),
    MU("Mauritius", "모리셔스"),
    MV("Maldives", "몰디브"),
    MW("Malawi", "말라위"),
    MX("Mexico", "멕시코"),
    MY("Malaysia", "말레이시아"),
    NC("New Caledonia", "뉴칼레도니아"),
    NG("Nigeria", "나이지리아"),
    NL("Netherlands", "네덜란드"),
    NO("Norway", "노르웨이"),
    NP("Nepal", "네팔"),
    NZ("New Zealand", "뉴질랜드"),
    PE("Peru", "페루"),
    PH("Philippines", "필리핀"),
    PK("Pakistan", "파키스탄"),
    PL("Poland", "폴란드"),
    PR("Puerto Rico", "푸에르토리코"),
    PS("Palestine", "팔레스타인"),
    PT("Portugal", "포르투갈"),
    PY("Paraguay", "파라과이"),
    QA("Qatar", "카타르"),
    RO("Romania", "루마니아"),
    RS("Serbia", "세르비아"),
    RU("Russia", "러시아"),
    SA("Saudi Arabia", "사우디아라비아"),
    SD("Sudan", "수단"),
    SE("Sweden", "스웨덴"),
    SC("Scotland","스코틀랜드"),
    SG("Singapore", "싱가포르"),
    SI("Slovenia", "슬로베니아"),
    SK("Slovakia", "슬로바키아"),
    SL("Sierra Leone", "시에라리온"),
    SM("San Marino", "산마리노"),
    SN("Senegal", "세네갈"),
    SO("Somalia", "소말리아"),
    SS("South Sudan", "남수단"),
    SY("Syria", "시리아"),
    TG("Togo", "토고"),
    TH("Thailand", "태국"),
    TL("East Timor", "동티모르"),
    TN("Tunisia", "튀니지"),
    TR("Turkey", "튀르키예"),
    TW("Taiwan", "대만"),
    UA("Ukraine", "우크라이나"),
    UG("Uganda", "우간다"),
    UK("United Kingdom", "영국"),
    US("United States", "미국"),
    UY("Uruguay", "우루과이"),
    UZ("Uzbekistan", "우즈베키스탄"),
    VE("Venezuela", "베네수엘라"),
    VN("Vietnam", "베트남"),
    ZA("South Africa", "남아프리카"),
    ZM("Zambia", "잠비아"),
    ZZ("OTHER", "기타"),
    NI("Nicaragua", "니카과라"),
    OTHER("OTHER", "기타");


    private final String code;
    private final String displayName;

    public static Origin from(String name) {
        return Arrays.stream(Origin.values())
                .filter(r -> r.name().equals(name))
                .findAny()
                .orElseGet(()-> Origin.OTHER);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return code + "/" + displayName;
    }
}


