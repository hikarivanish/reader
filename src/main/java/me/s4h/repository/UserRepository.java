package me.s4h.repository;

import me.s4h.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by LENOVO on 2014/11/21.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
}
