package moviegoods.movie.domain.dto.signin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.User.UserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto {
    private String nickname;
    private String image_url;
    private UserStatus user_status;
    private Boolean result;
    private String authority;
}
