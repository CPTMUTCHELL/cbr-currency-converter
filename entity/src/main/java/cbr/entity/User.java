package cbr.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private int id;
    @ApiModelProperty(notes = "Name of the role",example = "USER",allowableValues = "ADMIN, USER, OWNER, BANNED")
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinTable(name ="user_role",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    @ApiModelProperty(notes = "User's roles")
    private List<Role> roles;
    @ApiModelProperty(notes = "User's email", example = "ololosnhka@gmail.com")
    private String email;
    @ApiModelProperty(notes = "Is email verified")
    private boolean verified;

}
