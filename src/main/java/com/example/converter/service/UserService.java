package com.example.converter.service;

import com.example.converter.entity.user.Role;
import com.example.converter.entity.user.User;
import com.example.converter.repository.RoleRepo;
import com.example.converter.repository.UserRepo;
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
public class UserService implements UserDetailsService  {

    private final UserRepo userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepo roleRepo;
    // Lazy init to avoid cycling. Docker container doesn't have cglib to cope with cycling.
    //Not empty constructor might have complex logic, so Spring has to initialize the beans
    @Lazy
    @Autowired
    public UserService(UserRepo userRepository, BCryptPasswordEncoder encoder, RoleRepo roleRepo) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(s);
        return  new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),mapRolesToAuth(user.getRoles()));
    }

    public boolean checkUser(String username) {
        return  (userRepository.existsUserByUsername(username)) ;
    }
    public User save(User user){
        List<Role> roles=new ArrayList<>();
        roles.add(roleRepo.getOne(1));
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
