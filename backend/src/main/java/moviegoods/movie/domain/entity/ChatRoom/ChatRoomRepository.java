package moviegoods.movie.domain.entity.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<Chat_Room, Long> {
    Optional<Chat_Room> findById(Long chat_room_id);
}
