package SweetredBeans.collector.domain.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "chat_room_join")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat_Room_Join {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_room_join_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private Chat_Room chat_room;

}
