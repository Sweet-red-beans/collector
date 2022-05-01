package moviegoods.movie.domain.dto.informationShare;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class InformationShareRequestDetail {
   private Long post_id;
   private Long user_id;
}
