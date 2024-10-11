package net.kosa.mentopingserver;

import net.kosa.mentopingserver.domain.mentor.MentorApplicantRepository;
import net.kosa.mentopingserver.domain.mentor.entity.MentorApplicant;
import net.kosa.mentopingserver.global.common.enums.Category;
import net.kosa.mentopingserver.global.common.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {


    private final S3Client s3Client;
    private final String bucketName;
    private final MentorApplicantRepository mentorApplicantRepository;

    public S3Service(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                     @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                     @Value("${cloud.aws.region.static}") String region,
                     @Value("${cloud.aws.s3.bucket}") String bucketName, MentorApplicantRepository mentorApplicantRepository) {

        this.bucketName = bucketName;
        this.mentorApplicantRepository = mentorApplicantRepository;
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        String url = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        return url;
    }
}
