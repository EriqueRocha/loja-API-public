package dev.erique.loja.infra.security;

import dev.erique.loja.infra.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors().and().csrf().disable()
                .addFilterAfter(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                .antMatchers("/public/**").permitAll()

                .antMatchers(HttpMethod.POST,"/manager/saveNew").hasRole("MANAGER")
                .antMatchers(HttpMethod.PUT,"/manager/update/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.DELETE,"/manager/delete/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET,"/manager/getList").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET,"/manager/getOne/{id}").hasRole("MANAGER")

                .antMatchers(HttpMethod.POST,"/cliente/saveNew").permitAll()
                .antMatchers(HttpMethod.PUT,"/cliente/update").hasRole("USER")
                .antMatchers(HttpMethod.PUT,"/updateCliente/manager{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.DELETE,"/cliente/deleteManager/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.DELETE,"/cliente/delete").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/cliente/getList").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET,"/cliente/getOne").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/cliente/getOne/manager/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET,"/cliente/compras").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/cliente/compras/manager/{id}").hasRole("MANAGER")

                .antMatchers(HttpMethod.POST,"/produto/saveNew").hasRole("MANAGER")
                .antMatchers(HttpMethod.PUT,"/produto/update/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.DELETE,"/produto/delete/{id}").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET,"/produto/getList").permitAll()
                .antMatchers(HttpMethod.GET,"/produto/getOne/{id}").permitAll()
                .antMatchers(HttpMethod.PATCH,"/produto/addImage/{id}").hasRole("MANAGER")

                .antMatchers(HttpMethod.POST,"/api/payments").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/api/payments/webhook").permitAll()

                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}