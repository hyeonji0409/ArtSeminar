package com.artineer.artineer_renewal.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InformationFileDataService {
    private static final Logger log = LoggerFactory.getLogger(InformationFileDataService.class);
    @Value("${file.informationFileData-dir}")
    private String sourceDir;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, String> dataSource = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        File file = new File(sourceDir);
        if (file.exists()) {
            // JSON 파일을 읽어서 Map으로 변환
            dataSource = objectMapper.readValue(file, Map.class);
        } else {
            // 파일 경로가 존재하지 않으면 디렉터리를 생성
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, dataSource); // 빈 JSON 파일 생성
        }
    }

    public boolean set(String key, String value) {
        dataSource.put(key, value);
        try {
            objectMapper.writeValue(new File(sourceDir), dataSource);
        } catch (IOException e) {
            log.error("Failed to write data to file", e);
            return false;
        }

        return true;
    }

    public String get(String key) {
        return dataSource.get(key);
    }

    public boolean delete(String key) {
        dataSource.remove(key);

        try {
            objectMapper.writeValue(new File(sourceDir), dataSource);
        } catch (Exception e) {
            log.error("Failed to write data to file", e);
            return false;
        }
        return true;
    }
}
