package com.BookMyShow.ApiGateway.Repository;

import com.BookMyShow.ApiGateway.Model.User_View;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserRepository  extends ReactiveCrudRepository<User_View, Integer> {

    Mono<User_View> findByEmailid(String Email);
}
