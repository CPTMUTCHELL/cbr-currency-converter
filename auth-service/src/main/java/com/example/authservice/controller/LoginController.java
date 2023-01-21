package com.example.authservice.controller;

import com.example.authservice.dto.Token;
import com.example.authservice.dto.UserCredentialsDto;
import com.example.authservice.dto.UserRegistrationDto;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.VerificationTokenService;
import com.example.authservice.util.JwtUtil;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class LoginController {

    private final AuthService userService;
    private final JwtUtil jwtUtil;
    private final VerificationTokenService verificationTokenService;

    //dummy controller ( for swagger ), CustomAuthFilter does the job
    @ApiOperation(value = "Login in to get the pair of tokens")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Token.class),
            @ApiResponse(code = 500, message = "Internal error")
    })
    @PostMapping("/login")
    public ResponseEntity<Token> login(
            @ApiParam(name = "Sign in object", value = "Fields required for the user login", required = true)
            @Valid @RequestBody UserCredentialsDto user,
            @ApiIgnore
            Authentication authentication) {
        return new ResponseEntity<>(jwtUtil.generateJwt(authentication),HttpStatus.OK);
    }

    @ApiOperation(value = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted",response = User.class),
            @ApiResponse(code = 409, message = "User with this username already exists",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PostMapping("/registration")
    public ResponseEntity<User> receiveRegistration(
            @ApiParam(name = "Registration object", value = "Fields required for the user registration", required = true)
            @Valid @RequestBody UserRegistrationDto user) throws CustomException{
            var saved = userService.save(user);
            return new ResponseEntity<>(saved,HttpStatus.ACCEPTED);


    }

    @ApiOperation(value = "Verify registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Verification token is not found or expired",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )

    @GetMapping("/verification")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
         verificationTokenService.verifyToken(token);
        return new ResponseEntity<>("Email has been verified",HttpStatus.OK);

    }


}
