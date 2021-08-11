package io.vpv.homeuse.config;

import io.vpv.homeuse.config.security.OAuthLoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.HeaderWriterServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;

import static org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter.Directive.CACHE;
import static org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES;


@Configuration
@EnableWebFluxSecurity
public class OAuth2LoginSecurityConfig {

    private final OAuthLoginHandler oAuthLoginHandler;
    private final OAuthLogoutHandler oAuthLogoutHandler;

    public OAuth2LoginSecurityConfig(OAuthLoginHandler oAuthLoginHandler, OAuthLogoutHandler oAuthLogoutHandler) {
        this.oAuthLoginHandler = oAuthLoginHandler;
        this.oAuthLogoutHandler = oAuthLogoutHandler;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        ServerLogoutHandler securityContext = new SecurityContextServerLogoutHandler();
        ClearSiteDataServerHttpHeadersWriter writer = new ClearSiteDataServerHttpHeadersWriter(CACHE, COOKIES);
        ServerLogoutHandler clearSiteData = new HeaderWriterServerLogoutHandler(writer);

        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(securityContext, clearSiteData, oAuthLogoutHandler);

        return http
                .authorizeExchange()
                .pathMatchers("/login**", "/error**", "/", "/index", "/webjars/**", "/oauth2/authorization/**", "/css/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Login(oauthLogin -> oauthLogin.authenticationSuccessHandler(oAuthLoginHandler)).logout()
                .logoutHandler(logoutHandler)
                .and()
                .build()
                ;
    }
}
