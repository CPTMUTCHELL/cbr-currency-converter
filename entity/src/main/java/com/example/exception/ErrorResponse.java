package com.example.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Сообщение об ошибке")
public class ErrorResponse implements Serializable {
    @ApiModelProperty(value = "Текст ошибки", example = "Exception occurred", required = true)
    private String message;
}
