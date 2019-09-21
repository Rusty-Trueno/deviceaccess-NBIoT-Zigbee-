/*
package cn.edu.bupt.security;

import cn.edu.bupt.security.model.Authority;
import cn.edu.bupt.security.model.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by CZX on 2018/5/5.
 *//*

public class UserTokenConverter extends DefaultUserAuthenticationConverter{

    final String USER_ID = "user_id";
    final String USERNAME = "user_name";
    final String CUSTOMER_ID = "customer_id";
    final String TENANT_ID = "tenant_id";
    final String AUTHORITY = "authority";
    final String PERMISSION = "permissions";

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            //TODO
            List<String> permissions =(List<String>) map.get(PERMISSION);
            SecurityUser securityUser = new SecurityUser((Integer)map.get(USER_ID), (String) map.get(USERNAME),(Integer)map.get(CUSTOMER_ID),(Integer)map.get(TENANT_ID), Authority.parse((String) map.get(AUTHORITY)),permissions);
            Object principal = securityUser;
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return null;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
*/
