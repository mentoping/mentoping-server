package net.kosa.mentopingserver.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String oauthId;
    private String role;
    private String name;
    private String password;
    private String email;
    private String nickname;
    private String profile;
    private String content;
}