package com.example.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
public class Properties {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.authTokenExpire}"))
    private  String authTokenExpire;
    @Value(("${jwt.refreshTokenExpire}"))
    private  String refreshTokenExpire;
}
