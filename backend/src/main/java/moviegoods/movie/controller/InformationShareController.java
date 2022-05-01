package moviegoods.movie.controller;


import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.informationShare.*;
import moviegoods.movie.domain.entity.Post.Post;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.FireBaseService;
import moviegoods.movie.service.InformationShareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/information-share")
public class InformationShareController {

    private final InformationShareService informationShareService;
    private final FireBaseService fireBaseService;

    @PostMapping("/write")
    public ResponseEntity<Result> write2(@Login User loginUser, @RequestParam(value="image_url",required = false)MultipartFile image_url,
                         @RequestParam(value="cinema_name")String cinema_name,
                         @RequestParam(value="cinema_area")String cinema_area,
                         @RequestParam(value="cinema_branch")String cinema_branch,
                         @RequestParam(value="title")String title,
                         @RequestParam(value="content")String content
    ) throws IOException, FirebaseAuthException {
        ResponseEntity<Result> result2;
        Result result=new Result();
        if (loginUser == null) {
            result2 = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            return result2;
        }
        Long user_id = loginUser.getUser_id();

        if (cinema_area.equals("") || cinema_name.equals("") || cinema_branch.equals("") || user_id.equals("") || title.equals("") || content.equals("")) {
            result.setResult(false);
            result2 = new ResponseEntity<>(result, HttpStatus.OK);

            return result2;
        }
        InformationShareRequestWrite isrw=new InformationShareRequestWrite(user_id,image_url,cinema_branch,cinema_area,cinema_name,title,content);

        //informationShareRepository.savePost(isrw);
        List<Post> postList=informationShareService.savePost(isrw);
        for (Post post : postList) {
            log.info("저장된post_title={}",post.getTitle());
            log.info("저장된 post_id={}",post.getPost_id());
            log.info("저장된 post_user_id={}",post.getUser().getUser_id());
        }

        result.setResult(true);
        result2 = new ResponseEntity<>(result, HttpStatus.OK);

        return result2;

    }

    @GetMapping("/search")
    public ResponseEntity<List<InformationShareResponseSearch>> search(@ModelAttribute InformationShareRequestSearch isrs){
        List<InformationShareResponseSearch> list = informationShareService.Search(isrs);
        ResponseEntity<List<InformationShareResponseSearch>> entity=new ResponseEntity<>(list, HttpStatus.OK);
        return  entity;
    }

    @GetMapping("/detail")
    public InformationShareResponseDetail detail(@Login User loginUser, @ModelAttribute InformationShareRequestDetail isrd){
        InformationShareResponseDetail ifsr=informationShareService.detailInfo(loginUser, isrd);
        return ifsr;
    }

    @PostMapping("/comment")
    public ResponseEntity<ResultResponseDto> saveComment(@Login User loginUser, @RequestBody InformationShareRequestSaveComment isrsc){
        ResultResponseDto resultResponseDto=informationShareService.saveComment(loginUser,isrsc);
        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }

    @DeleteMapping("/detail")
    public Result deleteDetail(@Login User loginUser, @RequestBody InformationShareRequestDeleteDetail isrdd){
        Boolean check=informationShareService.deleteDetail(loginUser, isrdd);
        Result result=new Result();
        if(check==false){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        return result;
    }

    @DeleteMapping("/comment")
    public Result deleteComment(@Login User loginUser, @RequestBody InformationShareRequestDeleteComment isrdc){
        Boolean check=informationShareService.deleteComment(loginUser, isrdc);
        Result result=new Result();
        if(check==false){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        return result;
    }


}
