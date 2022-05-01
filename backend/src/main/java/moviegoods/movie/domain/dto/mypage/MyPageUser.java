package moviegoods.movie.domain.dto.mypage;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MyPageUser {

    private String nickname;
    private String profile_url;
    private Long reliability;
}
