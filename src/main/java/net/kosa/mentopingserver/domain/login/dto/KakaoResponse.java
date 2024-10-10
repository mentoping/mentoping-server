package net.kosa.mentopingserver.domain.login.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            return kakaoAccount.get("email").toString();
        }
        return null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null && properties.containsKey("nickname")) {
            return properties.get("nickname").toString();
        }
        return null;
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.containsKey("profile")) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile.containsKey("profile_image_url")) {
                return profile.get("profile_image_url").toString();
            }
        }
        return null;
    }
}
