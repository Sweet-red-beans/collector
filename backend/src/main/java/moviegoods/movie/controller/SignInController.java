package moviegoods.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.dto.signin.SignInRequestDto;
import moviegoods.movie.domain.dto.signin.SignInResponseDto;
import moviegoods.movie.service.SignInService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/signin")
public class SignInController {

    private final SignInService signInService;

    @PostMapping
    public SignInResponseDto login(@RequestBody SignInRequestDto requestDto, HttpServletRequest httpServletRequest) {

        return signInService.login(requestDto, httpServletRequest);
    }

    @ResponseBody
    @GetMapping("/oauth2/code/kakao")
    public SignInRequestDto  kakaoCallback(@RequestParam String code, HttpServletRequest httpServletRequest){

        String access_Token = signInService.getKaKaoAccessToken(code);
        SignInRequestDto userInfo = signInService.getUserInfo(access_Token);

        return userInfo;

    }

    @GetMapping("/auth/google/callback")
    public SignInRequestDto googleCallback(@RequestParam String code) {
        String access_Token = signInService.googleRequestAccessToken(code);
        SignInRequestDto userInfo = signInService.googleGetUserInfo(access_Token);

        return userInfo;
    }

}