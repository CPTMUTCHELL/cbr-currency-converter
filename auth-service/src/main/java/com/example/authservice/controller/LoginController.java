package com.example.authservice.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authservice.service.AuthService;
import com.example.entity.Role;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/auth")
@RestController
public class LoginController {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.refresh.token.expire}"))
    private  String refreshTokenExpire;
    @Autowired
    private AuthService userService;
    @PostMapping("/registration")
    public ResponseEntity<User> receiveRegistration(@Valid @RequestBody User user) {
        boolean exist = userService.checkUser(user.getUsername());
        System.out.println(user);
        if (exist) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User with this username already exists!");

        }
        else {
            userService.save(user);
            return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("/token")
    public void setRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String accessToken = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpire)))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", token);

                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }
            catch (Exception e) {

                e.printStackTrace();
                response.setHeader("error",e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
    }
    @GetMapping("/validate")
    public boolean validate() {

         return true;
    }


}
@Data
class RoleToUserForm{
    private String username;
    private String roleName;

}
