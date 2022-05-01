package moviegoods.movie.service;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.directMessage.*;
import moviegoods.movie.domain.entity.ChatRoom.ChatRoomRepository;
import moviegoods.movie.domain.entity.ChatRoom.Chat_Room;
import moviegoods.movie.domain.entity.Content_Detail.Content_Detail;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Message.MessageRepository;
import moviegoods.movie.domain.entity.Report.Report;
import moviegoods.movie.domain.entity.Report.ReportRepository;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.Transaction.TransactionRepository;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final UserRepository informationShareUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final TransactionRepository directMessageTransactionRepository;
    private final ReportRepository directMessageReportRepository;
    private final FireBaseService fireBaseService;
    private final ContentDetailService contentDetailService;

    @Transactional(rollbackFor = Exception.class)
    public String saveMessage(User loginUser, DirectMessage message) throws IOException, FirebaseAuthException {
        String finalUrl="";
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        User user=loginUser;
        Message message1=new Message();
        Chat_Room chat_room=chatRoomRepository.findById(message.getChat_room_id()).get();
        Content_Detail content_detail=new Content_Detail();
        content_detail.setWritten_date(LocalDateTime.now());
        String firebaseUrl="";
        content_detail.setContent(message.getContent());
        if(message.getImage_url()!=null){

            content_detail.setContent(message.getContent());
            firebaseUrl+=changeStringToFileAndUpload(message.getImage_url());
            message1.setImage_url(firebaseUrl);
            finalUrl+=firebaseUrl;
        }
        message1.setContent_detail(content_detail);
        message1.setUser(user);
        message1.setChat_room(chat_room);
        messageRepository.save(message1);

        return finalUrl;

    }

    public String changeStringToFileAndUpload(String base64url) throws IOException, FirebaseAuthException {
        String firebaseUrl="";
        String url="";
        String contentType="";
        if(base64url.contains("image/png")){
            contentType+="image/png";
        }else if(base64url.contains("image/jpeg")){
            contentType+="image/jpeg";
        }
        int startIdx= base64url.indexOf("base64,");
        int start=startIdx+7;
        for(int i=start; i<base64url.length(); i++){
            url+=base64url.charAt(i);
        }
        byte[] decodeByte= Base64.getDecoder().decode(url.getBytes());
        String nameFile= UUID.randomUUID().toString();
        fireBaseService.uploadFiles2(decodeByte,contentType,nameFile);
        firebaseUrl+="https://firebasestorage.googleapis.com/v0/b/stroagetest-f0778.appspot.com/o/"+nameFile+"?alt=media";

        return firebaseUrl;



    }

    public List<DirectMessageDetailResponseDto> show(User loginUser, Long room_id) {
        List<DirectMessageDetailResponseDto> messagesList = new ArrayList<>();
        Optional<Chat_Room> chatRoom = chatRoomRepository.findById(room_id);
        Chat_Room findedRoom = chatRoom.get();
        List<Message> messages = findedRoom.getMessages();
        for (Message message : messages) {
            Content_Detail content_detail = message.getContent_detail();
            String content = content_detail.getContent();
            LocalDateTime written_date = content_detail.getWritten_date();
            String image_url = message.getImage_url();

            User user = message.getUser();
            Long user_id = user.getUser_id();
            String nickname = user.getNickname();
            String profile_url = user.getProfile_url();
            Long reliability = user.getReliability();

            messagesList.add(new DirectMessageDetailResponseDto(content, written_date, image_url, user_id, nickname, profile_url, reliability));
        }
        return messagesList;
    }

    public Boolean updateTransactionComplete(User loginUser, DirectMessageRequestComplete dmrc){
        Boolean check;
        Long transaction_id=dmrc.getTransaction_id();
        Transaction transaction=directMessageTransactionRepository.findById(transaction_id).get();
        transaction.setStatus(Status.마감);
        Transaction t=directMessageTransactionRepository.save(transaction);
        if(t.getStatus().equals(Status.마감)){
            check=true;
        }else{
            check=false;
        }
        return check;

    }

    public Boolean updateReliability(User loginUser, DirectMessageRequestReliability dmrr){ //메세지생성창에서 user_id 받아와야함
        Boolean check=false;
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        User user= loginUser;
        User user2=informationShareUserRepository.findById(dmrr.getUser_id()).get();
        Long n= user2.getReliability()+1;
        user2.setReliability(n);
        User u=informationShareUserRepository.save(user2);
        if(u.getReliability()==n){
            check=true;
        }
        return check;

    }

    public Report report(User loginUser, DirectMessageRequestReport dmrr){
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        Long transaction_id=dmrr.getTransaction_id();
        String report_content=dmrr.getReport_content();
        Report report=new Report();
        User user=loginUser;
        Transaction transaction=directMessageTransactionRepository.findById(transaction_id).get();
        Content_Detail content_detail=new Content_Detail();
        content_detail.setContent(report_content);
        content_detail.setWritten_date(LocalDateTime.now());
        user.getReports().add(report);
        report.setUser(user);
        content_detail.setReport(report);
        report.setContent_detail(content_detail);
        transaction.getReports().add(report);
        report.setTransaction(transaction);
        Report report1=directMessageReportRepository.save(report);

        return  report1;

    }
}