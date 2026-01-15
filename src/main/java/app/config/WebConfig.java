package app.config;

import app.interceptor.ThemeInterceptor;
import app.interceptor.ThemeResolverInterceptor;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    //Interceptores personalizados de tema porque Spring no maneja uno como el del idioma
    private final ThemeInterceptor themeInterceptor;
    private final ThemeResolverInterceptor themeResolverInterceptor;
    
    public WebConfig(ThemeInterceptor themeInterceptor, ThemeResolverInterceptor themeResolverInterceptor) {
        this.themeInterceptor = themeInterceptor;
        this.themeResolverInterceptor = themeResolverInterceptor;
    }

    //Setear el idioma/region por defecto
    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        
        Locale locale = new Locale.Builder()
                .setLanguage("es")
                .build();
        
        slr.setDefaultLocale(locale);
        
        return slr;
    }
    
    //Interceptamos los cambios de lenguaje con el query parameter lang
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        
        lci.setParamName("lang");
        
        return lci;
    }
    
    //Se agregan los interceptores de idioma y temas creados al registro de interceptores del aplicativo
    //EL ORDEN DE LLAMADA IMPORTA
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(themeInterceptor);
        registry.addInterceptor(themeResolverInterceptor);
    }
    
    //Mapear paths/vistas que no necesariamente pasan por un controlador, no tiene lógica o datos como la raiz, login, paginas estáticas...
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }
    
}