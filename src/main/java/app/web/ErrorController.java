package app.web;

import app.domain.ErrorGeneric;
import app.service.IMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//Controlador de errores 403 y 401 que maneja Spring Security (Este no pasa por un advice)
@Controller
public class ErrorController {
    
    private final IMessageService messageService;
    
    public ErrorController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("error/403")
    public String error403(Model model) {
        ErrorGeneric error = new ErrorGeneric();
        
        error.setStatus(403);
        error.setTitle(messageService.getMessage("error.403.title"));        
        error.setMessage(messageService.getMessage("error.403.message"));
        error.setImage("images/error403.svg");
        
        model.addAttribute("error", error);
        
        return "error/errorGeneric";
    }
    
}