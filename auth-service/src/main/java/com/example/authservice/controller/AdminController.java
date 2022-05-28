package com.example.authservice.controller;

import com.example.authservice.service.AuthService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserRoleDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
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
@PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")

public class AdminController {
    @Autowired
    private AuthService userService;

    @GetMapping("/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(
            @ApiParam(name = "User's id", value = "Deletes user by it's id", required = true)
            @PathVariable int id) {
        User user = userService.deleteUserById(id);
        if (user != null) return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
        else throw new ResponseStatusException(HttpStatus.CONFLICT, "User not found!");
    }
    @PutMapping("/roles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<UserRoleDto> updateRole(
            @ApiParam(name = "User with it's roles", value = "Put username and all new roles", required = true)
            @Valid @RequestBody UserRoleDto user) {
        userService.updateRole(user);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}

