//package net.kosa.mentopingserver.domain.mentor;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import net.kosa.mentopingserver.domain.member.MemberRepository;
//import net.kosa.mentopingserver.domain.member.entity.Member;
//import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantRequestDto;
//import net.kosa.mentopingserver.domain.mentor.dto.MentorApplicantResponseDto;
//import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
//import net.kosa.mentopingserver.global.common.enums.Category;
//import net.kosa.mentopingserver.global.common.enums.Role;
//import net.kosa.mentopingserver.global.common.enums.Status;
////import net.kosa.mentopingserver.FirebaseStorageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional  // 테스트 후 데이터 롤백
//@ActiveProfiles("test")  //  'test' 프로필 활성화
//
//public class MentorApplicantServiceImplTest {
//
//    @Autowired
//    private MentorApplicantService mentorApplicantService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private MentorApplicantRepository mentorApplicantRepository;
//
////    @Autowired
////    private FirebaseStorageService firebaseStorageService; // 실제 Firebase와 통신
//
//    @Value("${firebase.bucket-name}")
//    private String firebaseBucketName;
//
//    @Value("${firebase.service-account-file}")
//    private String serviceAccountFile;
//
//
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        // Firebase 서비스 계정 파일 경로 설정
//        String serviceAccountPath = "src/test/resources/firebaseApiKey.json";
//        File serviceAccountFile = new File(serviceAccountPath);
//
//        if (!serviceAccountFile.exists()) {
//            throw new FileNotFoundException("Firebase 서비스 계정 파일이 없습니다: " + serviceAccountPath);
//        }
//
//        // FirebaseOptions로 Firebase App 초기화
//        FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setStorageBucket("mentoping-3d954.appspot.com")
//                .build();
//
//        if (FirebaseApp.getApps().isEmpty()) { // 이미 초기화된 경우 중복 실행 방지
//            FirebaseApp.initializeApp(options);
//        }
//    }
//
//
//
//    // 멘토 신청 생성 테스트 (실제 Firebase에 업로드)
//    @Test
//    public void testCreateMentorApplication() throws IOException {
//        // 실제 Member 엔터티 저장
//        Member member = Member.builder()
//                .email("test1@example.com")
//                .name("Test User1")
//                .nickname("testuser1")
//                .password("password")
//                .role(Role.ROLE_MENTEE)
//                .build();
//        memberRepository.save(member); // 실제 DB에 저장
//
//        // 실제 파일 로드
//        File file = new File("src/test/resources/testfile.pdf");  // 경로 수정
//        FileInputStream input = new FileInputStream(file);
//        MultipartFile certificationFile = new MockMultipartFile("certification_file", file.getName(), "application/pdf", input);
//        input.close();  // 스트림 닫기
//
//        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
//                .memberId(member.getId())
//                .field("ITSW")
//                .certification_file(certificationFile)
//                .build();
//
//        // FirebaseStorageService를 통해 실제 파일 업로드
//        String uploadedFileUrl = firebaseStorageService.uploadFile(certificationFile);
//        System.out.println("Uploaded File URL: " + uploadedFileUrl);  // 업로드된 파일 URL 출력
//
//        // 서비스 호출
//        MentorApplicantResponseDto responseDto = mentorApplicantService.createMentorApplication(requestDto);
//
//        // 결과 검증
//        assertNotNull(responseDto);
//        assertEquals("PENDING", responseDto.getStatus());
//
//        // 데이터베이스에 저장된 MentorApplicant 확인
//        MentorApplicant savedApplicant = mentorApplicantRepository.findById(Long.valueOf(responseDto.getApplicationId())).orElse(null);
//        assertNotNull(savedApplicant);
//        assertEquals(member.getId(), savedApplicant.getMember().getId());
//
//        // 업로드된 파일 URL 확인
////        assertEquals(uploadedFileUrl, savedApplicant.getFile());
//    }
//
//    // 멘토 신청 업데이트 테스트 (실제 Firebase에 파일 업로드)
//    @Test
//    public void testUpdateMentorApplication() throws IOException {
//        // 실제 Member 엔터티 저장
//        Member member = Member.builder()
//                .email("test2@example.com")
//                .name("Test User2")
//                .nickname("testuser2")
//                .password("password")
//                .role(Role.ROLE_MENTEE)
//                .build();
//        memberRepository.save(member);
//
//        // 멘토 신청 엔터티 생성 후 저장
//        MentorApplicant mentorApplicant = MentorApplicant.builder()
//                .member(member)
//                .category(Category.ITSW)
//                .file("old_certification.pdf")
//                .status(Status.PENDING)
//                .submittedAt(LocalDateTime.now())
//                .build();
//        mentorApplicantRepository.save(mentorApplicant);
//
//        // 실제 파일 로드
//        File file = new File("src/test/resources/new_testfile.pdf");
//        FileInputStream input = new FileInputStream(file);
//        MultipartFile newCertificationFile = new MockMultipartFile("certification_file", file.getName(), "application/pdf", input);
//        input.close();  // 스트림 닫기
//
//        MentorApplicantRequestDto requestDto = MentorApplicantRequestDto.builder()
//                .field("DESIGN")
//                .certification_file(newCertificationFile)
//                .status("APPROVED")
//                .review("Good work")
//                .build();
//
//        // FirebaseStorageService를 통해 실제 파일 업로드
//        String uploadedFileUrl = firebaseStorageService.uploadFile(newCertificationFile);
//        System.out.println("Uploaded File URL: " + uploadedFileUrl);  // 업로드된 파일 URL 출력
//
//        // 서비스 호출
//        MentorApplicantResponseDto responseDto = mentorApplicantService.updateMentorApplication(mentorApplicant.getId(), requestDto);
//
//        // 결과 검증
//        assertNotNull(responseDto);
//        assertEquals("APPROVED", responseDto.getStatus());
//
//        // 업데이트된 데이터베이스 확인
//        MentorApplicant updatedApplicant = mentorApplicantRepository.findById(mentorApplicant.getId()).orElse(null);
//        assertNotNull(updatedApplicant);
//        assertEquals("DESIGN", updatedApplicant.getCategory().name());
//        assertEquals(uploadedFileUrl, updatedApplicant.getFile());  // 업로드된 파일 URL 확인
//        assertEquals("APPROVED", updatedApplicant.getStatus().name());
//    }
//
//    // 멘토 신청 삭제 테스트
//    @Test
//    public void testDeleteMentorApplication() {
//        // 실제 Member 엔터티 저장
//        Member member = Member.builder()
//                .email("test3@example.com")
//                .name("Test User3")
//                .nickname("testuser3")
//                .password("password")
//                .role(Role.ROLE_MENTEE)
//                .build();
//        memberRepository.save(member);
//
//        // 멘토 신청 엔터티 생성 후 저장
//        MentorApplicant mentorApplicant = MentorApplicant.builder()
//                .member(member)
//                .category(Category.ITSW)
//                .file("certification.pdf")
//                .status(Status.PENDING)
//                .submittedAt(LocalDateTime.now())
//                .build();
//        mentorApplicantRepository.save(mentorApplicant);
//
//        // 삭제 전 확인
//        MentorApplicant foundApplicant = mentorApplicantRepository.findById(mentorApplicant.getId()).orElse(null);
//        assertNotNull(foundApplicant);
//
//        // 서비스 호출: 삭제
//        mentorApplicantService.deleteMentorApplication(mentorApplicant.getId());
//
//        // 삭제 후 데이터베이스에서 삭제되었는지 확인
//        MentorApplicant deletedApplicant = mentorApplicantRepository.findById(mentorApplicant.getId()).orElse(null);
//        assertNull(deletedApplicant);
//    }
//}
