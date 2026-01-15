package app.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//Obtiene la preferencia de tema (O el valor por defecto) y la expone a la vista
@Component
public class ThemeResolverInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String theme = request.getParameter("theme");
        
        if(theme == null && request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("theme")) {
                    theme = cookie.getValue();
                    break;
                }
            }
        }
        
        if(theme == null) {
            theme = "light";
        }
        
        request.setAttribute("theme", theme);
        
        return true;
    }
    
}