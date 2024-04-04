package org.jeecg.modules.im.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //多语言
        registry.addInterceptor(new LocaleChangeInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieName("locale");//LocaleChangeInterceptor 默认会获取url参数 locale
        return cookieLocaleResolver;
    }
}