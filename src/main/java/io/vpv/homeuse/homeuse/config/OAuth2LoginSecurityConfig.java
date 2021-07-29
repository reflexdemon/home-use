package io.vpv.homeuse.homeuse.config;

import io.vpv.homeuse.homeuse.config.security.OAuth2AccessTokenResponseConverterWithDefaults;
import io.vpv.homeuse.homeuse.config.security.OAuthLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuthLoginHandler oAuthLoginHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login**", "/error**")
                .permitAll()
                .antMatchers("/", "/index", "/webjars/**")
            .permitAll()
            .antMatchers("/oauth2/authorization/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .tokenEndpoint()
            .accessTokenResponseClient(authorizationCodeTokenResponseClient())
            .and()
            .successHandler(oAuthLoginHandler);
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new OAuth2AccessTokenResponseConverterWithDefaults());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRestOperations(restTemplate);

        return tokenResponseClient;
    }

}
