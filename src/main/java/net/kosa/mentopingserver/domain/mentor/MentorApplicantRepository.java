package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorApplicantRepository extends JpaRepository<MentorApplicant , Long> {
}
