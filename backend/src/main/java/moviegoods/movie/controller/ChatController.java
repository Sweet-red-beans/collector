package moviegoods.movie.controller;


import com.google.api.Http;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.configure.SessionConfig;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.directMessage.*;
import moviegoods.movie.domain.entity.ChatRoom.ChatRoomRepository;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Message.MessageRepository;
import moviegoods.movie.domain.entity.Report.Report;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import moviegoods.movie.service.FireBaseService;
import moviegoods.movie.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/direct-message")
public class ChatController {
    private final UserRepository informationShareUserRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatService messageService;
    private final FireBaseService fireBaseService;

    @MessageMapping("/chat/message")
    public void message(@Login User loginUser,DirectMessage message, SimpMessageHeaderAccessor headerAccessor) throws IOException, FirebaseAuthException {

        User user1 = (User) headerAccessor.getSessionAttributes().get(SessionConfig.SessionConst.LOGIN_MEMBER);
        String url= messageService.saveMessage(user1,message);
        if(message.getImage_url()!=null){
            message.setImage_url(url);
        }else{
            message.setImage_url("");
        }
        message.setNickname(user1.getNickname());
        message.setUser_id(user1.getUser_id());


        messagingTemplate.convertAndSend("/sub/chat/room/"+message.getChat_room_id(), message);

    }


    @PostMapping("/detail")
    public Result saveMessage(@Login User loginUser,@ModelAttribute DirectMessage message) throws IOException, FirebaseAuthException {

        messageService.saveMessage(loginUser,message);
        Result result=new Result();
        result.setResult(true);
        return result;

    }

    @PostMapping("/transaction-complete")
    public Result complete(@Login User loginUser,@RequestBody DirectMessageRequestComplete dmrc){
        Boolean check= messageService.updateTransactionComplete(loginUser,dmrc);
        Result result=new Result();
        if(check==true){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        return result;
    }

    @PostMapping("/reliability")
    public Result updateReliability(@Login User loginUser, @RequestBody DirectMessageRequestReliability dmrr){
        Boolean check=messageService.updateReliability(loginUser, dmrr);
        Result result=new Result();
        if(check==true){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        return result;
    }

    @PostMapping("/report")
    public Result Report(@Login User loginUser, @RequestBody DirectMessageRequestReport dmrr){
        Report report=messageService.report(loginUser, dmrr);
        Result result=new Result();
        if(report!=null){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        return result;
    }

}
