package moviegoods.movie.domain.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponseDto {

    private String report_content;
    private LocalDateTime written_date;
    private String nickname;
    private Long user_id;
    private Long reported_user_id;
    private String reported_nickname;
    private String reported_content;
    private Boolean is_complete;
}