package moviegoods.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Service
@Slf4j
public class MovieVideoService {

    private WebDriver driver;
    private static String CHROMEDRIVER_PATH = "src/main/resources/chromedriver1.exe";

    public String startDriver() throws IOException, WebDriverException {
        System.out.println("Driver Start!!!");
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("--start-maximized"); // 전체화면으로 실행
        options.addArguments("--disable-popup-blocking"); // 팝업 무시
        options.addArguments("--disable-default-apps"); // 기본앱 사용안함
        //options.setCapability("ignoreProtected");
        //브라우저 로딩 타임아웃 5초
        // driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver = new ChromeDriver(options);
        driver.get("https://www.lottecinema.co.kr/NLCHS#");
        WebDriverWait wait = new WebDriverWait(driver, 100);
        String CSS_SELECTOR="div[class=\"owl-item cloned\"] > div.item img";

        List<WebElement >videos =  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(CSS_SELECTOR)));
        List<String> videoList=new ArrayList<>();


        for (WebElement video : videos) {
            String name= video.getAttribute("data-video");
            videoList.add(name);

        }

        driver.quit();
        if(videoList.size()>0){
            int num=videoList.size()-1;
            Random random=new Random();
            int idx=random.nextInt(num);

            return videoList.get(idx);
        }
        return " ";


    }

}
