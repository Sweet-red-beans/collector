package moviegoods.movie.domain.entity.Content_Detail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentDetailRepository extends JpaRepository<Content_Detail, Long> {
    Optional<Content_Detail> findById(Long content_detail_id);
}
