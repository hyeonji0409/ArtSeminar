package fileETC;


import com.artineer.artineer_renewal.Entity.File;
import com.artineer.artineer_renewal.repository.FileRepository;
import com.artineer.artineer_renewal.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class FileController {


    private final FileService fileService;

    private final FileRepository fileRepository;


    // upload 페이지 이동

    @GetMapping("/upload")
    public String upload() {
        System.out.println("upload page");
        return "file/upload";
    }

    // upload 동작

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("files") List<MultipartFile> files) throws IOException {

        System.out.println("upload file");

        fileService.saveFile(file);

        for (MultipartFile file1 : files) {
            fileService.saveFile(file1);
        }

        return "redirect:/view";
    }

    // view 페이지 이동
    @GetMapping("/view")
    public String view(Model model) {
        List<File> files = fileRepository.findAll();
        model.addAttribute("allFiles", files);
        return "file/view";
    }

//     이미지 출력

    @GetMapping("/image/{fileId}")
    @ResponseBody
    public Resource downloadImage(@PathVariable("fileId") Long fileId,
                                Model model) throws IOException {
        File file = fileRepository.findById(fileId).orElse(null);

        return new UrlResource("file:" + file.getSavedPath());
    }


    //     이미지 다운로드

    @GetMapping("/attach/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadImage(@PathVariable("id") Long id) throws IOException {

        File file = fileRepository.findById(id).orElse(null);

        assert file != null;
        UrlResource resource = new UrlResource("file:" + file.getSavedPath());

        String encodedFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
