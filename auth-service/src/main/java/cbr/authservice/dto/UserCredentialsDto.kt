package cbr.authservice.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel(description = "User credentials", value = "Fields required for login")

class UserCredentialsDto(
    @field:NotNull(message = "Name mustn't be null")
    @field:Size(min = 5, message = "Username's length must be greater than 5 chars")
    @field:ApiModelProperty(value = "username", example = "user1")
    var username: String,
    @field:NotNull(message = "pass mustn't be null")
    @field:Size(min = 5, message = "Password's length must be greater than 5 chars")
    @field:ApiModelProperty(value = "password", example = "pass1")
    var password: String
)
