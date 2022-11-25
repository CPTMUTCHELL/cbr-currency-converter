package com.example.authservice.service;

import com.example.authservice.dto.UserRoleDto;
import com.example.authservice.mapper.UserRoleDtoMapper;
import com.example.authservice.repository.RoleRepo;
import com.example.authservice.repository.UserRepo;
import com.example.authservice.dto.UserCredentialsDto;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepo userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepo roleRepo;
    private final UserRoleDtoMapper mapper;

    @Lazy
    @Autowired
    public AuthService(UserRepo userRepository, BCryptPasswordEncoder encoder, RoleRepo roleRepo, UserRoleDtoMapper mapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
        this.mapper = mapper;
    }

    public User getUser(String username) throws CustomException {
        var ex = "User not found";
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ex, HttpStatus.NOT_FOUND, new ErrorResponse(ex)));
    }

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = null;
        try {
            user = getUser(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuth(user.getRoles()));
    }


    public User save(UserCredentialsDto user) throws CustomException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            var ex = "User with this username already exists!";
            throw new CustomException(ex, HttpStatus.CONFLICT, new ErrorResponse(ex));
        } else {
            var userToSave = new User();
            userToSave.setUsername(user.getUsername());
            var roles = new ArrayList<Role>();
            roles.add(roleRepo.findById(3).get()); //USER
            userToSave.setRoles(roles);
            userToSave.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(userToSave);
            return userToSave;
        }
    }

    private List<? extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getId() + "-" + role.getName()))
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUserById(int id) throws CustomException {
        var userToDelete = userRepository.findById(id);
        if (userToDelete.isPresent()) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            var minRoleId = getUser(authentication.getName()).getRoles().stream().map(Role::getId).min(Integer::compareTo).get();
            if (minRoleId < userToDelete.get().getRoles().stream().map(Role::getId).min(Integer::compareTo).get()) {
                userRepository.delete(userToDelete.get());
            } else {
                var msg = "insufficient rights to remove " + userToDelete.get().getUsername();
                throw new CustomException(msg, HttpStatus.UNAUTHORIZED, new ErrorResponse(msg));
            }
        }
    }

    public User updateRole(UserRoleDto userDto) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer minRoleId = getUser(authentication.getName()).getRoles().stream().map(Role::getId).min(Integer::compareTo).get();
        User userToChange = getUser(userDto.getUsername());
        if (minRoleId < userToChange.getRoles().stream().map(Role::getId).min(Integer::compareTo).get()) {
            mapper.updateRoles(userDto, userToChange);
            return userRepository.save(userToChange);
        } else {
            var msg = "insufficient rights to change " + userDto.getUsername();
            throw new CustomException(msg, HttpStatus.UNAUTHORIZED, new ErrorResponse(msg));
        }
    }
}
