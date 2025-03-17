package com.zzang.chongdae.comment.domain;

import java.util.Arrays;

public enum SearchDirection {
    NEXT,
    PREVIOUS;

    public static SearchDirection of(String direction) {
        return Arrays.stream(SearchDirection.values())
                .filter(searchDirection -> searchDirection.name().equals(direction))
                .findFirst()
                .orElseGet(() -> SearchDirection.PREVIOUS);
    }
}
