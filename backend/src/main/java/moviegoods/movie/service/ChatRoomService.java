package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomRequestDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomResponseDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageListResponseDto;
import moviegoods.movie.domain.entity.ChatRoom.ChatRoomRepository;
import moviegoods.movie.domain.entity.ChatRoom.Chat_Room;
import moviegoods.movie.domain.entity.ChatRoomJoin.ChatRoomJoinRepository;
import moviegoods.movie.domain.entity.ChatRoomJoin.Chat_Room_Join;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.Transaction.TransactionRepository;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import moviegoods.movie.domain.entity.User.UserStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final EntityManager em;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DirectMessageCreateRoomResponseDto createRoom(User loginUser, DirectMessageCreateRoomRequestDto requestDto){

        if (loginUser == null) {
            DirectMessageCreateRoomResponseDto responseDto =
                    new DirectMessageCreateRoomResponseDto(false, null, null, null, null, null, null, null, null, null, null, null);
            return responseDto;
        }
        //-------------------------
        String recent_message = null;
        LocalDateTime recent_message_date = null;
        //-------------------------
        Long user_id = loginUser.getUser_id();
        UserStatus user_status = loginUser.getUser_status();

        log.info("transaction_id={}", requestDto.getTransaction_id());

        Optional<Transaction> relatedTransaction = transactionRepository.findById(requestDto.getTransaction_id());
        if(relatedTransaction.isPresent()) {
            Transaction transaction = relatedTransaction.get();
            Long transaction_id = transaction.getTransaction_id();
            Boolean is_complete = false;
            Status transaction_status = transaction.getStatus();
            if(transaction_status.equals(Status.마감)) {
                is_complete = true;
            }
            User notMineUser = transaction.getUser();
            Long not_mine_id = notMineUser.getUser_id();
            String not_mine_nickname = notMineUser.getNickname();
            String not_mine_profile_url = notMineUser.getProfile_url();
            Long not_mine_reliability = notMineUser.getReliability();
            UserStatus not_mine_user_status = notMineUser.getUser_status();


            //중복 확인
            boolean ifExistUserId = false;
            boolean ifExixtWriterId = false;
            Long exist_chat_room_id = null;
            String searchChatRoomJpql = "select c from chat_room c where c.transaction = '" + transaction_id + "'";
            List<Chat_Room> list = em.createQuery(searchChatRoomJpql, Chat_Room.class).getResultList();
            for (Chat_Room chat_room : list) {
                Long chat_room_id = chat_room.getChat_room_id();
                String searchChatRoomJoinJpql = "select j from chat_room_join j where j.chat_room = '" + chat_room_id + "'";
                List<Chat_Room_Join> resultList = em.createQuery(searchChatRoomJoinJpql, Chat_Room_Join.class).getResultList();
                for (Chat_Room_Join chat_room_join : resultList) {
                    if(!ifExistUserId || !ifExixtWriterId) {
                        Long exist_user_id = chat_room_join.getUser().getUser_id();
                        if(exist_user_id == user_id) {
                            ifExistUserId = true;
                        }
                        if(exist_user_id == not_mine_id) {
                            ifExixtWriterId = true;
                        }
                    }
                    else if(ifExistUserId && ifExixtWriterId) {
                        exist_chat_room_id = chat_room_id;
                        break;
                    }
                }
            }

            //중복일때
            if(ifExistUserId && ifExixtWriterId && (exist_chat_room_id != null)) {
                DirectMessageCreateRoomResponseDto responseDto =
                        new DirectMessageCreateRoomResponseDto(true,
                                exist_chat_room_id,
                                not_mine_id,
                                not_mine_nickname,
                                not_mine_profile_url,
                                not_mine_reliability,
                                user_status,
                                not_mine_user_status,
                                transaction_id,
                                is_complete,
                                recent_message,
                                recent_message_date);

                return responseDto;
            }

            //중복이 아닐때
            User user = userRepository.getById(user_id);
            User writerUser = userRepository.getById(not_mine_id);

            Chat_Room chat_room = new Chat_Room();
            chat_room.setTransaction(transaction);
            chat_room.setCreate_date(LocalDateTime.now());
            Chat_Room savedMessageRoom = chatRoomRepository.save(chat_room);

            Chat_Room_Join chat_room_join = new Chat_Room_Join();
            chat_room_join.setChat_room(savedMessageRoom);
            chat_room_join.setUser(user);
            Chat_Room_Join chat_room_join_Writer = new Chat_Room_Join();
            chat_room_join_Writer.setChat_room(savedMessageRoom);
            chat_room_join_Writer.setUser(writerUser);
            chatRoomJoinRepository.save(chat_room_join);
            chatRoomJoinRepository.save(chat_room_join_Writer);

            List<Chat_Room_Join> user_chat_room_join = user.getChat_room_joins();
            user_chat_room_join.add(chat_room_join);
            List<Chat_Room_Join> writer_chat_room_joins = writerUser.getChat_room_joins();
            writer_chat_room_joins.add(chat_room_join_Writer);

            DirectMessageCreateRoomResponseDto responseDto =
                    new DirectMessageCreateRoomResponseDto(true,
                            savedMessageRoom.getChat_room_id(),
                            not_mine_id,
                            not_mine_nickname,
                            not_mine_profile_url,
                            not_mine_reliability,
                            user_status,
                            not_mine_user_status,
                            transaction_id,
                            is_complete,
                            recent_message,
                            recent_message_date);

            return responseDto;

        }
        else {
            DirectMessageCreateRoomResponseDto responseDto =
                    new DirectMessageCreateRoomResponseDto(false, null, null, null, null, null, null, null, null, null, null, null);

            return responseDto;
        }
    }

    public List<DirectMessageListResponseDto> findMessageRooms(User loginUser, Long user_id) {
        List<DirectMessageListResponseDto> roomsList = new ArrayList<>();
        Optional<User> user = userRepository.findById(user_id);
        User findedUser = user.get();
        List<Chat_Room_Join> chat_room_joins = findedUser.getChat_room_joins();
        for (Chat_Room_Join chat_room_join : chat_room_joins) {
            Chat_Room chat_room = chat_room_join.getChat_room();
            Long chat_room_id = chat_room.getChat_room_id();
            LocalDateTime create_date = chat_room.getCreate_date(); //DM창 생성시간

            Long not_mine_id = null;
            String not_mine_nickname = null;
            String not_mine_profile_url = null;
            Long not_mine_reliability = null;
            @NotNull UserStatus not_mine_status = null;
            Long transaction_id = null;
            Boolean is_complete = false;
            String recent_message = null;
//            LocalDateTime recent_message_date = LocalDateTime.of(2019, 11, 12, 12, 32,22,3333);
            LocalDateTime recent_message_date = null;

            String searchJpql = "select c from chat_room_join c where c.chat_room = '" + chat_room_id + "'";
            List<Chat_Room_Join> list = em.createQuery(searchJpql, Chat_Room_Join.class).getResultList();
            for (Chat_Room_Join chatRoomJoin : list) {
                User user1 = chatRoomJoin.getUser();
                if(!user1.getUser_id().equals(user_id)) {
                    not_mine_id = user1.getUser_id();
                    not_mine_nickname = user1.getNickname();
                    not_mine_profile_url = user1.getProfile_url();
                    not_mine_reliability = user1.getReliability();
                    not_mine_status = user1.getUser_status();
                }
            }

            Transaction transaction = chat_room.getTransaction();
            transaction_id = transaction.getTransaction_id();
            Status status = transaction.getStatus();
            if(status.equals(Status.마감)) {
                is_complete = true;
            }
            List<Message> messages = chat_room.getMessages();
            if(!messages.isEmpty()) {
                Comp comp = new Comp();
                Collections.sort(messages, comp);
                Message message = messages.get(0);
                recent_message = message.getContent_detail().getContent();
                recent_message_date = message.getContent_detail().getWritten_date();
            }
            else {
                //메시지가 없으면 방 생성날짜로
                recent_message_date = create_date;
            }

            roomsList.add(new DirectMessageListResponseDto(
                    chat_room_id,
                    not_mine_id,
                    not_mine_nickname,
                    not_mine_profile_url,
                    not_mine_reliability,
                    not_mine_status,
                    transaction_id,
                    is_complete,
                    recent_message,
                    recent_message_date));
        }

        Comp2 comp2 = new Comp2();
        Collections.sort(roomsList, comp2);

        return roomsList;
    }

    private static class Comp implements Comparator<Message> {

        @Override
        public int compare(Message message1, Message message2) {

            LocalDateTime date1 = message1.getContent_detail().getWritten_date();
            LocalDateTime date2 = message2.getContent_detail().getWritten_date();

            int result = date2.compareTo(date1);
            return result;
        }
    }

    private static class Comp2 implements Comparator<DirectMessageListResponseDto> {

        @Override
        public int compare(DirectMessageListResponseDto responseDto1, DirectMessageListResponseDto responseDto2) {

            LocalDateTime date1 = responseDto1.getRecent_message_date();
            LocalDateTime date2 = responseDto2.getRecent_message_date();

            int result = date2.compareTo(date1);
            return result;
        }
    }
}