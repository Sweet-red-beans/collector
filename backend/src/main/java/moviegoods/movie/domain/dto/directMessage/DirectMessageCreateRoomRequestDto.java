package moviegoods.movie.domain.dto.directMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageCreateRoomRequestDto {

    Long user_id;
    Long transaction_id;
}
