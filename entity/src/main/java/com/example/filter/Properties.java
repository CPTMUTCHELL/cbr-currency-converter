package com.example.filter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@Getter
public class Properties {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.authTokenExpire}"))
    private  String authTokenExpire;
    @Value(("${jwt.refreshTokenExpire}"))
    private  String refreshTokenExpire;
}
