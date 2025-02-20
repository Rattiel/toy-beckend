package com.demo.auth.authorization.comparator;

import org.springframework.security.core.GrantedAuthority;

import java.util.Comparator;

public final class GrantedAuthorityComparator implements Comparator<GrantedAuthority> {
    @Override
    public int compare(GrantedAuthority g1, GrantedAuthority g2) {
        if (g2.getAuthority() == null) {
            return -1;
        }
        if (g1.getAuthority() == null) {
            return 1;
        }
        return g1.getAuthority().compareTo(g2.getAuthority());
    }
}