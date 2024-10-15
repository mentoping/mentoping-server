package net.kosa.mentopingserver.global.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MariaDBConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "spring.jpa")
//    public DataSource getDataSource() {
//
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }

    @Bean
    public DataSource getDataSource() {

        return DataSourceBuilder.create()
                .driverClassName("org.mariadb.jdbc.Driver")
                .url("jdbc:mariadb://localhost:3306/mento?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8mb4\n")
                .username("root")
                .password("root")
                .build();
    }
}
