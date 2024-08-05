package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.controller.CookieConsumer;
import com.zzang.chongdae.auth.service.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final CookieConsumer cookieConsumer;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authService, cookieConsumer));
    }
}
