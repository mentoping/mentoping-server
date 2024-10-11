//package net.kosa.mentopingserver;
//
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class FirebaseStorageService {
//
//    private final String bucketName = "mentoping-3d954.appspot.com"; // Firebase Storage 버킷 이름
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        Bucket bucket = storage.get(bucketName);
//        String fileName = "mentor/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
//
//        // 파일 업로드 후 다운로드 URL 반환
//        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
//    }
//}