//package net.kosa.mentopingserver.domain.chat;
//
//
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.*;
//import com.google.firebase.cloud.FirestoreClient;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.ExecutionException;
//
//
//@Service
//public class ChatServiceImpl implements ChatService {
//
//    public static final String COLLECTION_NAME = "chats";
//
//    @Override
//    public void saveMessage(String chatId, ChatMessage message) throws ExecutionException, InterruptedException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<DocumentReference> future = dbFirestore
//                .collection(COLLECTION_NAME)
//                .document(chatId)
//                .collection("messages")
//                .add(message);
//        future.get(); // 결과를 기다려서 예외를 처리합니다.
//    }
//
//    @Override
//    public Iterable<ChatMessage> getMessages(String chatId) throws ExecutionException, InterruptedException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        CollectionReference messagesRef = dbFirestore
//                .collection(COLLECTION_NAME)
//                .document(chatId)
//                .collection("messages");
//
//        ApiFuture<QuerySnapshot> future = messagesRef.get();
//        return future.get().toObjects(ChatMessage.class);
//    }
//}
