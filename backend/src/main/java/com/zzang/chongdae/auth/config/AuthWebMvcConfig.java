package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.controller.CookieConsumer;
import com.zzang.chongdae.auth.service.AuthTokenManager;
import com.zzang.chongdae.auth.service.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {

    private final AuthTokenManager authTokenManager;
    private final CookieConsumer cookieConsumer;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authTokenManager, cookieConsumer));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(cookieConsumer, jwtTokenProvider))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/swagger-config",
                        "/static/swagger-ui/openapi3.yaml",
                        "/health-check",
                        "/read-only/**",
                        "/favicon.ico"
                );
    }
}
