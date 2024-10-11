package net.kosa.mentopingserver.domain.mentor;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kosa.mentopingserver.domain.member.MemberRepository;
import net.kosa.mentopingserver.domain.member.entity.Member;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.S3Service;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Role;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("aws")  // AWS 프로파일 활성화
@Transactional  // 테스트가 끝난 후 데이터가 자동으로 롤백되도록 설정
public class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MentorApplicantService mentorApplicantService;

    @Autowired
    private MentorApplicantRepository mentorApplicantRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        // 실제 데이터베이스에 멤버 저장
        // 고유한 oauthId를 생성하여 member 데이터 중복을 방지
        Member member = Member.builder()
                .role(Role.ROLE_MENTEE)
                .name("John Doe")
                .password("password123")
                .email(UUID.randomUUID().toString() + "@example.com")  // 고유한 이메일 생성
                .nickname("johnd")
                .oauthId(UUID.randomUUID().toString())  // 고유한 oauthId 생성
                .build();
        testMember = memberRepository.save(member);

        // mentor_applicant에서도 고유한 데이터를 설정
        MentorApplicant mentorApplicant = MentorApplicant.builder()
                .member(testMember)
                .category(Category.ITSW)  // 고유한 category 생성
                .status(Status.PENDING)
                .build();
        mentorApplicantRepository.save(mentorApplicant);
    }

    @Test
    public void testCreateMentorApplication() throws Exception {
        // 파일 데이터를 포함한 MockMultipartFile 생성
        MockMultipartFile certificationFile = new MockMultipartFile(
                "certification_file",
                "testFile.txt",
                "text/plain",
                "Test File Content".getBytes()
        );

        // MentorApplicantRequestDto 생성
        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
                .memberId(testMember.getId())  // 테스트 멤버 ID 사용
                .field("ITSW")
                .certification_file(certificationFile)
                .build();

        // ObjectMapper를 사용해 DTO를 JSON으로 변환
        String requestJson = objectMapper.writeValueAsString(requestDto);

        // 테스트 요청 전송
        mockMvc.perform(multipart("/mentor-applicants/upload")
                        .file(certificationFile)  // 파일을 추가
                        .param("memberId", testMember.getId().toString())
                        .param("field", "ITSW")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // 응답 상태가 200이어야 함
                .andReturn();
    }
}
