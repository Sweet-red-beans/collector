package moviegoods.movie.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class FireBaseService {

    public String uploadFiles(MultipartFile file, String nameFile)throws IOException, FirebaseAuthException {

        Bucket bucket= StorageClient.getInstance().bucket("stroagetest-f0778.appspot.com");

        InputStream content=new ByteArrayInputStream(file.getBytes());
        Blob blob=bucket.create(nameFile.toString(),content,file.getContentType());
        return blob.getMediaLink();

    }

    public String uploadFiles2(byte[] decodeByte, String contentType, String nameFile) throws IOException, FirebaseAuthException {

        Bucket bucket= StorageClient.getInstance().bucket("stroagetest-f0778.appspot.com");
        InputStream content=new ByteArrayInputStream(decodeByte);
        Blob blob=bucket.create(nameFile.toString(),content,contentType);

        return blob.getMediaLink();

    }
}
