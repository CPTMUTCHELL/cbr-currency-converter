//package com.example.converter.service;
//
//import com.example.converter.entity.user.Role;
//import com.example.converter.entity.user.User;
//import com.example.converter.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//@org.springframework.stereotype.Service
//public class Service  implements UserDetailsService {
//    @Autowired
//    private UserRepo userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        User user=userRepository.findByUsername(s);
//        return  new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(),mapRolesToAuth(user.getRoles()));
//    }
//    private List<?extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }
//}
