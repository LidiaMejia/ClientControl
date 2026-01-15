package app.service;

import app.domain.Client;
import java.util.List;

public interface IClientService {
    
    public List<Client> list();
    
    public Client findOneById(Client client);
    
    public void save(Client client);
    
    public void delete(Client client);
    
    public String getFormatedBalance(Client client);
    
    public Double getTotalBalance(List<Client> client);
    
    public String getBalanceStatus(Double totalBalance);
    
    public String getBalanceStatusBgColor(String balanceStatus);
    
}