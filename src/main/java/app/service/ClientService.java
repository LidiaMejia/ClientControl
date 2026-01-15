package app.service;

import app.domain.Client;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.dao.IClientDao;
import app.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService implements IClientService {
    private static final double LOW_TOTAL_BALANCE = 499.00;
    private static final double MEDIUM_TOTAL_BALANCE = 999.00;

    private final IClientDao clientDao;   
    private final IValidationService validationService;    
    private final IMessageService messageService;
    
    public ClientService(IClientDao clientDao, IValidationService validationService, IMessageService messageService) {
        this.clientDao = clientDao;
        this.validationService = validationService;
        this.messageService = messageService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Client> list() {
        return (List<Client>) clientDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findOneById(Client client) {
        return clientDao.findById(client.getClientId()).orElseThrow(() -> {
            log.warn("Cliente {} no encontrado", client.getClientId());
            return new ResourceNotFoundException();
        });
    }

    @Override
    @Transactional
    public void save(Client client) {
        clientDao.save(client);
    }

    @Override
    @Transactional
    public void delete(Client client) {
        clientDao.delete(client);
    }
    
    @Override
    public String getFormatedBalance(Client client) {
        return validationService.getAmountWithFormat(client.getBalance());
    }
    
    @Override
    public Double getTotalBalance(List<Client> clientList) {
        Double totalBalance = 0D;
        
        if(clientList != null & !clientList.isEmpty()) {
            totalBalance = clientList.stream().mapToDouble(client -> client.getBalance()).sum();
        }
            
        return totalBalance;
    }
    
    @Override
    public String getBalanceStatus(Double totalBalance) {        
        String balanceStatus;
        
        String lowBalanceStatus = messageService.getMessage("client.balance.low");
        String mediumBalanceStatus = messageService.getMessage("client.balance.medium");
        String highBalanceStatus = messageService.getMessage("client.balance.high");
        
        if(totalBalance <= LOW_TOTAL_BALANCE) {
            balanceStatus = lowBalanceStatus;
        }
        else if(totalBalance > LOW_TOTAL_BALANCE && totalBalance <= MEDIUM_TOTAL_BALANCE) {
            balanceStatus = mediumBalanceStatus;
        }
        else {
            balanceStatus = highBalanceStatus;
        }
        
        return balanceStatus;
    }
    
    @Override
    public String getBalanceStatusBgColor(String balanceStatus) {
        String balanceBgColor = "bg-primary";
        
        String lowBalanceStatus = messageService.getMessage("client.balance.low");
        String mediumBalanceStatus = messageService.getMessage("client.balance.medium");
        String highBalanceStatus = messageService.getMessage("client.balance.high");
        
        if(balanceStatus.equals(lowBalanceStatus)) {
            balanceBgColor = "bg-danger";
        }
        else if (balanceStatus.equals(mediumBalanceStatus)) {
            balanceBgColor = "bg-warning";
        }
        else if (balanceStatus.equals(highBalanceStatus)) {
            balanceBgColor = "bg-success";
        }
        
        return balanceBgColor;
    }
    
}