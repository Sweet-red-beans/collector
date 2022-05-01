package moviegoods.movie.domain.dto.signin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SignInRequestDto {
    private String email;
    private String password;
    private String method;

}
