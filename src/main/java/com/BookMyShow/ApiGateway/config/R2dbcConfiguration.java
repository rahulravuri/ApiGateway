package com.BookMyShow.ApiGateway.config;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.time.ZoneId;

@Configuration
public class R2dbcConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(
                MySqlConnectionConfiguration.builder()
                        .host("host.docker.internal") // Replace with your DB host, usually localhost
                        .port(3306)        // Default MySQL port
                        .username("root")  // Your DB username
                        .password("Himalayan@2024R") // Your DB password
                        .database("users") // Your DB name
                        .serverZoneId(ZoneId.of("Asia/Kolkata")) // Set your timezone
                        .connectTimeout(Duration.ofSeconds(3)) // Connection timeout
                        .build()
        );
    }
}
