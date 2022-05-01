package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.configure.SessionConfig;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.entity.User.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogOutService {

    public ResultResponseDto logout(User loginUser, HttpSession session) {

        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if (session != null) {
            session.setAttribute(SessionConfig.SessionConst.LOGIN_MEMBER, null);
            log.info("sessionId= {}", session.getId());
            session.invalidate();
            resultResponseDto.setResult(true);
        }
        else {
            resultResponseDto.setResult(false);
        }

        return resultResponseDto;
    }
}

