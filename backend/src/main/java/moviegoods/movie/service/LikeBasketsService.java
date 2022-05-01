package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import moviegoods.movie.domain.entity.Like_Basket.LikeBasketRepository;
import moviegoods.movie.domain.entity.Like_Basket.Like_Basket;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class LikeBasketsService {
    private final EntityManager em;
    private final LikeBasketRepository likeBasketRepository;

    public Boolean isLikeEvent(Long user_id, Long event_id) {
        Boolean is_like = Boolean.FALSE;

        String likeJpql = "select count(l) from like_basket l where event_id = :event_id and user_id = :user_id";
        Object object = em.createQuery(likeJpql).setParameter("event_id", event_id)
                .setParameter("user_id", user_id).setMaxResults(1).getSingleResult();

        if (Integer.parseInt(object.toString()) == 1) {
            is_like = Boolean.TRUE;
        }
        return is_like;
    }

    public Long selectLikeEvent(Long user_id, Long event_id) {
        String likeJpql = "select l from like_basket l where event_id = :event_id and user_id = :user_id";

        Like_Basket like_basket = em.createQuery(likeJpql, Like_Basket.class).setParameter("event_id", event_id)
                .setParameter("user_id", user_id).getSingleResult();
        Long like_basket_id = like_basket.getLike_basket_id();

        return like_basket_id;
    }

    public Boolean deleteLike(Long like_basket_id, Long user_id) {
        Like_Basket like_basket = likeBasketRepository.findById(like_basket_id).orElseThrow(() ->
                new IllegalArgumentException("해당 좋아요 내역이 없습니다. like_basket_id = {}"+ like_basket_id));

        if (like_basket.getUser().getUser_id() == user_id) {
            likeBasketRepository.deleteById(like_basket_id);
        }

        Boolean is_exists=likeBasketRepository.existsById(like_basket_id);
        Boolean result = false;
        if (is_exists == false) {
            result = true;
        }
        return result;

    }

    public Boolean isLikeTransaction(Long user_id, Long transaction_id) {
        String likeJpql = "select count(l) from like_basket l where transaction_id = :transaction_id and user_id = :user_id";

        Object object = em.createQuery(likeJpql).setParameter("transaction_id", transaction_id)
                .setParameter("user_id",user_id).setMaxResults(1).getSingleResult();
        Boolean is_like = Boolean.FALSE;
        if (Integer.parseInt(object.toString()) == 1) {
            is_like = Boolean.TRUE;
        }
        return is_like;

    }

    public Long selectLikeTransaction(Long user_id, Long transaction_id) {
        String likeJpql = "select l from like_basket l where transaction_id = :transaction_id and user_id = :user_id";

        Like_Basket like_basket = em.createQuery(likeJpql, Like_Basket.class).setParameter("transaction_id", transaction_id)
                .setParameter("user_id", user_id).getSingleResult();
        Long like_basket_id = like_basket.getLike_basket_id();

        return like_basket_id;
    }

}
