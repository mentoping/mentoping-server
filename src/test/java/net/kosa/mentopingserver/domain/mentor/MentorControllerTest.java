package net.kosa.mentopingserver.domain.mentor;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
import net.kosa.mentopingserver.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("aws")  // AWS 프로파일 활성화

public class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MentorApplicantService mentorApplicantService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MentorController mentorController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMentorApplication() throws Exception {
        MockMultipartFile certificationFile = new MockMultipartFile("certification_file", "test.pdf", "application/pdf", "Test data".getBytes());
        String fileUrl = "https://s3.bucket/test.pdf";

        when(s3Service.uploadFile(certificationFile)).thenReturn(fileUrl);

        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
                .memberId(1L)
                .field("ITSW")
                .certification_file(certificationFile)
                .build();

        MentorApplicantResponseDto responseDto = MentorApplicantResponseDto.builder()
                .applicationId("1")
                .status("PENDING")
                .build();

        when(mentorApplicantService.createMentorApplication(requestDto)).thenReturn(responseDto);

        mockMvc.perform(multipart("/mentor-applicants/upload")
                        .file(certificationFile)
                        .param("memberId", "1")
                        .param("field", "ITSW")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
}
