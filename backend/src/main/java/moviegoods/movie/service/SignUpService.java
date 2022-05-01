package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import moviegoods.movie.domain.dto.booleanResult.ResultResponseDto;
import moviegoods.movie.domain.dto.signup.SignUpDuplicateCheckRequestDto;
import moviegoods.movie.domain.dto.signup.SignUpRequestDto;
import moviegoods.movie.domain.entity.User.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static moviegoods.movie.domain.entity.User.Method.*;
import static moviegoods.movie.domain.entity.User.UserStatus.정상;


@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static String basicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlx2rvqRVwn6S5uXPkHl856CcYvV2z8bUMyw&usqp=CAU";
    private static Long basicReliability = 0L;
    private static UserStatus basicStatus = 정상;

    public ResultResponseDto saveUser(SignUpRequestDto requestDto, Method method) {
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();

        String encodedPassword = passwordEncoder.encode(password);
        User saveEntity = new User();

        if (method==일반) {
            saveEntity = User.builder().authority(Authority.일반).email(email).user_status(basicStatus).reliability(basicReliability).
                    nickname(nickname).password(encodedPassword).profile_url(basicUrl).method(일반).build();
        }
        if (method==카카오) {
            saveEntity = User.builder().authority(Authority.일반).email(email).user_status(basicStatus).reliability(basicReliability).
                    nickname(nickname).password(encodedPassword).profile_url(basicUrl).method(카카오).build();
        }
        if (method==구글) {
            saveEntity = User.builder().authority(Authority.일반).email(email).user_status(basicStatus).reliability(basicReliability).
                    nickname(nickname).password(encodedPassword).profile_url(basicUrl).method(구글).build();
        }

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        userRepository.save(saveEntity);
        resultResponseDto.setResult(true);

        return resultResponseDto;
    }

    public ResultResponseDto duplicateCheck(SignUpDuplicateCheckRequestDto requestDto,Method method) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        User user = new User();
        if (email != null) {
            user = (User) userRepository.findByEmailAndMethod(email,method).orElse(null);
        }
        if (nickname != null) {
            user = userRepository.findByNickname(nickname).orElse(null);
        }

        resultResponseDto.setResult(true);
        if (user == null) {
            resultResponseDto.setResult(false);
        }

        return resultResponseDto;
    }

}