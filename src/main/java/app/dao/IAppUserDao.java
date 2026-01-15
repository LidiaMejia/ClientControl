package app.dao;

import app.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppUserDao extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
}