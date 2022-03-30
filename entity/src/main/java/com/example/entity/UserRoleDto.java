package com.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class UserRoleDto {

    private String username;
//    this doesn't work
//    @NotNull
    @NotEmpty(message = "Must be at least one role")
    private List<@Valid Role> roles;
}
