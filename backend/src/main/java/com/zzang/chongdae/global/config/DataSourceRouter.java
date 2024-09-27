package com.zzang.chongdae.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DataSourceRouter extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Object object = contextHolder.get();
        log.info("datasourceRounter에서 look up key로 사용할 datasource를 결정한다: {}", object);
        return object;
    }
}
