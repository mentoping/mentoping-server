package net.kosa.mentopingserver.global.config.database;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MariaDBConfig {

    @Bean
    public DataSource getDataSource() {

        return DataSourceBuilder.create()
                .driverClassName("org.mariadb.jdbc.Driver")
                .url("jdbc:mariadb://localhost:3306/mento?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC\n")
                .username("root")
                .password("root")
                .build();
    }
}
