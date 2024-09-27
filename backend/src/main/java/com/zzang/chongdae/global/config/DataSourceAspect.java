package com.zzang.chongdae.global.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(writerDatabase)")
    public void setWriterDataSource(WriterDatabase writerDatabase) {
        log.debug("DataSourceAspect - Before advice: Switching to 'write' data source");
        DataSourceRouter.setDataSourceKey("write");
    }

    @After("@annotation(writerDatabase)")
    public void setReaderDataSource(WriterDatabase writerDatabase) {
        log.debug("DataSourceAspect - After advice: Switching back to 'read' data source");
        DataSourceRouter.setDataSourceKey("read");
    }
}
