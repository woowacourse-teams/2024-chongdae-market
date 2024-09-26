package com.zzang.chongdae.global.config;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(writerDatabase)")
    public void setDataSource(WriterDatabase writerDatabase) {
        DataSourceRouter.setDataSourceKey("write");
    }
}
