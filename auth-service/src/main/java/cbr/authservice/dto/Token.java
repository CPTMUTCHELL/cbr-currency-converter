package cbr.authservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(description = "Access and refresh token object", value = "Tokens")

public class Token {
    @ApiModelProperty(value = "access token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2NTg1MjYwOX0.vRM86NrkT0qJZfA5sP_cdzbrXS8vla2s6afymwXipZk")
    private String accessToken;
    @ApiModelProperty(value = "refresh token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2NTg1MjYwOX0.vRM86NrkT0qJZfA5sP_cdzbrXS8vla2s6afymwXipZk")
    private String refreshToken;
}
