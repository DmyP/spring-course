package beans.models;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {

    BOOKING_MANAGER,
    REGISTERED_USER,
    ANONYMOUS;

    @Override
    public String getAuthority() {
        return toString();
    }
}
