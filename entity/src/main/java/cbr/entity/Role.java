package cbr.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the role",example = "3")
    private int id;
    @ApiModelProperty(notes = "Name of the role",example = "USER",allowableValues = "ADMIN, USER, OWNER, BANNED")

    @Pattern(regexp="^(ADMIN|USER|OWNER|BANNED)$",message="invalid role")
    @NotNull
    private String name;

}
