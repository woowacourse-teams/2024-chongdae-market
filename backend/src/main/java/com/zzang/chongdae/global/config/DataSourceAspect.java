package com.zzang.chongdae.global.config;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional) && @annotation(transactional)")
    public void setDataSource(Transactional transactional) {
        if (transactional.readOnly()) {
            DataSourceRouter.setDataSourceKey("read");
            return;
        }
        DataSourceRouter.setDataSourceKey("write");
    }
}
