package com.example.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authservice.repository.RoleRepo;
import com.example.authservice.repository.UserRepo;
import com.example.entity.Role;
import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepo userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepo roleRepo;
    @Lazy
    @Autowired
    public AuthService(UserRepo userRepository, BCryptPasswordEncoder encoder, RoleRepo roleRepo) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }
    public User getUser(String username){
        return userRepository.findByUsername(username );
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user=userRepository.findByUsername(s);

        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuth(user.getRoles()));
        return user1;
    }

    public boolean checkUser(String username) {
        return  (userRepository.existsUserByUsername(username)) ;
    }
    public User save(User user){
        List<Role> roles=new ArrayList<>();
        roles.add(roleRepo.findById(1).get());
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
    private List<?extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
