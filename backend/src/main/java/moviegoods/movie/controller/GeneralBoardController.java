package moviegoods.movie.controller;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.generalBoard.*;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.GeneralBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/general-board")
public class GeneralBoardController {
    private final GeneralBoardService generalBoardService;

    @PostMapping("/write")
    public ResponseEntity<ResultResponseDto> write(@Login User loginUser, @RequestParam(value="image_url",required = false) MultipartFile image_url,
                                         @RequestParam(value="title")String title,
                                         @RequestParam(value="content")String content
    ) throws IOException, FirebaseAuthException {
        GeneralBoardWriteRequestDto requestDto = new GeneralBoardWriteRequestDto(title, content, image_url);
        ResultResponseDto resultResponseDto = generalBoardService.savePost(loginUser,requestDto);

        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;

    }

    @GetMapping("/search")
    public ResponseEntity<List<GeneralBoardSearchResponseDto>> search(@ModelAttribute GeneralBoardSearchRequestDto requestDto){
        List<GeneralBoardSearchResponseDto> list = generalBoardService.search(requestDto);
        ResponseEntity<List<GeneralBoardSearchResponseDto>> entity = new ResponseEntity<>(list, HttpStatus.OK);

        return entity;
    }

    @GetMapping("/detail")
    public GeneralBoardDetailResponseDto detail(@Login User loginUser, @ModelAttribute GeneralBoardDetailRequestDto requestDto){
        GeneralBoardDetailResponseDto responseDto = generalBoardService.detailInfo(loginUser, requestDto);

        return responseDto;
    }

    @PostMapping("/comment")
    public ResponseEntity<ResultResponseDto> saveComment(@Login User loginUser, @RequestBody GeneralBoardCommentRequestDto requestDto){
        ResultResponseDto resultResponseDto = generalBoardService.saveComment(loginUser,requestDto);
        ResponseEntity<ResultResponseDto> result;
        result = new ResponseEntity<>(resultResponseDto, HttpStatus.OK);

        if (!resultResponseDto.isResult()) {
            result = new ResponseEntity<>(resultResponseDto, HttpStatus.UNAUTHORIZED);
        }

        return result;
    }

    @DeleteMapping("/detail")
    public ResultResponseDto deleteDetail(@Login User loginUser, @RequestBody GeneralBoardDetailDeleteRequestDto requestDto){
        ResultResponseDto resultResponseDto=generalBoardService.deleteDetail(loginUser, requestDto);

        return resultResponseDto;
    }

    @DeleteMapping("/comment")
    public ResultResponseDto deleteComment(@Login User loginUser, @RequestBody GeneralBoardCommentDeleteRequestDto requestDto){
        ResultResponseDto resultResponseDto=generalBoardService.deleteComment(loginUser, requestDto);

        return resultResponseDto;
    }


}
