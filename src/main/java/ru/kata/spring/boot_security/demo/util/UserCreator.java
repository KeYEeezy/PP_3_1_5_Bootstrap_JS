package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserCreator {
    private RoleRepository roleRepository;

    @Autowired
    public UserCreator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    public List<User> createUsersWithRoles() {
        roleRepository.save(new Role(1L, ROLE_USER));
        roleRepository.save(new Role(2L, ROLE_ADMIN));
        HashSet<Role> roles = new HashSet<>();
        roles.add(new Role(1L, ROLE_USER));
        roles.add(new Role(2L, ROLE_ADMIN));
        User user = new User("user", "user", 26, "user@mail.ru", "user", Set.of(new Role(1L,ROLE_USER)));
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedWho("AutoCreated");
        User admin = new User("admin", "admin", 26, "admin@mail.ru", "admin", roles);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setCreatedWho("AutoCreated");
        return List.of(admin,user);

    }
}

