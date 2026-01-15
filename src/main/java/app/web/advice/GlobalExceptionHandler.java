package app.web.advice;

import app.domain.ErrorGeneric;
import app.exception.ResourceNotFoundException;
import app.service.IMessageService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Intercepta globalmente todas las excepciones, excepto las que maneja Spring Security (401, 403)
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private final IMessageService messageService;
    
    public GlobalExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handle404(Model model) {
        ErrorGeneric error = new ErrorGeneric();
        
        error.setStatus(404);
        error.setTitle(messageService.getMessage("error.404.title"));
        error.setMessage(messageService.getMessage("error.404.message"));
        error.setImage("/images/error404.svg");
        
        model.addAttribute("error", error);
        
        return "error/errorGeneric";
    }
    
    @ExceptionHandler(Exception.class)
    public String handle500(Model model) {
        ErrorGeneric error = new ErrorGeneric();
        
        error.setStatus(500);
        error.setTitle(messageService.getMessage("error.500.title"));
        error.setMessage(messageService.getMessage("error.500.message"));
        error.setImage("/images/error500.svg");
        
        model.addAttribute("error", error);
        
        return "error/errorGeneric";
    }
    
}