package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomRequestDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageCreateRoomResponseDto;
import moviegoods.movie.domain.dto.directMessage.DirectMessageListResponseDto;
import moviegoods.movie.domain.dto.Manager.ManagerResponseDto;
import moviegoods.movie.domain.entity.ChatRoom.Chat_Room;
import moviegoods.movie.domain.entity.ChatRoom.ChatRoomRepository;
import moviegoods.movie.domain.entity.ChatRoomJoin.ChatRoomJoinRepository;
import moviegoods.movie.domain.entity.ChatRoomJoin.Chat_Room_Join;
import moviegoods.movie.domain.entity.Event.Event;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Transaction.Status;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.Transaction.TransactionRepository;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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

    public DirectMessageCreateRoomResponseDto createRoom(DirectMessageCreateRoomRequestDto requestDto){
        Chat_Room chat_room = new Chat_Room();
        log.info("transaction_id={}", requestDto.getTransaction_id());


        Optional<Transaction> relatedTransaction = transactionRepository.findById(requestDto.getTransaction_id());
        if(relatedTransaction.isPresent()) {
            Transaction transaction = relatedTransaction.get();
            Long writer_id = transaction.getUser().getUser_id();
            User user = userRepository.getById(requestDto.getUser_id());
            User writerUser = userRepository.getById(writer_id);
            chat_room.setTransaction(transaction);
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
                            user.getUser_id(),
                            writer_id);

            return responseDto;

        }
        else {
            DirectMessageCreateRoomResponseDto responseDto =
                    new DirectMessageCreateRoomResponseDto(false, null, null, null);

            return responseDto;
        }
    }

    public List<DirectMessageListResponseDto> findMessageRooms(Long user_id) {
        List<DirectMessageListResponseDto> roomsList = new ArrayList<>();
        Optional<User> user = userRepository.findById(user_id);
        User findedUser = user.get();
        List<Chat_Room_Join> chat_room_joins = findedUser.getChat_room_joins();
        for (Chat_Room_Join chat_room_join : chat_room_joins) {
            Chat_Room chat_room = chat_room_join.getChat_room();
            Long chat_room_id = chat_room.getChat_room_id();

            Long not_mine_id = null;
            String not_mine_nickname = null;
            String not_mine_profile_url = null;
            Long not_mine_reliability = null;
            Long transaction_id = null;
            Boolean is_complete = false;
            String recent_message = null;
            LocalDateTime recent_message_date = LocalDateTime.of(2019, 11, 12, 12, 32,22,3333);

            String searchJpql = "select c from chat_room_join c where c.chat_room = '" + chat_room_id + "'";
            List<Chat_Room_Join> list = em.createQuery(searchJpql, Chat_Room_Join.class).getResultList();
            for (Chat_Room_Join chatRoomJoin : list) {
                User user1 = chatRoomJoin.getUser();
                if(!user1.getUser_id().equals(user_id)) {
                    not_mine_id = user1.getUser_id();
                    not_mine_nickname = user1.getNickname();
                    not_mine_profile_url = user1.getProfile_url();
                    not_mine_reliability = user1.getReliability();
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

            roomsList.add(new DirectMessageListResponseDto(
                    chat_room_id,
                    not_mine_id,
                    not_mine_nickname,
                    not_mine_profile_url,
                    not_mine_reliability,
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