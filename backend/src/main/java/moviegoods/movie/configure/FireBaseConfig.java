package moviegoods.movie.configure;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Service
@Slf4j
public class FireBaseConfig {


    @PostConstruct
    public void initialize(){
        try{
            FileInputStream serviceAccount=new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options= new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).
                    setDatabaseUrl("https://stroagetest.firebaseio.com/").build();
            FirebaseApp.initializeApp(options);
           /* FirebaseOptions options=new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(
                    new ClassPathResource(firebaseConfigPath).getInputStream()
            )).build();
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }*/

        }catch(IOException e){
            log.error(e.getMessage());
        }
    }
}
