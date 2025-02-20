package com.demo.auth.config

import com.demo.auth.web.resolver.RedirectUrlResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(RedirectUrlResolver(DefaultSecurityConfig.REQUEST_CACHE))
    }
}