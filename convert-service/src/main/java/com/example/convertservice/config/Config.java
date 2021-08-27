package com.example.convertservice.config;


import com.example.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class Config extends WebSecurityConfigurerAdapter {
    @Value(("${jwt.secret}"))
    private  String secret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        filter.setFilterProcessesUrl("/login");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
