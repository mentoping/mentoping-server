package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface MentorApplicantRepository extends JpaRepository<MentorApplicant , Long> {

    // Member ID를 기반으로 MentorApplicant 찾기
    Optional<MentorApplicant> findByMember_Id(Long memberId);

}
