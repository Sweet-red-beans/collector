package moviegoods.movie.domain.entity.Post;

import moviegoods.movie.domain.entity.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long post_id);
}
