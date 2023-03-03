package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.ExceptionHandlers.NoSuchUserException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RESTController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    @Autowired
    public RESTController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.findAll();
         return users != null
                ? new ResponseEntity<>(userService.findAll(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        System.out.println(userService.getById(id));
        User user = userService.getById(id);
        if (user == null)
            throw new NoSuchUserException("User with id= " + id + " not found");

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        user.setPassword(user.getPassword());
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        userService.updateUser(id,user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        if (userService.getById(id) == null)
            throw new NoSuchUserException("User with id= " + id + " not found");

        userService.deleteUser(id);
        return "User with id= " + id + " was deleted";
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getRoles() {
        return new ResponseEntity<>(new HashSet<>(roleRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/currentuser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.findByUsername(principal.getName()), HttpStatus.OK);
    }
}
