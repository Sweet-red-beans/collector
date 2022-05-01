package moviegoods.movie.domain.dto.mypage;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MyPageRequestNickname {
   private Long user_id;
    private String nickname;
}
