package com.packge.manager.tosam.br.libraryApi.config;

import com.packge.manager.tosam.br.libraryApi.security.CustomAuthentication;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());


        httpSecurity.oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()));


        httpSecurity.formLogin( form -> form.loginPage("/login"));




        return httpSecurity.build();
    }

    @Bean
    TokenSettings tokenSettings(){

        return TokenSettings
                .builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                //Utilizado nas requisições : Utilizado para a primeura autenticação
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                // Utilizado para estender a autenticação (quando já estiver logado)
                //A duração do reflesh deve ser maior do que a duração do que o acsessToken
                .refreshTokenTimeToLive(Duration.ofMinutes(120))
                .build();

    }

    @Bean
    ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings(){

        return AuthorizationServerSettings.builder()

                //Obter token
                .tokenEndpoint("/oauth2/token")
                //para consultar status token
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                //rovogar o token
                .tokenRevocationEndpoint("/oauth2/revoke")
                //authorization endpoint
                .authorizationEndpoint("/oauth2/authorize")
                //Informaçõe sod Usuario
                .oidcUserInfoEndpoint("/oauth2/userinfo")
                //obeter a chave publica e veriaficar a asinatura do token.
                .jwkSetEndpoint("/oauth2/jwks")
                //logout
                .oidcLogoutEndpoint("/oauth2/logout")

                .build();

    }


    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtEncodingContextOAuth2TokenCustomizer(){

        return context -> {
                    var principal  = context.getPrincipal();

                    if (principal instanceof CustomAuthentication customAuthentication){

                        OAuth2TokenType  oAuth2TokenType  = context.getTokenType();

                        if (OAuth2TokenType.ACCESS_TOKEN.equals(oAuth2TokenType)){

                            Collection<? extends GrantedAuthority> authorities = customAuthentication.getAuthorities();

                            List<String> stringStream = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

                            context.getClaims().claim("authorities" , stringStream).claim("email" , customAuthentication.getUsuario().getEmail());

                        }
                    }

        };


    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
