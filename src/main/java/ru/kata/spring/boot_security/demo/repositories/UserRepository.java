package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u FROM User u  join fetch u.roles where u.email =:username")
    Optional<User> findByEmail(@Param("username") String username);

    @Query("select distinct u from User u left join fetch u.roles")
    List<User> findAll();

    @Query("select distinct u from User u left join fetch u.roles where u.id =:id")
    Optional<User> findById(@Param("id") Long id);

}
