package net.kosa.mentopingserver.domain.login;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRole().name()));
    }

    public Long getId() {
        return userDTO.getId();
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getOauthId() {
        return userDTO.getOauthId();
    }

    public String getEmail() {
        return userDTO.getName();
    }
}
