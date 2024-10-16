package net.kosa.mentopingserver.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.CustomOAuth2User;
import net.kosa.mentopingserver.domain.login.dto.UserDTO;
import net.kosa.mentopingserver.global.common.enums.Role;
import net.kosa.mentopingserver.global.jwt.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*
            재로그인 루프 발생시 사용
        
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
         */

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                System.out.println(cookie.getName());
                if (cookie.getName().equals("Authorization")) {

                    authorization = cookie.getValue();
                }
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰
        String token = authorization;
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        String oauthId = jwtUtil.getOauthId(token);
        Role role = Role.valueOf(jwtUtil.getRole(token));
        UserDTO userDTO = new UserDTO();
        userDTO.setOauthId(oauthId);
        userDTO.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

}
