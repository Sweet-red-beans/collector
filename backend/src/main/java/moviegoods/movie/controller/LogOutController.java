package moviegoods.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.logout.LogOutRequestDto;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.LogOutService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users/logout")
public class LogOutController {

    private final LogOutService logOutService;

    @PostMapping
    public ResultResponseDto logout(@Login User loginUser, HttpSession session) {
        ResultResponseDto resultResponseDto = logOutService.logout(loginUser, session);
        return resultResponseDto;
    }
}
