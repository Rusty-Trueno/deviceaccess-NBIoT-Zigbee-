/*

package cn.edu.bupt.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityUser implements Serializable {

    private static final long serialVersionUID = -797397440703066079L;

    private Collection<GrantedAuthority> authorities;
    private Collection<String> permissions;
    private Integer Id;
    private String userPrincipal;
    private Integer customerId;
    private Integer tenantId;
    private Authority authority;

    public SecurityUser(Integer id, String userPrincipal, Integer customerId, Integer tenantId, Authority authority, Collection<String> permissions) {
        this.Id = id;
        this.userPrincipal = userPrincipal;
        this.customerId = customerId;
        this.tenantId = tenantId;
        this.authority = authority;
        this.permissions = permissions;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = Stream.of(SecurityUser.this.getAuthority())
                    .map(authority -> new SimpleGrantedAuthority(authority.name()))
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(String userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"authorities\":")
                .append(authorities);
        sb.append(",\"permissions\":")
                .append(permissions);
        sb.append(",\"Id\":")
                .append(Id);
        sb.append(",\"userPrincipal\":\"")
                .append(userPrincipal).append('\"');
        sb.append(",\"customerId\":")
                .append(customerId);
        sb.append(",\"tenantId\":")
                .append(tenantId);
        sb.append(",\"authority\":")
                .append(authority);
        sb.append('}');
        return sb.toString();
    }
}
*/
