package org.cayman.model.admin;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
