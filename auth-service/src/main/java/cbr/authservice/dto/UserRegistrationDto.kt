package cbr.authservice.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
@ApiModel(description = "User registration data", value = "Fields required for registration")

data class UserRegistrationDto(
    @field:NotNull(message = "Name mustn't be null")
    @field:Size(min = 5, message = "Username's length must be greater than 5 chars")
    @field:ApiModelProperty(value = "username", example = "user1")
     val username: String,
    @field:NotNull(message = "pass mustn't be null")
    @field:Size(min = 5, message = "Password's length must be greater than 5 chars")
    @field:ApiModelProperty(value = "password", example = "pass1")
     val password: String,
    @field:NotNull(message = "email mustn't be null")
    @field:ApiModelProperty(value = "email", example = "ololo@gmail.com")
    @field:Email
     val email: String
)

