package io.vpv.homeuse.config;

import io.vpv.homeuse.config.security.OAuthLoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class OAuth2LoginSecurityConfig {

    private final OAuthLoginHandler oAuthLoginHandler;

    public OAuth2LoginSecurityConfig(OAuthLoginHandler oAuthLoginHandler) {
        this.oAuthLoginHandler = oAuthLoginHandler;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/login**", "/error**", "/", "/index", "/webjars/**", "/oauth2/authorization/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Login(oauthLogin -> {
                    oauthLogin.authenticationSuccessHandler(oAuthLoginHandler);

                })
                .oauth2Client(oauthClient -> {

                })
                .build()


                ;
    }

//    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
//        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
//                new OAuth2AccessTokenResponseHttpMessageConverter();
//        tokenResponseHttpMessageConverter.setTokenResponseConverter(new OAuth2AccessTokenResponseConverterWithDefaults());
//
//        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
//        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//
//        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
//        tokenResponseClient.setRestOperations(restTemplate);
//        tokenResponseClient.
//
//        return tokenResponseClient;
//    }

}
