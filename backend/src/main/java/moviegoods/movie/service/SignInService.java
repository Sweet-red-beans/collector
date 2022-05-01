package moviegoods.movie.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.configure.SessionConfig.SessionConst;
import moviegoods.movie.domain.dto.signin.SignInRequestDto;
import moviegoods.movie.domain.dto.signin.SignInResponseDto;
import moviegoods.movie.domain.dto.signup.SignUpRequestDto;
import moviegoods.movie.domain.entity.User.Method;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.domain.entity.User.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static moviegoods.movie.domain.entity.User.UserStatus.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {

    private final PasswordEncoder passwordEncoder;
    private final SignUpService signUpService;
    private final EntityManager em;
    //private final GoogleConfigUtils googleConfigUtils;

    public SignInResponseDto login(SignInRequestDto requestDto, HttpServletRequest request) {
        SignInResponseDto signInResponseDto;
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();
        String method = requestDto.getMethod();

        String searchJpql = "select u from user u where u.email = '" + email + "' and u.method = '" + method + "'";
        User user;
        try{
            user = em.createQuery(searchJpql, User.class).getSingleResult();

        }catch (NoResultException e){
            signInResponseDto = new SignInResponseDto(null,null,정상, false, null);
            return signInResponseDto;
        }

        String existPassword = user.getPassword();

        if(passwordEncoder.matches(password, existPassword)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(30*60);
            session.setAttribute(SessionConst.LOGIN_MEMBER, user);
            @NotNull UserStatus status = user.getUser_status();
            Enum authority = user.getAuthority();

            signInResponseDto = new SignInResponseDto(user.getNickname(), user.getProfile_url(),status, true, authority.toString());

        }
        else {
            signInResponseDto = new SignInResponseDto(null,null,정상,false, null);
        }

        return signInResponseDto;

    }

    public String getKaKaoAccessToken(String code){

        String access_Token="";
        String refresh_Token ="";

        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=e64599af67aac20483ad02a14a8c5058"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:3000/signin/oauth2/code/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public SignInRequestDto getUserInfo(String access_Token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        SignInRequestDto signInRequestDto = new SignInRequestDto();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            Long lastUserId = getUserId();
            Long nxtUserId = lastUserId + 1;

            String originNickname = properties.getAsJsonObject().get("nickname").getAsString();
            String nickname = originNickname + nxtUserId.toString() + randomNumGen();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            String password = email + "kakao";

            SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
            signUpRequestDto.setNickname(nickname);
            signUpRequestDto.setEmail(email);
            signUpRequestDto.setPassword(password);
            String searchJpql = "select u from user u where u.email = '" + email + "' and u.method = '카카오'";
            List<User> user = em.createQuery(searchJpql, User.class).getResultList();

            if (user.size() == 0) {
                signUpService.saveUser(signUpRequestDto, Method.카카오);
            }

            signInRequestDto.setEmail(email);
            signInRequestDto.setPassword(password);
            signInRequestDto.setMethod("카카오");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return signInRequestDto;
    }

    public String googleRequestAccessToken(String code) {
        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v4/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", "435089655733-6v1fo661d0dda2ue3ql61420dtquril1.apps.googleusercontent.com");
            params.put("client_secret", "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");
            params.put("redirect_uri", "http://localhost:3000/signin/auth/google/callback");
            params.put("grant_type", "authorization_code");
            params.put("state", "url_parameter");

            String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
            bous.write(parameterString.getBytes());
            bous.flush();
            bous.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if(conn.getResponseCode() == 200) {
                return sb.toString();
            }
            return "구글 로그인 요청 처리 실패";
        }catch (IOException e) {
            throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다 :: " + "https://oauth2.googleapis.com/token");
        }
    }

    public SignInRequestDto googleGetUserInfo(String access_Token) {
        JsonParser accessTokenParser = new JsonParser();
        JsonElement getAccescTokenElement = accessTokenParser.parse(access_Token);
        String accessToken = getAccescTokenElement.getAsJsonObject().get("access_token").getAsString();

        String reqURL = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
        SignInRequestDto signInRequestDto = new SignInRequestDto();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            Long lastUserId = getUserId();
            Long nxtUserId = lastUserId + 1;

            String originNickname = element.getAsJsonObject().get("name").getAsString();
            String nickname = originNickname + nxtUserId.toString() + randomNumGen();
            System.out.println("nickname : " + nickname);

            String email = element.getAsJsonObject().get("email").getAsString();
            String password = email + "google";

            SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
            signUpRequestDto.setNickname(nickname);
            signUpRequestDto.setEmail(email);
            signUpRequestDto.setPassword(password);
            String searchJpql = "select u from user u where u.email = '" + email + "' and u.method = '구글'";
            List<User> user = em.createQuery(searchJpql, User.class).getResultList();

            if (user.size() == 0) {
                signUpService.saveUser(signUpRequestDto, Method.구글);
            }

            signInRequestDto.setEmail(email);
            signInRequestDto.setPassword(password);
            signInRequestDto.setMethod("구글");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return signInRequestDto;
    }
    public static String randomNumGen() {
        Random random = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            //0~9 난수 생성
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        return numStr;
    }

    public Long getUserId() {
        String getLastUserIdJpql = "select MAX(u.user_id) from user u";
        Object lastUserId = em.createQuery(getLastUserIdJpql).getSingleResult();
        return Long.valueOf(String.valueOf(lastUserId));
    }
}