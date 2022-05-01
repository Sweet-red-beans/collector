package moviegoods.movie.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviegoods.movie.domain.argumentresolver.Login;
import moviegoods.movie.domain.dto.mypage.*;
import moviegoods.movie.domain.entity.User.User;
import moviegoods.movie.service.MyPageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

    @RestController
    @Slf4j
    @RequiredArgsConstructor
    @RequestMapping("/mypage")
    public class MyPageController {


        private final MyPageService myPageService;

        @GetMapping
        public MyPageResponseSearch searchMyPage(@Login User loginUser, @ModelAttribute MyPageRequestSearch mprs){
            MyPageResponseSearch myPageResponseSearch=myPageService.search(loginUser,mprs);

            return myPageResponseSearch;
        }


        @PatchMapping("/profile")
        public Result updateProfile(@Login User loginUser, @RequestParam(value="file",required = false)MultipartFile file
                                    )throws Exception{

            MyPageRequestProfile mprp=new MyPageRequestProfile();
            mprp.setProfile_image(file);

            myPageService.updateProfile(loginUser,mprp);
            Result result=new Result();
            result.setResult(true);
            return result;
        }

        @PatchMapping("/nickname")
        public Result updateNickname(@Login User loginUser,@RequestBody MyPageRequestNickname mrnn){
            Result result=new Result();
            String name=myPageService.updateNickname(loginUser,mrnn);
            if(mrnn.getNickname().equals(name)){
                result.setResult(true);
            }
            else{
                result.setResult(false);
            }
            return result;

        }

        @DeleteMapping("/withdrawal")
        public Result withdrawal(@Login User loginUser, @RequestBody MyPageRequestWithdrawal mrwd){
            Boolean check=   myPageService.withdrawal(loginUser,mrwd);
            Result result=new Result();
            if(check==true){
                result.setResult(true);
            }else{
                result.setResult(false);
            }
            return result;

        }

        @PostMapping("/duplicate-check")
        public Result nicknameDuplicate(@RequestBody MyPageRequestNicknameDuplicateDto mprnd){
            Result result= myPageService.nicknameDuplicate(mprnd);
            return result;

        }
    }

