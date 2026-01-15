package app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.role")
public class RoleProperties {
    private String admin;
    private String user;
    
    public boolean isAdmin() {
        return hasAuthority(this.admin);
    }
    
    public boolean isUser() {
        return hasAuthority(this.user);
    }
    
    private Boolean hasAuthority(String role) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null) 
            return false;
        
        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}