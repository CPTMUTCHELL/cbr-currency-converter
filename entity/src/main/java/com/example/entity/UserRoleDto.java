package com.example.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class UserRoleDto {
    @ApiModelProperty(notes = "Username, whose roles you want to change")
    private String username;
//    this doesn't work
//    @NotNull
    @NotEmpty(message = "Must be at least one role")
    @ApiModelProperty(notes = "Array of user's roles")
    private List<@Valid Role> roles;
}
