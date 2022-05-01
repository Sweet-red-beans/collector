package moviegoods.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.signup.SignUpDuplicateCheckRequestDto;
import moviegoods.movie.domain.dto.signup.SignUpRequestDto;
import moviegoods.movie.domain.entity.User.Method;
import moviegoods.movie.service.SignUpService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResultResponseDto signup(@RequestBody SignUpRequestDto requestDto){
        ResultResponseDto resultResponseDto = signUpService.saveUser(requestDto, Method.일반);

        return resultResponseDto;
    }

    @PostMapping("/duplicate-check")
    public ResultResponseDto duplicateCheck(@RequestBody SignUpDuplicateCheckRequestDto requestDto){
        ResultResponseDto resultResponseDto = signUpService.duplicateCheck(requestDto,Method.일반);

        return resultResponseDto;
    }

}


