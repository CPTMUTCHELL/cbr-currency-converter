package com.example.authservice.controller;

import com.example.authservice.service.AuthService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/auth/admin")
@RestController
public class AdminController {
    @Value(("${jwt.secret}"))
    private String secret;
    @Value(("${jwt.refresh.token.expire}"))
    private String refreshTokenExpire;
    @Autowired
    private AuthService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        User user = userService.deleteUserById(id);
        if (user != null) return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
        else throw new ResponseStatusException(HttpStatus.CONFLICT, "User not found!");
    }
    @PutMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserRoleDto> updateRole(@Valid @RequestBody UserRoleDto user) {
        userService.updateRole(user);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}

