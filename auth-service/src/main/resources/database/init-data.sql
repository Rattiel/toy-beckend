INSERT INTO public.oauth2_registered_client (id, client_id, client_id_issued_at, client_secret,
                                             client_secret_expires_at, client_name, client_authentication_methods,
                                             authorization_grant_types, redirect_uris, post_logout_redirect_uris,
                                             scopes, client_settings, token_settings)
VALUES (
        '93f38639-7d1e-425d-afbe-94540205969e',
        'default-client',
        '2025-01-15 19:34:03.832164',
        '$2a$10$Og3nvAx.iQikHLGRRyRZuefQ8umXUI.fsLwxl0SDLgVA.6gL3ZAku',
        null,
        '93f38639-7d1e-425d-afbe-94540205969e',
        'client_secret_basic',
        'refresh_token,client_credentials,authorization_code',
        'http://127.0.0.1:8080/authorized,https://oauth.pstmn.io/v1/callback',
        'http://127.0.0.1:8080/logged-out',
        'user.read,openid,profile,message.read,message.write',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');
