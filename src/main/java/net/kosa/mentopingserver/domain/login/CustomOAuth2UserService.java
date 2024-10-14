package net.kosa.mentopingserver.domain.login;

import lombok.RequiredArgsConstructor;
import net.kosa.mentopingserver.domain.login.dto.KakaoResponse;
import net.kosa.mentopingserver.domain.login.dto.OAuth2Response;
import net.kosa.mentopingserver.domain.login.dto.UserDTO;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        switch (registrationId) {
            case "kakao":
                oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
        }

//        String oauthId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String oauthId = oAuth2Response.getProviderId();
        Optional<Member> existMember = memberRepository.findByOauthId(oauthId);

        System.out.println(oauthId);

        if (existMember.isEmpty()) {
            Member member = Member.builder()
                    .oauthId(oauthId)
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getName())
                    .role(Role.ROLE_MENTEE)
                    .profile(oAuth2Response.getProfileImage())
                    .build();

            memberRepository.save(member);

            UserDTO userDTO = new UserDTO();
            userDTO.setOauthId(member.getOauthId());
            userDTO.setName(member.getName());
            userDTO.setRole(member.getRole());

            return new CustomOAuth2User(userDTO);
        }
        else {
            Member updatedMember = Member.builder()
                    .id(existMember.get().getId())
                    .oauthId(existMember.get().getOauthId())
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getName())
                    .role(existMember.get().getRole())
                    .profile(oAuth2Response.getProfileImage())
                    .build();

            memberRepository.save(updatedMember);

            UserDTO userDTO = new UserDTO();
            userDTO.setOauthId(updatedMember.getOauthId());
            userDTO.setName(updatedMember.getName());
            userDTO.setRole(updatedMember.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }

}
