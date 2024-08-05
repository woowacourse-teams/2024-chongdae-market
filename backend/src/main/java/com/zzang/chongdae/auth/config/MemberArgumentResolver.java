package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.controller.CookieConsumer;
import com.zzang.chongdae.auth.service.AuthService;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@AllArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final CookieConsumer cookieConsumer;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberEntity.class);
    }

    @Override
    public MemberEntity resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest,
                                        WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = cookieConsumer.getAccessToken(request.getCookies());
        return authService.findMemberByToken(token);
    }
}
