package com.example.authservice.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authservice.service.AuthService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.filter.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class LoginController {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.auth.token.expire}"))
    private  String accessTokenExpire;
    @Autowired
    private AuthService userService;

    //dummy controller ( for swagger ), CustomAuthFilter does the job

    @PostMapping("/login")
    public void login(
            @ApiParam(name = "Sign in object", value = "Fields required for the user login", required = true)
            @Valid @RequestBody UserCredentials user) {

    }
    @PostMapping("/registration")
    public ResponseEntity<User> receiveRegistration(
            @ApiParam(name = "Registration object", value = "Fields required for the user registration", required = true)
            @Valid @RequestBody UserCredentials user) {
        boolean exist = userService.checkUser(user.getUsername());
        System.out.println(user);
        if (exist) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User with this username already exists!");

        }
        else {
            User saved = userService.save(user);
            return new ResponseEntity<>(saved,HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("/token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Refresh Token", required = true, paramType = "header", example = "Bearer refresh_token"),
    })
    public Map<String,String > setRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Map<String, String> tokens = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                UserDetails user = userService.loadUserByUsername(username);
                String accessToken = JWT.create().withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpire)))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);

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
        return tokens;

    }

}
@Data
class RoleToUserForm{
    private String username;
    private String roleName;

}
