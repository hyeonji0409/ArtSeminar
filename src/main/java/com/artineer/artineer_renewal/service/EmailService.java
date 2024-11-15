package com.artineer.artineer_renewal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
// todo 트랜젝셔널이 뭔가
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;
    private final ConcurrentHashMap<String, Verification> verificationStore = new ConcurrentHashMap<>();

    public void sendEmail(String toEmail,
                          String title,
                          String text) {

        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.warn("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
//            throw new  BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
//            throw e;
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public String createVerificationCode(String email) {

        // 유효한 이메일 인증시도가 있다면 지우고 다시 생성
        if (!verificationStore.containsKey(email)) {
            this.removeVerificationCode(email);
        }

        String randomString = getRandomString(5, true);

        Verification ve = new Verification(email, randomString, LocalDateTime.now().plusMinutes(5));

        verificationStore.put(email, ve);

        return randomString;
    }


    public String getVerificationCode(String email) {
        Verification ve = verificationStore.get(email);

        if (ve == null || ve.expirationDateTime.isBefore(LocalDateTime.now())) {
            if (ve!=null) removeVerificationCode(email);
            return null;
        }

        return ve.randomString;
    }

    public void removeVerificationCode(String email) {
        verificationStore.remove(email);
    }

    public List<String> getVerificationMapToList() {
        List<String> list = new ArrayList<>(verificationStore.keySet());
        for (Map.Entry<String, Verification> v: verificationStore.entrySet()) {
            list.add(v.getKey() +" : "+ v.getValue().randomString + " ["+ v.getValue().expirationDateTime +"]");
        }
        return list;
    }


    public String getRandomString(int length, boolean isNeedUpperCase) {
        String randomString = new Random().ints(48, 122)
                .filter(i -> isNeedUpperCase? Character.isLetterOrDigit(i) : (Character.isLowerCase(i)||Character.isDigit(i)))
                .limit(length)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());

        return randomString;
    }

    public void resetVerification() {
        verificationStore.clear();
    }


}


class Verification {
    final LocalDateTime expirationDateTime;
    final String randomString;

    public Verification(String email , String randomString, LocalDateTime expirationDateTime) {
        this.randomString = randomString;
        this.expirationDateTime = expirationDateTime;
    }
}