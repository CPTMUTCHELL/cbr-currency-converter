package cbr.authservice.util;

import cbr.authservice.dto.Token;
import cbr.authservice.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.auth.token.expire}"))
    private  String accessExpire;
    @Value(("${jwt.refresh.token.expire}"))
    private  String refreshExpire;
    @Autowired
    private AuthService userService;
    public Token generateJwt(Authentication authResult){
        var username =  authResult.getName();
        var algorithm = Algorithm.HMAC256(secret.getBytes());
        var accessToken = JWT.create().withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(accessExpire)))
                .withClaim("roles", userService.loadUserByUsername(username).getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        var refreshToken = JWT.create().withSubject(username)
                .withClaim("roles",new ArrayList<>(List.of("xxx-REFRESH")))
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpire)))
                .sign(algorithm);


        return new Token(accessToken,refreshToken);
    }

}
