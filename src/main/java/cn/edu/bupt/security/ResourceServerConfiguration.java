/*
package cn.edu.bupt.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

*/
/**
 * Created by CZX on 2018/5/4.
 *//*

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${account.client_id}")
    private void getClientId(String client_id) {
        Client_id = client_id ;
    }

    @Value("${account.client_secret}")
    private void getClientSecret(String client_secret) {
        Client_secret = client_secret ;
    }

    @Value("${account.check_url}")
    private void getCheck(String checkUrl) {
        System.out.println(checkUrl);
        checkurl = checkUrl ;
    }

    private static String Client_id;
    private static String Client_secret;
    private static String checkurl;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                // Since we want the protected resources to be accessible in the UI as well we need
                // session creation to be allowed (it's disabled by default in 2.0.6)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .requestMatchers().anyRequest()
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/api/tes/device/*").hasAuthority("TENANT_ADMIN");

    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        CustomRemoteTokenServices resourceServerTokenServices = new CustomRemoteTokenServices();
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(new UserTokenConverter());
//        resourceServerTokenServices.setAccessTokenConverter(accessTokenConverter);
//        resources.tokenServices(resourceServerTokenServices);
//    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        RemoteTokenServices resourceServerTokenServices = new RemoteTokenServices();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserTokenConverter());
        resourceServerTokenServices.setAccessTokenConverter(accessTokenConverter);
        resourceServerTokenServices.setClientId(Client_id);
        resourceServerTokenServices.setClientSecret(Client_secret);
        resourceServerTokenServices.setCheckTokenEndpointUrl(checkurl);
        resources.tokenServices(resourceServerTokenServices);
    }
}
*/
