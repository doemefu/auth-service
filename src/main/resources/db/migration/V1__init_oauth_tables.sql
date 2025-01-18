-- ===================================================================
--  OAuth2 Tables for Spring Authorization Server in PostgreSQL
--  Adapted for "blob" -> "BYTEA" and with foreign key constraints
-- ===================================================================

CREATE TABLE IF NOT EXISTS oauth2_registered_client (
                                                        id                                VARCHAR(100) NOT NULL,
                                                        client_id                         VARCHAR(100) NOT NULL,
                                                        client_id_issued_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        client_secret                     VARCHAR(200) DEFAULT NULL,
                                                        client_secret_expires_at          TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                        client_name                       VARCHAR(200) NOT NULL,
                                                        client_authentication_methods     VARCHAR(1000) NOT NULL,
                                                        authorization_grant_types         VARCHAR(1000) NOT NULL,
                                                        redirect_uris                     VARCHAR(1000) DEFAULT NULL,
                                                        post_logout_redirect_uris         VARCHAR(1000) DEFAULT NULL,
                                                        scopes                            VARCHAR(1000) NOT NULL,
                                                        client_settings                   VARCHAR(2000) NOT NULL,
                                                        token_settings                    VARCHAR(2000) NOT NULL,
                                                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS oauth2_authorization (
                                                    id                                VARCHAR(100) NOT NULL,
                                                    registered_client_id              VARCHAR(100) NOT NULL,
                                                    principal_name                    VARCHAR(200) NOT NULL,
                                                    authorization_grant_type          VARCHAR(100) NOT NULL,
                                                    authorized_scopes                 VARCHAR(1000) DEFAULT NULL,
                                                    attributes                        BYTEA DEFAULT NULL,
                                                    state                             VARCHAR(500) DEFAULT NULL,
                                                    authorization_code_value          BYTEA DEFAULT NULL,
                                                    authorization_code_issued_at      TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    authorization_code_expires_at     TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    authorization_code_metadata       BYTEA DEFAULT NULL,
                                                    access_token_value                BYTEA DEFAULT NULL,
                                                    access_token_issued_at            TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    access_token_expires_at           TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    access_token_metadata             BYTEA DEFAULT NULL,
                                                    access_token_type                 VARCHAR(100) DEFAULT NULL,
                                                    access_token_scopes               VARCHAR(1000) DEFAULT NULL,
                                                    oidc_id_token_value               BYTEA DEFAULT NULL,
                                                    oidc_id_token_issued_at           TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    oidc_id_token_expires_at          TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    oidc_id_token_metadata            BYTEA DEFAULT NULL,
                                                    refresh_token_value               BYTEA DEFAULT NULL,
                                                    refresh_token_issued_at           TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    refresh_token_expires_at          TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    refresh_token_metadata            BYTEA DEFAULT NULL,
                                                    user_code_value                   BYTEA DEFAULT NULL,
                                                    user_code_issued_at               TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    user_code_expires_at              TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    user_code_metadata                BYTEA DEFAULT NULL,
                                                    device_code_value                 BYTEA DEFAULT NULL,
                                                    device_code_issued_at             TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    device_code_expires_at            TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                                                    device_code_metadata              BYTEA DEFAULT NULL,
                                                    PRIMARY KEY (id),

                                                    CONSTRAINT fk_oauth2_auth_registered_client
                                                        FOREIGN KEY (registered_client_id)
                                                            REFERENCES oauth2_registered_client (id)
);

CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
                                                            registered_client_id              VARCHAR(100) NOT NULL,
                                                            principal_name                    VARCHAR(200) NOT NULL,
                                                            authorities                       VARCHAR(1000) NOT NULL,
                                                            PRIMARY KEY (registered_client_id, principal_name),

                                                            CONSTRAINT fk_oauth2_consent_registered_client
                                                                FOREIGN KEY (registered_client_id)
                                                                    REFERENCES oauth2_registered_client (id)
);
