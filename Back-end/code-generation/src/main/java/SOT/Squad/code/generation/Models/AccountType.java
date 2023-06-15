package SOT.Squad.code.generation.Models;

import org.springframework.security.core.GrantedAuthority;

public enum AccountType implements GrantedAuthority {
    CURRENT,
    SAVINGS;
    @Override
    public String getAuthority() {
        return null;
    }
}

