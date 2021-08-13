package io.vpv.homeuse.config;


/******************************************************************************
 * Copyright 2021 reflexdemon                                                 *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS    *
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,*
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    *
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        *
 * DEALINGS IN THE SOFTWARE.                                                  *
 ******************************************************************************/

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
