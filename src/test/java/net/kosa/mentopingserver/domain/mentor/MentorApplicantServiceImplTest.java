package net.kosa.mentopingserver.domain.mentor;

import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.util.S3Service;
import net.kosa.mentopingserver.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class MentorApplicantServiceImplTest {

    @Autowired
    private MentorApplicantService mentorApplicantService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MentorApplicantRepository mentorApplicantRepository;

    @MockBean
    private S3Service s3Service; // S3Service를 모킹합니다.

    private MultipartFile certificationFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Mock S3 file upload result
        when(s3Service.uploadFile(any(MultipartFile.class)))
                .thenReturn("https://s3.amazonaws.com/test-bucket/testfile.pdf");

        certificationFile = new MockMultipartFile("certification_file", "testfile.pdf", "application/pdf", "test data".getBytes());
    }

    @Test
    public void testCreateMentorApplication() throws IOException {
        // Create a test member
        Member member = Member.builder()
                .email("test2@example.com")
                .name("Test User2")
                .nickname("testuser2")
                .password("password")
                .role(Role.ROLE_MENTEE)
                .build();
        memberRepository.save(member);

        // Create request DTO
        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
                .memberId(member.getId())
                .field("ITSW")
                .certification_file(certificationFile)
                .build();

        // Call the service method
        MentorApplicantResponseDto responseDto = mentorApplicantService.createMentorApplication(requestDto);

        // Assert the response
        assertNotNull(responseDto);
        assertEquals("PENDING", responseDto.getStatus());

        // Verify the saved mentor applicant
        Optional<MentorApplicant> savedApplicant = mentorApplicantRepository.findById(Long.valueOf(responseDto.getApplicationId()));
        assertTrue(savedApplicant.isPresent());
        assertEquals(member.getId(), savedApplicant.get().getMember().getId());
    }
}
