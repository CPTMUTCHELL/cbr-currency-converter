package com.example.filter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@Getter
public class Properties {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.auth.token.expire}"))
    private  String authTokenExpire;
    @Value(("${jwt.refresh.token.expire}"))
    private  String refreshTokenExpire;
}
