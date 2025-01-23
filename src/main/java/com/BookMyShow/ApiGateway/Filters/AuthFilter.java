package com.BookMyShow.ApiGateway.Filters;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

import com.BookMyShow.ApiGateway.Exceptions.InvalidJwtException;
import com.BookMyShow.ApiGateway.Exceptions.UserLoginException;
import com.BookMyShow.ApiGateway.Util.JWTUtil;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.BookMyShow.ApiGateway.Service.userService;


public class AuthFilter implements GatewayFilter {


    JWTUtil KeyUtil;
    userService userService;
    PublicKey PublicKey;

    public AuthFilter(JWTUtil JWTUtil,PublicKey PublicKey,userService userService){
        this.KeyUtil=JWTUtil;this.PublicKey=PublicKey;
        this.userService=userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("came here for checking");
        String token = exchange.getRequest().getCookies().getFirst("Bearer") != null
                ? exchange.getRequest().getCookies().getFirst("Bearer").getValue()
                : null;

        return Mono.fromCallable(() -> KeyUtil.parseJwt(token, PublicKey))
                .flatMap(claims -> {
                    String user = claims.get("user", String.class);
                    String roles = claims.get("role", String.class);
                    exchange.getRequest().mutate()
                            .header("X-User", user)
                            .header("X-User-Roles", roles)
                            .build();
                    return chain.filter(exchange);})
                .onErrorResume(e -> {
                    // Handle invalid token error
                    return Mono.error(new Exception("Invalid token: " + e.getMessage()));
                });

    }
}

