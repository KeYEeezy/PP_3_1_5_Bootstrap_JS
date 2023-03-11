package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.ExceptionHandlers.NoSuchUserException;
import ru.kata.spring.boot_security.demo.ExceptionHandlers.UserNotCreated;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RESTController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public RESTController(UserService userService, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDto>> showAllUsers() {
        List<UserDto> dto = userService.findAll().stream().map(this::userToDto).collect(Collectors.toList());
        return dto != null
                ? new ResponseEntity<>(dto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null)
            throw new NoSuchUserException("User with id= " + id + " not found");

        return new ResponseEntity<>(userToDto(user), HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDto dto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder er = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(fieldError -> er.append(fieldError.getField())
                    .append("-")
                    .append(fieldError.getDefaultMessage())
                    .append(";"));
            throw new UserNotCreated(er.toString());
        }
        User user = dtoToUser(dto);
        user.setCreatedWho(principal.getName());
        userService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDto dto, @PathVariable("id") Long id, Principal principal) {
        User user = dtoToUser(dto);
        user.setUpdatedWho(principal.getName());
        userService.updateUser(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if (userService.getById(id) == null)
            throw new NoSuchUserException("User with id= " + id + " not found");

        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getRoles() {
        return new ResponseEntity<>(new HashSet<>(roleRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/currentuser")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userToDto(userService.findByUsername(principal.getName())), HttpStatus.OK);
    }


    private User dtoToUser(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    private UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }


}
