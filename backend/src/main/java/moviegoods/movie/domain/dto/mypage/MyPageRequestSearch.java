package moviegoods.movie.domain.dto.mypage;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MyPageRequestSearch {

    private Long user_id;
}
