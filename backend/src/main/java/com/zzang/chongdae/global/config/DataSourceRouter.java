package com.zzang.chongdae.global.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }
}
