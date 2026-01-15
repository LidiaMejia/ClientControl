package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final RoleProperties roleProperties;   
    private final UserDetailsService userDetailsService;
    
    public SecurityConfig(RoleProperties roleProperties, UserDetailsService userDetailsService) {
        this.roleProperties = roleProperties;
        this.userDetailsService = userDetailsService;
    }
    
    //Establecer la autorizacion para las rutas del aplicativo, configuracion de login/logout y errores
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/resources/**")
                    .permitAll()
                .requestMatchers("/getClientForm/**", "/saveClient/**", "/updateClient/**", "/deleteClient/**")
                    .hasAuthority(roleProperties.getAdmin())
                .requestMatchers("/")
                    .hasAnyAuthority(roleProperties.getAdmin(), roleProperties.getUser())
                .anyRequest()
                    .authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/error/403")
            );
        
        return httpSecurity.build();
    }
    
    //Setear el metodo encoder utilizado en las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //Indicarle a Spring que use la implementacion definida para obtener los roles desde la base de datos
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }    
    
}