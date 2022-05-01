package moviegoods.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomRequestDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomResponseDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageDetailResponseDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageListResponseDto;
import moviegoods.movie.domain.entity.ChatRoom.ChatRoomRepository;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.ChatRoomService;
import moviegoods.movie.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/direct-message")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping
    public ResponseEntity<DirectMessageCreateRoomResponseDto> create(@Login User loginUser, @RequestBody DirectMessageCreateRoomRequestDto requestDto){
        DirectMessageCreateRoomResponseDto responseDto = chatRoomService.createRoom(loginUser, requestDto);
        ResponseEntity<DirectMessageCreateRoomResponseDto> result;
        result = new ResponseEntity<>(responseDto, HttpStatus.OK);

        if (!responseDto.isResult()) {
            result = new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<DirectMessageListResponseDto>>> directMessageList (@Login User loginUser) {
        Long user_id = null;
        if (loginUser != null) {
            user_id = loginUser.getUser_id();
        }
        List<DirectMessageListResponseDto> roomsList = chatRoomService.findMessageRooms(loginUser, user_id);
        Map<String, List<DirectMessageListResponseDto>> roomsListJson = new HashMap<>();
        roomsListJson.put("room_id", roomsList);
        ResponseEntity<Map<String, List<DirectMessageListResponseDto>>> result = new ResponseEntity<>(roomsListJson, HttpStatus.OK);
        return result;
    }

    @GetMapping("/detail")
    public ResponseEntity<Map<String, List<DirectMessageDetailResponseDto>>> detail (@Login User loginUser, @RequestParam Long room_id) {
        List<DirectMessageDetailResponseDto> messagesList = chatService.show(loginUser, room_id);
        Map<String, List<DirectMessageDetailResponseDto>> messagesListJson = new HashMap<>();

        messagesListJson.put("message", messagesList);

        ResponseEntity<Map<String, List<DirectMessageDetailResponseDto>>> result = new ResponseEntity<>(messagesListJson, HttpStatus.OK);
        return result;
    }

    @DeleteMapping
    public Boolean create(@Login User loginUser, @RequestBody DirectMessageCreateRoomResponseDto responseDto){
        chatRoomRepository.deleteById(responseDto.getChat_room_id());
        return true;
    }

}
