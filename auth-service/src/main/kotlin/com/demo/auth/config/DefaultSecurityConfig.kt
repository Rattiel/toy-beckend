package com.demo.auth.config

import com.demo.auth.web.LoginParameterNames
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.util.*

@Slf4j
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class DefaultSecurityConfig {
    companion object {
        const val LOGIN_URL: String = "/login"
    }

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { httpBasic ->
                httpBasic.disable()
            }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/favicon.*", "/assets/**", "/robots.txt").permitAll()
                authorize.requestMatchers("/", "/error").permitAll()
                authorize.anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin.usernameParameter(LoginParameterNames.USERNAME)
                formLogin.passwordParameter(LoginParameterNames.PASSWORD)
                formLogin.successHandler(SavedRequestAwareAuthenticationSuccessHandler())
                formLogin.failureHandler(null)
                formLogin.authenticationDetailsSource(WebAuthenticationDetailsSource())
                formLogin.permitAll()
            }
            .exceptionHandling { exception ->
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
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider {
        return DaoAuthenticationProvider(passwordEncoder).apply {
            setUserDetailsService(userDetailsService)
        }
    }

    @Bean
    fun userService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val admin = User.withUsername("admin")
            .password("1234")
            .passwordEncoder(passwordEncoder::encode)
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(admin)
    }
}