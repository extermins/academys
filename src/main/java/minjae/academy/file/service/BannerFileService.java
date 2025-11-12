package minjae.academy.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerFileService {
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    // 이미지 저장하고 저장위치를 반환하는 코드
    // 나중에 저장위치를 다른 서버?로 변경해서 저장소만 따로 관리하는 법을 하고싶은데
    public String saveBannerImage(MultipartFile image) throws IOException {
        String bannerPath = uploadDirectory + "/banner";
        File imageDirectory = new File(bannerPath);
        System.out.println(uploadDirectory);
        System.out.println("이미지 저장 위치 확인 : " + imageDirectory.toString());

        if (!imageDirectory.exists()) {
            Boolean makeDirectorySuccess = imageDirectory.mkdirs();
            if (makeDirectorySuccess) {
                System.out.println("배너 이미지 저장 디렉토리 생성 성공");
            }else{
                throw new IOException("배너 이미지 저장 디렉토리 생성 실패");
            }
        }

        String fileName = image.getOriginalFilename();
        String extension = "";

        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("EXTENSION : " + extension);
        }

        String savedFileName = UUID.randomUUID().toString() + extension;

        System.out.println("savedFileName : " + savedFileName);
        Path path = Paths.get(imageDirectory + "/" + savedFileName);

        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return savedFileName;

    }

}
