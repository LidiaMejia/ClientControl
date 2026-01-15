package app.web;

import app.config.RoleProperties;
import app.domain.Client;
import app.dto.ClientBriefDto;
import app.dto.ClientListDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import app.service.IClientService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.validation.BindingResult;

@Controller
@Slf4j
public class InitController {
    
    private final RoleProperties roleProperties;
    private final IClientService clientService;
    
    public InitController(RoleProperties roleProperties, IClientService clientService) {
        this.roleProperties = roleProperties;
        this.clientService = clientService;
    }
    
    @GetMapping("/")
    public String init(Model model, @AuthenticationPrincipal User user) {
        log.info("Inicio de sesion del usuario " + user);
        
        //Obtener los clientes de la base de datos
        var list = clientService.list();

        //Crear el objeto con los datos que se enviara a la vista
        List<ClientListDto> clientList = list.stream()
                .map(client -> new ClientListDto(
                    client.getClientId(),
                    client.getFullName(),
                    client.getPhone(),
                    client.getEmail(),
                    clientService.getFormatedBalance(client)
                )).toList();
                
        model.addAttribute("clientList", clientList);
        
        //Crear el objeto de datos resumen de los clientes
        Double totalBalance = clientService.getTotalBalance(list);
        Integer totalClients = list.size();        
        String balanceStatus = clientService.getBalanceStatus(totalBalance);
        String balanceStatusBgColor = clientService.getBalanceStatusBgColor(balanceStatus);
        
        ClientBriefDto clientBrief = new ClientBriefDto(
            totalBalance,
            totalClients,
            balanceStatus,
            balanceStatusBgColor
        );
        
        model.addAttribute("clientBrief", clientBrief);
        
        //Agregar los roles                
        model.addAttribute("roles", roleProperties);
        
        return "index";
    }
    
    //Se carga un objeto de tipo client vacio o con los datos recibidos
    @GetMapping("/getClientForm")
    public String getClientForm(Client client) {
        //Aqui se pueden agregar mensajes para el modal segun el idioma
        return "clientForm :: clientForm";
    }
    
    //En este caso se trabaja con Modal + Javascript + AJAX por lo que se agregan Headers para manejar la respuesta
    @PostMapping("/saveClient")
    public String saveClient(@Valid Client client, BindingResult result, HttpServletResponse response) {
        if(result.hasErrors()) {
            response.setHeader("X-Has-Errors", "true");
            
            return "clientForm :: clientForm";
        }
        
        clientService.save(client);
        
        response.setHeader("X-Has-Errors", "false");
        
        return "redirect:/";
    }
    
    //Path Variable
    //Se agrega el clientId recibido al objeto client mapeado
    @GetMapping("/updateClient/{clientId}")
    public String updateClient(Client client, Model model) {
        client = clientService.findOneById(client);
        
        //Se comparte el objeto client encontrado para que se cargue en el formulario
        model.addAttribute("client", client);
        
        return "clientForm :: clientForm";
    }
    
    //Parameter Query
    //Se crea el objeto client con el Id recibido como parametro
    @GetMapping("/deleteClient")
    public String deleteClient(Client client) {
        clientService.delete(client);
        
        return "redirect:/";
    }
    
}