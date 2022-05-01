package moviegoods.movie.domain.dto.logout;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogOutRequestDto {
    private Long user_id;
}
