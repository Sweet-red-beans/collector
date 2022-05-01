package moviegoods.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.main.MainDailyCommunityRequestDto;
import moviegoods.movie.domain.dto.main.MainDailyCommunityResponseDto;
import moviegoods.movie.domain.dto.main.MainEventLimitResponseDto;
import moviegoods.movie.domain.dto.main.MainVideoResponseDto;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.MainService;
import moviegoods.movie.service.MovieVideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final MainService mainService;
    private final MovieVideoService movieVideoService;

    @GetMapping("/video")
    public MainVideoResponseDto video() throws IOException {
        String src=movieVideoService.startDriver();
        MainVideoResponseDto mainVideoResponseDto=new MainVideoResponseDto();
        mainVideoResponseDto.setSrc(src);
        return mainVideoResponseDto;
    }

    @GetMapping("/event-limit")
    public ResponseEntity<List<MainEventLimitResponseDto>> eventLimit(@Login User loginUser){
        List<MainEventLimitResponseDto> list = mainService.eventLimit(loginUser);
        ResponseEntity<List<MainEventLimitResponseDto>> result = new ResponseEntity<>(list, HttpStatus.OK);
        return result;
    }

    @GetMapping("/daily-community")
    public ResponseEntity<List<MainDailyCommunityResponseDto>> dailyCommunity(@ModelAttribute MainDailyCommunityRequestDto requestDto) {
        List<MainDailyCommunityResponseDto> list = mainService.dailyCommunity(requestDto);
        ResponseEntity<List<MainDailyCommunityResponseDto>> result = new ResponseEntity<>(list, HttpStatus.OK);
        return result;
    }


}
