package com.example.authservice.controller;
import com.example.authservice.service.AuthService;
import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class LoginController {
    @Autowired
    private AuthService userService;
    @PostMapping("/registration")
    public ResponseEntity<User> receiveRegistration(@RequestBody User user) {
        boolean exist = userService.checkUser(user.getUsername());
        System.out.println(user);
        if (exist) {
            return new ResponseEntity<>(user,HttpStatus.CONFLICT);
        }
        else {
            userService.save(user);
            return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
        }

    }
//    @PostMapping("/login")
//    public ResponseEntity<User> receiveLogin(@RequestBody User user) {
//        System.out.println(user);
//        userService.loadUserByUsername(user.getUsername());
//        System.out.println("sdfg");
//        return null;
//    }
}
