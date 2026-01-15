package app.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.util.UriUtils;

//Se ejecuta antes de rendereizar cualquier vista
@ControllerAdvice
public class GlobalViewAttributes {
    
    public String currentUrl(HttpServletRequest request) {
        //Obtiene la uri actual
        String uri = request.getRequestURI();
        
        //Obtiene los parametros, si existen
        Map<String, String[]> params = request.getParameterMap();
        
        //Se quita el param lang para evitar duplicados en caso de cambio de idioma, y se arma el query
        String newQuery = params.entrySet()
            .stream()
            .filter(param -> !param.getKey().equals("lang"))
            .flatMap(param -> Arrays.stream(param.getValue())
                .map(value -> param.getKey() + "=" + UriUtils.encode(value, StandardCharsets.UTF_8))
            )
            .collect(Collectors.joining("&"));
        
        return newQuery.isEmpty() ? uri : uri + newQuery;
    }
    
}
