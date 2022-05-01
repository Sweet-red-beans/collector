package moviegoods.movie.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long user_id);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    Optional<Object> findByEmailAndMethod(String email, Method method);
}
