package fileETC;


import com.artineer.artineer_renewal.Entity.File;
import com.artineer.artineer_renewal.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    @Value("${file.dir}")
    private String fileDir;

    public Long saveFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        //원래 파일 네임 추출
        String originalFileName = files.getOriginalFilename();

        //기존 파일 네임에서 uuid 로 사용
        String uuid = UUID.randomUUID().toString();

        // 확장자
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String savedFileName = uuid + extension;

        String savedFilePath = fileDir + savedFileName;

        File file = File.builder()
                .originalFilename(originalFileName)
                .savedFilename(savedFileName)
                .savedPath(savedFilePath)
                .build();

        files.transferTo(new java.io.File(savedFilePath));

        File savedFile = fileRepository.save(file);


        return savedFile.getId();
    }

}
