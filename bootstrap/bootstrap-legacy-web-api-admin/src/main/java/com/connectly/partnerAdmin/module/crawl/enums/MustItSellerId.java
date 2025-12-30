package com.connectly.partnerAdmin.module.crawl.enums;

import java.util.HashMap;
import java.util.Map;

public enum MustItSellerId {
    SELLER_1559("wdrobe",1559, 25),
    SELLER_2397("viaitalia", 2397, 23),
    SELLER_1403("LIKEASTAR",1403, 12),
    SELLER_1116("bino2345",1116, 4),
    SELLER_2638("thefactor2",2638, 20),
    SELLER_8372("ccapsule1",8372, 7),
    SELLER_1903("fixedone", 1903, 9),
    SELLER_1619("italiagom",1619, 10);

    private static final Map<Integer, MustItSellerId> BY_ORIGINAL_ID = new HashMap<>();
    private static final Map<String, MustItSellerId> BY_NAME_ID = new HashMap<>();
    private static final Map<Integer, Integer> MAPPED_ID_BY_ORIGINAL = new HashMap<>();

    static {
        for (MustItSellerId seller : values()) {
            BY_ORIGINAL_ID.put(seller.originalId, seller);
            BY_NAME_ID.put(seller.name, seller);
            MAPPED_ID_BY_ORIGINAL.put(seller.originalId, seller.mappedId);
        }
    }

    private final String name;
    private final int originalId;
    private final int mappedId;

    MustItSellerId(String name, int originalId, int mappedId) {
        this.name = name;
        this.originalId = originalId;
        this.mappedId = mappedId;
    }

    public String getName() {
        return name;
    }

    public int getOriginalId() {
        return originalId;
    }

    public int getMappedId() {
        return mappedId;
    }

    public static MustItSellerId fromOriginalId(int originalId) {
        return BY_ORIGINAL_ID.get(originalId);
    }

    public static MustItSellerId fromOriginalId(String name) {
        return BY_NAME_ID.get(name);
    }

    public static Integer getMappedId(int originalId) {
        return MAPPED_ID_BY_ORIGINAL.get(originalId);
    }
}