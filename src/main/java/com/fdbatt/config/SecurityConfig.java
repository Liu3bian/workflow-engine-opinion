package com.fdbatt.config;

import com.fdbatt.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * 密码加密器
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager（登录时用）
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 用户认证来源
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * HTTP 安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // 禁用 CSRF
                .csrf().disable()

                // 不创建 Session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 接口权限控制
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/bpmn/**").authenticated()
                .antMatchers("/process/**").authenticated()
                .antMatchers("/task/**").authenticated()
                .anyRequest().authenticated()
                .and()

                // 添加 JWT 过滤器
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
