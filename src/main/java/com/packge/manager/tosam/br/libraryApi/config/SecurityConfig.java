package com.packge.manager.tosam.br.libraryApi.config;

import com.packge.manager.tosam.br.libraryApi.security.JwtCustomAuthenticationFilter;
import com.packge.manager.tosam.br.libraryApi.security.LoginSocialSuccesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, LoginSocialSuccesHandler succesHandler  , JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {


        return httpSecurity.csrf(AbstractHttpConfigurer::disable)


                .formLogin(form -> form.loginPage("/login").permitAll())
               


                .authorizeHttpRequests(custom -> custom
                        .requestMatchers("/login").permitAll().
                        requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers("/autores/**").permitAll()
                        .requestMatchers("/livros/**").permitAll()


                        .anyRequest().authenticated())
                .oauth2Login(outh2 -> {

                    outh2.loginPage("/login");
                    outh2.successHandler(succesHandler);




                })
                .oauth2ResourceServer(oauthRs -> oauthRs.jwt(Customizer.withDefaults()))

                .addFilterAfter(jwtCustomAuthenticationFilter , BearerTokenAuthenticationFilter.class)

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

}
