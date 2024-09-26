package com.zzang.chongdae.global.config;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(writerDatabase)")
    public void setDataSource(WriterDatabase writerDatabase) {
        DataSourceRouter.setDataSourceKey("write");
    }
}
