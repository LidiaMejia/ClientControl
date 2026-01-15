package app.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//Detecta el cambio de tema y guarda la preferencia
@Component
public class ThemeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String theme = request.getParameter("theme");
        
        if(theme != null) {
            Cookie cookie = new Cookie("theme", theme);
            
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); //1 dia
            
            response.addCookie(cookie);
        }
        
        return true;
    }
    
}