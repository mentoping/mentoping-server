package net.kosa.mentopingserver.domain.login.dto;

import lombok.Getter;
import lombok.Setter;
import net.kosa.mentopingserver.global.common.enums.Role;

@Getter
@Setter
public class UserDTO {

    private Role role;
    private String name;
    private String oauthId;
}
