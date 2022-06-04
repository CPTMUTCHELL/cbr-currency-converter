package com.example.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @NotEmpty(message = "May not be empty")
    @Size(min = 5,message = "Username's length must be greater than 5 chars")
    @ApiModelProperty(notes = "Name of the role",example = "USER",allowableValues = "ADMIN, USER, OWNER, BANNED")

    private String username;
    @Size(min = 5, message = "Password's length must be greater than 5 chars")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinTable(name ="user_role",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    @ApiModelProperty(notes = "User's roles")
    private List<Role> roles;

}
