package com.demo.auth.config

import com.demo.auth.authorization.core.LoginRedirectHandler
import com.demo.auth.authorization.core.mfa.filter.RetrieveMfaAuthenticationFilter
import com.demo.auth.authorization.jackson2.AuthServerJackson2Module
import com.demo.auth.authorization.oauth.OAuthLoginUrlAuthenticationEntryPoint
import com.demo.auth.authorization.oauth.jose.Jwks
import com.demo.auth.authorization.oauth.token.DelegatingOAuth2TokenCustomizer
import com.demo.auth.authorization.oauth.token.IdTokenCustomizer
import com.demo.auth.config.DefaultSecurityConfig.Companion.LOGIN_URL
import com.demo.auth.config.DefaultSecurityConfig.Companion.MFA_URL
import com.demo.auth.config.DefaultSecurityConfig.Companion.REQUEST_CACHE
import com.demo.auth.config.DefaultSecurityConfig.Companion.SESSION_CONTEXT_HOLDER_STRATEGY
import com.demo.auth.config.DefaultSecurityConfig.Companion.SESSION_CONTEXT_REPOSITORY
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.jackson2.CoreJackson2Module
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.*
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.time.Duration
import java.util.*

@Slf4j
@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig(val jdbcTemplate: JdbcTemplate) {
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val authorizationServerConfigurer: OAuth2AuthorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer()

        http
            .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .with(authorizationServerConfigurer) { authorizationServer ->
                authorizationServer.authorizationEndpoint(Customizer.withDefaults())
                authorizationServer.oidc(Customizer.withDefaults())
                authorizationServer.tokenEndpoint(Customizer.withDefaults())
            }
            .requestCache { requestCache ->
                requestCache.disable()
            }
            .authorizeHttpRequests { authorize ->
                authorize.anyRequest().authenticated()
            }
            .addFilterAfter(
                RequestCacheAwareFilter(REQUEST_CACHE),
                LogoutFilter::class.java
            )
            .addFilterAfter(
                RetrieveMfaAuthenticationFilter().apply {
                    this.setMfaPage(MFA_URL)
                    this.setMfaProcessingUrl(MFA_URL)
                    this.setSecurityContextHolderStrategy(SESSION_CONTEXT_HOLDER_STRATEGY)
                    this.setSecurityContextRepository(SESSION_CONTEXT_REPOSITORY)
                },
                LogoutFilter::class.java
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
                    OAuthLoginUrlAuthenticationEntryPoint(LOGIN_URL)
                        .apply {
                        this.setRequestCache(REQUEST_CACHE)
                    },
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            }
        return http.build()
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:9000")
            .build()
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        // With Refresh token, Code flow
        val messagingClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
            .redirectUri("http://127.0.0.1:8080/authorized")
            .redirectUri("https://oauth.pstmn.io/v1/callback")
            .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope("message.read")
            .scope("message.write")
            .scope("user.read")
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofMinutes(60))
                    .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                    .reuseRefreshTokens(false)
                    .refreshTokenTimeToLive(Duration.ofDays(60))
                    .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                    .build()
            )
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .build()
            )
            .build()

        // Without Refresh token, Code flow with PKCE
        val deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("device-messaging-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("message.read")
            .scope("message.write")
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
            .redirectUri("http://127.0.0.1:8080/authorized")
            .redirectUri("https://oauth.pstmn.io/v1/callback")
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .requireProofKey(true)
                    .build()
            )
            .build()

        return JdbcRegisteredClientRepository(this.jdbcTemplate).apply {
            save(messagingClient)
            save(deviceClient)
        }
    }

    private fun authorizationRowMapper(
        registeredClientRepository: RegisteredClientRepository
    ): RowMapper<OAuth2Authorization> {
        val authorizationServiceClassLoader = JdbcOAuth2AuthorizationService::class.java.classLoader
        val objectMapper = ObjectMapper().apply {
            this.registerModules(SecurityJackson2Modules.getModules(authorizationServiceClassLoader))
            this.registerModules(
                CoreJackson2Module(),
                OAuth2AuthorizationServerJackson2Module(),
                AuthServerJackson2Module()
            )
        }
        return OAuth2AuthorizationRowMapper(registeredClientRepository).apply {
            this.setObjectMapper(objectMapper)
        }
    }

    @Bean
    fun authorizationService(
        registeredClientRepository: RegisteredClientRepository,
    ): OAuth2AuthorizationService {
        return JdbcOAuth2AuthorizationService(this.jdbcTemplate, registeredClientRepository).apply {
            this.setAuthorizationRowMapper(authorizationRowMapper(registeredClientRepository))
        }
    }

    @Bean
    fun authorizationConsentService(
        registeredClientRepository: RegisteredClientRepository
    ): OAuth2AuthorizationConsentService {
        return JdbcOAuth2AuthorizationConsentService(this.jdbcTemplate, registeredClientRepository)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = Jwks.generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, _ ->
            jwkSelector.select(jwkSet)
        }
    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun tokenGenerator(
        jwtEncoder: JwtEncoder,
        oauth2TokenCustomizer: OAuth2TokenCustomizer<JwtEncodingContext>
    ): OAuth2TokenGenerator<OAuth2Token> {
        return DelegatingOAuth2TokenGenerator(
            JwtGenerator(jwtEncoder).apply {
                setJwtCustomizer(oauth2TokenCustomizer)
            },
            OAuth2AccessTokenGenerator(),
            OAuth2RefreshTokenGenerator(),
        )
    }

    @Bean
    fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return DelegatingOAuth2TokenCustomizer(
            IdTokenCustomizer(),
        )
    }
}