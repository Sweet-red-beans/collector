package moviegoods.movie.domain.dto.informationShare;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class InformationShareRequestWrite {



    private Long user_id;
    @NotBlank
    private String title;

    @NotBlank
    private String cinema_name;

    @NotBlank
    private String cinema_area;

    @NotBlank
    private String  cinema_branch;
    @NotBlank
    private String  content;

    private MultipartFile image_url;

    public InformationShareRequestWrite(){

    }

    public InformationShareRequestWrite(Long user_id,MultipartFile image_url,String cinema_branch,String cinema_area,String cinema_name,String title,String content){
        this.cinema_area=cinema_area;
        this.cinema_branch=cinema_branch;
        this.cinema_name=cinema_name;
        this.content=content;
        this.title=title;
        this.user_id=user_id;
        this.image_url=image_url;
    }





}
