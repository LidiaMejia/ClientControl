package app.dao;

import app.domain.Client;
import org.springframework.data.repository.CrudRepository;

public interface IClientDao extends CrudRepository<Client, Integer> {
    
}