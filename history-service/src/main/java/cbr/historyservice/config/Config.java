package cbr.historyservice.config;


import cbr.filter.CustomAuthorizationFilter;
import cbr.historyservice.service.rabbitmq.RabbitMqListener;
import cbr.rabbitmq.RabbitmqConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//RabbitMq configuration from another module
@Import(value = {RabbitmqConfig.class})
public class Config extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Value(("${jwt.secret}"))
    private  String secret;

    //create RabbitListener on condition
    @ConditionalOnProperty(
            value="rabbitmq.enable",
            havingValue = "true"
    )
    @Bean
    public RabbitMqListener RabbitMqListener() {
        return new RabbitMqListener();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
//                .antMatchers("/history/**").hasAuthority("ADMIN")
                .antMatchers("/health/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                .allowedOrigins("*")
                .allowedMethods("*");
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/v2/api-docs",
                "/**/swagger-resources/**",
                "/**/swagger-ui.html",
                "/**/webjars/**");
    }

}
