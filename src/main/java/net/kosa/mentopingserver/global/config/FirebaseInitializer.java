//package net.kosa.mentopingserver.global.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseInitializer {
//
//    @Value("${firebase.bucket-name}")
//    private String firebaseBucketName;
//
//    @Value("${firebase.service-account-file}")
//    private String serviceAccountFile;
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setStorageBucket(firebaseBucketName)
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
