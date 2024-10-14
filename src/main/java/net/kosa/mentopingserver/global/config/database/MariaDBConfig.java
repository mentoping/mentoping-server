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
}
