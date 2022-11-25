package com.example.authservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel(description = "User credentials", value = "Fields required for registration")

public class UserCredentialsDto {
    @NotNull(message = "Name mustn't be null")
    @Size(min = 5,message = "Username's length must be greater than 5 chars")
    @ApiModelProperty(value = "username", example = "user1")
    private String username;
    @NotNull(message = "pass mustn't be null")
    @Size(min = 5, message = "Password's length must be greater than 5 chars")
    @ApiModelProperty(value = "password", example = "pass1")
    private String  password;

}

