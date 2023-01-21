package cbr.authservice.dto;

import cbr.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "Username with roles")
public class UserRoleDto {
    @ApiModelProperty(notes = "Username, whose roles you want to change")
    private String username;
    @NotEmpty(message = "Must be at least one role")
    @ApiModelProperty(notes = "Array of user's roles")
    private List<@Valid Role> roles;
}
