package com.BookMyShow.ApiGateway.config;
import com.BookMyShow.ApiGateway.Service.userService;

import com.BookMyShow.ApiGateway.Filters.AuthFilter;
import com.BookMyShow.ApiGateway.Util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.PublicKey;

@Configuration
public class GatewayConfig {
	@Autowired
	com.BookMyShow.ApiGateway.Util.JWTUtil JWTUtil;
	@Autowired
	userService userService;
	
	@Value(("${auth.url}"))
	String AuthUrl;
	
	@Value(("${bookmyshow.url}"))
	String bmsurl;
	
	@Value(("${auth.service.name}"))
	String Authname;
	
	@Value(("${bookmyshow.service.name}"))
	String bmsname;

	@Bean
	public PublicKey publicKey() throws Exception {
		return  JWTUtil.getPublicKey("publicKey.pem");
	}

	@Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder,PublicKey PublicKey) {

    	
        return builder.routes()
            .route(Authname, r -> r.path("/auth/**")
                .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                .uri(AuthUrl))
            .route("bmsbooking", r -> r.path("/bookmyshow/Booking/**")
                .filters(f -> f.rewritePath("/bookmyshow/Booking/(?<segment>.*)", "/Booking/${segment}")
						.filter((GatewayFilter) new AuthFilter(JWTUtil,PublicKey,userService)))
                .uri(bmsurl))
				.route("bmsadmin", r -> r.path("/bookmyshow/Admin/**")
						.filters(f -> f.rewritePath("/bookmyshow/Admin/(?<segment>.*)", "/Admin/${segment}")
								.filter((GatewayFilter) new AuthFilter(JWTUtil,PublicKey,userService)))
						.uri(bmsurl))
				.route("bmsview", r -> r.path("/bookmyshow/view/**")
						.filters(f -> f.rewritePath("/bookmyshow/view/(?<segment>.*)", "/view/${segment}"))
						.uri(bmsurl))
            .build();
    }


}