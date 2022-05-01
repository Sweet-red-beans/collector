package moviegoods.movie.domain.dto.generalBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralBoardWriteRequestDto {
    private String title;
    private String content;
    private MultipartFile image_url;
}
