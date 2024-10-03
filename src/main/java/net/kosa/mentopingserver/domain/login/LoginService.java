package net.kosa.mentopingserver.domain.login;
import net.kosa.mentopingserver.domain.member.Member;

public interface LoginService {
    Member login(String email, String password);

}
