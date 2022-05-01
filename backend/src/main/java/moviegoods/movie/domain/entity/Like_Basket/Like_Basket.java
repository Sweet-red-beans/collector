package moviegoods.movie.domain.entity.Like_Basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.Event.Event;
import moviegoods.movie.domain.entity.Transaction.Transaction;
import moviegoods.movie.domain.entity.User.User;

import javax.persistence.*;

@Entity(name = "like_basket")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Like_Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_basket_id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}