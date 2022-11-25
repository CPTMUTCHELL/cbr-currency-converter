package com.example.authservice.controller;

import com.example.authservice.service.AuthService;
import com.example.authservice.util.JwtUtil;
import com.example.authservice.dto.Token;
import com.example.authservice.dto.UserCredentialsDto;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class LoginController {

    private final AuthService userService;
    private final JwtUtil jwtUtil;

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
            @Valid @RequestBody UserCredentialsDto user) throws CustomException{
            var saved = userService.save(user);
            return new ResponseEntity<>(saved,HttpStatus.ACCEPTED);


    }

    @ApiOperation(value = "Generate new pair of tokens by refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Token.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Refresh Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @PreAuthorize("hasAuthority('REFRESH')")
    @GetMapping("/token")
    public ResponseEntity<Token> generateNewTokens(@ApiIgnore Authentication authentication) {
        var tokens = jwtUtil.generateJwt(authentication);

        return new ResponseEntity<>(tokens,HttpStatus.OK);
    }

}
