package cbr.authservice.controller;

import cbr.authservice.dto.UserRoleDto;
import cbr.authservice.service.AuthService;
import cbr.entity.Role;
import cbr.entity.User;
import cbr.exception.CustomException;
import cbr.exception.ErrorResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@RestController
@PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")

public class AdminController {
    @Autowired
    private AuthService userService;

    @GetMapping("/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2NTg1MjYwOX0.vRM86NrkT0qJZfA5sP_cdzbrXS8vla2s6afymwXipZk"),
    })
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2NTg1MjYwOX0.vRM86NrkT0qJZfA5sP_cdzbrXS8vla2s6afymwXipZk"),
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(
            @ApiParam(name = "User's id", value = "Deletes user by it's id", required = true)
            @PathVariable int id) throws CustomException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/roles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2NTg1MjYwOX0.vRM86NrkT0qJZfA5sP_cdzbrXS8vla2s6afymwXipZk"),
    })
    @ApiOperation(value = "Update user roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK",response = UserRoleDto.class),
            @ApiResponse(code = 400, message = "Insufficient rights",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    public ResponseEntity<UserRoleDto> updateRole(
            @ApiParam(name = "User with it's roles", value = "Put username and all new roles", required = true)
            @Valid @RequestBody UserRoleDto user) throws CustomException {
        userService.updateRole(user);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}

