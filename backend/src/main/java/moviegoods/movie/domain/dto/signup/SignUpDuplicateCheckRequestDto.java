package moviegoods.movie.domain.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDuplicateCheckRequestDto {
    private String email;
    private String nickname;

}
