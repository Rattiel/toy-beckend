package com.demo.auth.config

import com.demo.auth.authorization.login.LoginRedirectHandler
import com.demo.auth.authorization.login.MfaRedirectHandler
import com.demo.auth.authorization.mfa.MfaAuthenticationProvider
import com.demo.auth.authorization.mfa.MfaAuthenticationRetryHandler
import com.demo.auth.authorization.mfa.filter.MfaAuthenticationFilter
import com.demo.auth.authorization.mfa.filter.RetrieveMfaAuthenticationFilter
import com.demo.auth.authorization.mfa.token.ConsoleMfaCodePublisher
import com.demo.auth.authorization.mfa.token.DelegateMfaCodePublisher
import com.demo.auth.authorization.mfa.token.MfaTokenAuthenticationProvider
import com.demo.auth.authorization.LoginParameterNames
import com.demo.auth.web.service.InmemoryUserService
import com.demo.auth.web.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.savedrequest.CookieRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.util.*

@Slf4j
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class DefaultSecurityConfig {
    companion object {
        const val LOGIN_URL: String = "/login"
        const val MFA_URL: String = "/mfa"
        const val PASSWORD_CHANGE_URL: String = "/not-support"
        val REQUEST_CACHE: RequestCache = CookieRequestCache()
        val SESSION_CONTEXT_HOLDER_STRATEGY: SecurityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
        val SESSION_CONTEXT_REPOSITORY: SecurityContextRepository =
            DelegatingSecurityContextRepository(
                HttpSessionSecurityContextRepository()
            )
    }

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { httpBasic ->
                httpBasic.disable()
            }
            .requestCache { requestCache ->
                requestCache.requestCache(REQUEST_CACHE)
            }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/favicon.*", "/assets/**", "/robots.txt").permitAll()
                authorize.requestMatchers("/", "/error").permitAll()
                authorize.requestMatchers(MFA_URL).hasAuthority("SCOPE_MFA_AUTHENTICATION_NEED")
                authorize.anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin.loginPage(LOGIN_URL)
                formLogin.loginProcessingUrl(LOGIN_URL)
                formLogin.usernameParameter(LoginParameterNames.USERNAME)
                formLogin.passwordParameter(LoginParameterNames.PASSWORD)
                formLogin.successHandler(MfaRedirectHandler().apply {
                    this.setMfaPage(MFA_URL)
                    this.setRequestCache(REQUEST_CACHE)
                })
                formLogin.failureHandler(null)
                formLogin.securityContextRepository(SESSION_CONTEXT_REPOSITORY)
                formLogin.authenticationDetailsSource(WebAuthenticationDetailsSource())
                formLogin.permitAll()
            }
            .securityContext { securityContext ->
                securityContext.securityContextRepository(SESSION_CONTEXT_REPOSITORY)
            }
            .passwordManagement { passwordManagement ->
                passwordManagement.changePasswordPage(PASSWORD_CHANGE_URL)
            }
            .addFilterBefore(
                MfaAuthenticationFilter(
                    ProviderManager(
                        MfaTokenAuthenticationProvider()
                    )
                ).apply {
                    this.setSecurityContextHolderStrategy(SESSION_CONTEXT_HOLDER_STRATEGY)
                    this.setSecurityContextRepository(SESSION_CONTEXT_REPOSITORY)
                    this.setAuthenticationSuccessHandler(SavedRequestAwareAuthenticationSuccessHandler().apply {
                        this.setRequestCache(REQUEST_CACHE)
                    })
                    this.setAuthenticationFailureHandler(
                        MfaAuthenticationRetryHandler("$MFA_URL?error").apply {
                            this.setLoginPage(LOGIN_URL)
                        }
                    )
                    this.setFilterProcessesUrl(MFA_URL)
                },
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterBefore(
                RetrieveMfaAuthenticationFilter().apply {
                    this.setMfaPage(MFA_URL)
                    this.setMfaProcessingUrl(MFA_URL)
                    this.setSecurityContextHolderStrategy(SESSION_CONTEXT_HOLDER_STRATEGY)
                    this.setSecurityContextRepository(SESSION_CONTEXT_REPOSITORY)
                },
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling { exception ->
                exception.defaultAccessDeniedHandlerFor(
                    LoginRedirectHandler().apply {
                        this.setLoginFormUrl(LOGIN_URL)
                        this.setRequestCache(REQUEST_CACHE)
                        this.setSecurityContextHolderStrategy(SESSION_CONTEXT_HOLDER_STRATEGY)
                        this.setSecurityContextRepository(SESSION_CONTEXT_REPOSITORY)
                    },
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
                exception.defaultAuthenticationEntryPointFor(
                    LoginUrlAuthenticationEntryPoint(LOGIN_URL),
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(
        userService: UserService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider {
        return MfaAuthenticationProvider(
            DaoAuthenticationProvider(passwordEncoder).apply {
                setUserDetailsService(userService)
                setUserDetailsPasswordService(userService)
            }
        ).apply {
            setPublisher(
                DelegateMfaCodePublisher(
                    ConsoleMfaCodePublisher()
                )
            )
        }
    }

    @Bean
    fun userService(passwordEncoder: PasswordEncoder): UserService {
        val admin = User.withUsername("admin")
            .password("1234")
            .passwordEncoder(passwordEncoder::encode)
            .roles("ADMIN")
            .build()
        return InmemoryUserService(admin)
    }
}