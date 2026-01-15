package app.service;

import app.domain.AppUser;
import app.domain.UserRole;
import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import app.dao.IAppUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class AppUserService implements IAppUserService, UserDetailsService {
    
    private final IAppUserDao userDao;
    
    public AppUserService(IAppUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userDao.findByUsername(username);
        
        if(appUser == null) {
            throw new UsernameNotFoundException(username);
        }
        
        //Guardar roles del usuario en el formato reconocible por Spring Security
        var grantedAuthorities = new ArrayList<GrantedAuthority>();
        
        for(UserRole userRole : appUser.getUserRoleList()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
        }
        
        return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
    }
    
}