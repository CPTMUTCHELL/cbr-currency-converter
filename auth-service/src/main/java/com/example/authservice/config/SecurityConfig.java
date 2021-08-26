package com.example.authservice.config;


import com.example.authservice.service.AuthService;
import com.example.filter.CustomAuthFilter;
import com.example.filter.CustomAuthorizationFilter;
import com.example.filter.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableSpringConfigured
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthFilter filter = new CustomAuthFilter(authenticationManagerBean(),getProperties());
       filter.setFilterProcessesUrl("/auth/login");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/auth/login","/auth/token","/auth/registration").permitAll()

                .anyRequest().authenticated()
                .and()
                .addFilter(filter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }



    @Override
    public void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(authenticationProvider());
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public Properties getProperties(){
        return new Properties();
    }


}
