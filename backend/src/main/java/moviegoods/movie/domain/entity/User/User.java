package moviegoods.movie.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.ChatRoomJoin.Chat_Room_Join;
import moviegoods.movie.domain.entity.Comment.Comment;
import moviegoods.movie.domain.entity.Like_Basket.Like_Basket;
import moviegoods.movie.domain.entity.Message.Message;
import moviegoods.movie.domain.entity.Post.Post;
import moviegoods.movie.domain.entity.Report.Report;
import moviegoods.movie.domain.entity.Transaction.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank
    @Column(length = 100)
    private String email;

    @NotBlank
    @Column(length = 30)
    private String nickname;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String password;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String profile_url;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @NotNull
    @Column(columnDefinition = "bigint default 0")
    private Long reliability;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus user_status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Method method;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Chat_Room_Join> chat_room_joins = new ArrayList<>();

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private List<Like_Basket> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();


}
