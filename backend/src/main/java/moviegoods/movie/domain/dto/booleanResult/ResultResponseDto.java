package moviegoods.movie.domain.dto.booleanResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
public class ResultResponseDto {
    private boolean result;
}
