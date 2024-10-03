package net.kosa.mentopingserver.domain.member;

import java.util.List;

public interface MemberService {
    List<Member> getAllMembers();
    Member getMemberByEmail(String email);
}
