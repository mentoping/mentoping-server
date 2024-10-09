package net.kosa.mentopingserver.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    // 오재혁이 채팅 테스트 하기위해 임의로 시큐리티 요청 모두 허용시킴
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (필요한 경우에만)
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll() // 모든 요청에 대해 인증 없이 접근 허용
                );

        return http.build();
    }
}
