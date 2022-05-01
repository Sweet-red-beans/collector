package moviegoods.movie.configure;

import moviegoods.movie.domain.argumentresolver.LoginMemberArgumentResolver;
import moviegoods.movie.domain.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/direct-message", "/mypage")
                .excludePathPatterns("/main/**", "/members/add", "/signin", "/logout",
                        "/css/**", "/*.ico", "/error");
    }
}