package com.packge.manager.tosam.br.libraryApi.config;

import com.packge.manager.tosam.br.libraryApi.security.JwtCustomAuthenticationFilter;
import com.packge.manager.tosam.br.libraryApi.security.LoginSocialSuccesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, LoginSocialSuccesHandler succesHandler  , JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {


        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())


                .formLogin(form -> form.loginPage("/login").permitAll())
               


                .authorizeHttpRequests(custom -> custom
                        .requestMatchers("/login").permitAll().
                        requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers("/autores/**").permitAll()
                        .requestMatchers("/livros/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()


                        .anyRequest().authenticated())
                .oauth2Login(outh2 -> {

                    outh2.loginPage("/login");
                    outh2.successHandler(succesHandler);




                })
                //.oauth2ResourceServer(oauthRs -> oauthRs.jwt(Customizer.withDefaults()))

                .addFilterBefore(jwtCustomAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
   public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/v2/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**"
        );
    }

 

    @Bean
    GrantedAuthorityDefaults grantedAuthority() {

        return new GrantedAuthorityDefaults("");
    }

     

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverterconvert = new JwtGrantedAuthoritiesConverter();

        authoritiesConverterconvert.setAuthorityPrefix("");


var convert = new JwtAuthenticationConverter();

        convert.setJwtGrantedAuthoritiesConverter(authoritiesConverterconvert);

        return convert;




    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Suas configurações de origens (IntelliJ, VSCode, etc)
        configuration.setAllowedOrigins(List.of("http://localhost:63342", "http://127.0.0.1:63342", "http://localhost:5500"));

        // Métodos permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers que o FRONT envia para o BACK
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 👇 ADICIONE ESTA LINHA OBRIGATORIAMENTE 👇
        // Headers que o BACK deixa o FRONT ler na resposta
        configuration.setExposedHeaders(List.of("Authorization"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .refreshTokenTimeToLive(Duration.ofMinutes(120))
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

}
