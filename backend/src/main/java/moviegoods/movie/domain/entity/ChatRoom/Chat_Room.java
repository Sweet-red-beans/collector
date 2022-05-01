package moviegoods.movie.domain.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.ChatRoomJoin.Chat_Room_Join;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "chat_room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat_Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_room_id;

    @NotNull
    @DateTimeFormat
    private LocalDateTime create_date;

    @OneToMany(mappedBy = "chat_room", cascade = CascadeType.ALL)
    private List<Chat_Room_Join> chat_room_joins = new ArrayList<>();

    @OneToMany(mappedBy = "chat_room",cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

}

