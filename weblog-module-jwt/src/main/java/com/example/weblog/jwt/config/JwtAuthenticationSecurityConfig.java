package com.example.weblog.jwt.config;

import com.example.weblog.jwt.filter.JwtAuthenticationFilter;
import com.example.weblog.jwt.filter.TokenAuthenticationFilter;
import com.example.weblog.jwt.handler.RestAuthenticationEntryPoint;
import com.example.weblog.jwt.handler.RestAuthenticationFailureHandler;
import com.example.weblog.jwt.handler.RestAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // 自定义的用于 JWT 身份验证的过滤器
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));

        // 设置登录认证对应的处理类（成功处理、失败处理）
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);

        // 直接使用 DaoAuthenticationProvider, 它是 Spring Security 提供的默认的身份验证提供者之一
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置 userDetailService，用于获取用户的详细信息
        provider.setUserDetailsService(userDetailsService);
        // 设置加密算法
        provider.setPasswordEncoder(passwordEncoder);
        httpSecurity.authenticationProvider(provider);
        // 将这个过滤器添加到 UsernamePasswordAuthenticationFilter 之前执行
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // 将 Token 认证过滤器添加到 UsernamePasswordAuthenticationFilter 之前执行，保证在鉴权前解析 Token 并写入 SecurityContext
        // 直接调用 @Bean 方法（@Configuration 类被 CGLIB 代理，返回容器中的单例），避免自注入导致的循环依赖
        httpSecurity.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 未认证用户访问受保护资源时，统一由 RestAuthenticationEntryPoint 输出 401 响参
        httpSecurity.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    /**
     * Token 校验过滤器
     * @return
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }
}
