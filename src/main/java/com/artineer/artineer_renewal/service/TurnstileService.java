package com.artineer.artineer_renewal.service;


import com.artineer.artineer_renewal.dto.Turnstile;
import com.artineer.artineer_renewal.dto.TurnstileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Service
@Slf4j
public class TurnstileService {

    @Value("${cloudflare.turnstile.secret-key}")
    private String secretKey;
    @Value("${cloudflare.turnstile.url}")
    private String turnstileUrl;

    public boolean getVerification(String token, String remoteip) throws ProtocolException {

        
        // todo 이거 대체 왜  ",인증키,이상한키?" 3개가 나오는 거야
        token = token.split(",")[1];
        WebClient webClient = WebClient.create("https://challenges.cloudflare.com/turnstile/v0/siteverify");

        // URL 인코딩된 문자열로 요청 데이터 생성
        String requestBody = "secret=" + secretKey + "&response=" + token;

//        System.out.println("\n\nRequest Body: " + requestBody);
//        System.out.println("\n\nHeaders: " + webClient.post().headers(headers -> headers.forEach((k, v) -> System.out.println(k + ": " + v))));

        // POST 요청 보내기
        TurnstileResponse turnstileResponse = webClient.post()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TurnstileResponse.class)
                .block(); // 동기식으로  응답 대기


        // 이거는 cloudflare 관리자 콘솔에서도 아마 볼 수 있을 것이다.
        if (!turnstileResponse.getSuccess()) log.info("turnstile: bot detected: {}", turnstileResponse);

        return turnstileResponse.getSuccess();
    }


}
