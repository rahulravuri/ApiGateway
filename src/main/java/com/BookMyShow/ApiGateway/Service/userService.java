package com.BookMyShow.ApiGateway.Service;

import com.BookMyShow.ApiGateway.Model.User_View;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.BookMyShow.ApiGateway.Repository.UserRepository;
import com.BookMyShow.ApiGateway.Util.JWTUtil;

import java.security.PublicKey;
import java.util.Optional;

@Service
public class userService {
    @Autowired
    JWTUtil JWTUtil;
    @Autowired
    UserRepository UserRepository;


    public Mono<Boolean> validateToken(String token, PublicKey PublicKey) {
        return Mono.fromCallable(() -> JWTUtil.parseJwt(token, PublicKey))
                .flatMap(claims -> {
                    String email = (String) claims.get("email");
                    System.out.println(email);
                     UserRepository.findByEmailid(email).subscribe(
                             value -> System.out.println("Mono Value: " + value)
                     );

                    return UserRepository.findByEmailid(email)
                            .map(user ->
                                    true)
                            .defaultIfEmpty(false);
                })
                .onErrorResume(e -> {
                    // Log error if necessary
                    System.out.println(e);
                    return Mono.just(false);
                });
    }
}
